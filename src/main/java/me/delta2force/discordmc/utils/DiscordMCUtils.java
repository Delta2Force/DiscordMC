package me.delta2force.discordmc.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;

import me.delta2force.discordmc.DiscordMCPlugin;
import me.delta2force.discordmc.chat.Chat;
import me.delta2force.discordmc.interaction.Interaction;
import me.delta2force.discordmc.interaction.Interaction.InteractiveEnum;
import me.delta2force.discordmc.maprenderer.DiscordMapRenderer;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.entities.MessageEmbed.ImageInfo;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class DiscordMCUtils implements EventListener {
	private DiscordMCPlugin discordMC;
	private JDA jdaClient;
	public Map<UUID, DiscordSession> sessions = new HashMap<UUID, DiscordSession>();
	public Map<IntVector, Interaction> interactions = new HashMap<>();
	public Map<UUID, Chat> chats = new HashMap<>();
	
	public DiscordMCUtils(DiscordMCPlugin discordMC) {
		this.discordMC = discordMC;
		try {
			this.jdaClient = new JDABuilder(discordMC.getConfig().getString("botToken")).build();
			this.jdaClient.awaitReady();
			this.jdaClient.addEventListener(this);
		} catch (LoginException | InterruptedException e) {
			Bukkit.broadcastMessage(getPrefix() + ChatColor.RED + "There was an error while logging in using DiscordMC! "
					+ "Tell the admins to look in the console and create an issue on the GitHub page! I'm outta here.");
			Bukkit.getServer().getPluginManager().disablePlugin(discordMC);
			e.printStackTrace();
		}
	}
	
	public DiscordSession getPlayerSession(Player p) {
		return sessions.get(p.getUniqueId());
	}
	
	public void addPlayer(Player p) {
		if(sessions.containsKey(p.getUniqueId())) {
			p.sendMessage(getPrefix() + ChatColor.RED + "You are already using Discord you dumb dumb!");
		}else {
			sessions.put(p.getUniqueId(), new DiscordSession(p.getUniqueId()));
			p.sendMessage(getPrefix() + ChatColor.YELLOW + "The interface is being set up...");
			setupInterface(p);
			p.sendMessage(getPrefix() + ChatColor.GREEN + "Please select the server to use!");
		}
	}
	
	public void setupInterface(Player p) {
		List<Guild> guilds = jdaClient.getGuilds();
		Location topLoc = randomTopCoordinate();
		int x = 0;
		for(Guild g : guilds) {
			topLoc.clone().add(x, 0, 0).getBlock().setType(Material.SMOOTH_STONE);
			topLoc.clone().add(x, -1, 0).getBlock().setType(Material.SMOOTH_STONE);
			topLoc.clone().add(x, -2, -1).getBlock().setType(Material.STONE_BRICKS);
			topLoc.clone().add(x, -2, -2).getBlock().setType(Material.STONE_BRICKS);
			topLoc.clone().add(x, -2, -3).getBlock().setType(Material.STONE_BRICKS);
			
			ItemFrame itf = (ItemFrame) topLoc.getWorld().spawnEntity(topLoc.clone().add(x, 0, -1), EntityType.ITEM_FRAME);
			itf.setFacingDirection(BlockFace.NORTH);
			String icon = g.getIconUrl();
			itf.setItem(createNamedMapWithURL(icon, g.getName(), topLoc.getWorld()));
			
			Block b = topLoc.clone().add(x, -1, -1).getBlock();
			b.setType(Material.DARK_OAK_BUTTON);
			Directional directional = (Directional) b.getBlockData();
			directional.setFacing(BlockFace.NORTH);
			b.setBlockData(directional);
			interactions.put(new IntVector(b.getLocation()), new Interaction(InteractiveEnum.JOIN_SERVER, g.getId()));
			x++;
		}
		p.setGameMode(GameMode.ADVENTURE);
		p.teleport(topLoc.clone().add(0, 1, 0));
	}
	
	public ItemStack createMapWithURL(String url, World world) {
		ItemStack it = new ItemStack(Material.FILLED_MAP);
		MapMeta mm = (MapMeta) it.getItemMeta();
		mm.setMapView(Bukkit.createMap(world));
		mm.getMapView().addRenderer(new DiscordMapRenderer(url, discordMC));
		it.setItemMeta(mm);
		return it;
	}
	
	public ItemStack createNamedMapWithURL(String url, String name, World world) {
		if(url == null) {
			url = "https://i.imgur.com/rkRZl9a.png";
		}
		ItemStack it = createMapWithURL(url, world);
		ItemMeta im = it.getItemMeta();
		im.setDisplayName(name);
		it.setItemMeta(im);
		return it;
	}
	
	public World getDiscordWorld() {
		WorldCreator wc = new WorldCreator("discord");
		wc.type(WorldType.FLAT);
		return wc.createWorld();
	}
	
	public Location randomTopCoordinate() {
		Random r = new Random();
		return new Location(getDiscordWorld(), r.nextInt(2000000)-1000000, 255, r.nextInt(2000000)-1000000);
	}
	
	public String getPrefix() {
		return ChatColor.BLUE + "[DiscordMC] ";
	}
	
	public JDA getClient() {
		return jdaClient;
	}
	
	public ArmorStand spawnHologram(Location l, String name) {
        ArmorStand as = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
        as.setCustomName(name);
        as.setCustomNameVisible(true);
        as.setGravity(false);
        as.setVisible(false);
        as.setInvulnerable(true);
        as.setCollidable(false);
        return as;
    }
	
	public void executeInteraction(Interaction i, Player executor) {
		if(i.interactionType == InteractiveEnum.JOIN_SERVER) {
			chats.put(executor.getUniqueId(), new Chat(randomTopCoordinate(), executor, this, i.value));
		}
	}
	
	public DiscordMCPlugin getDiscordMC() {
		return discordMC;
	}

	@Override
	public void onEvent(Event event) {
		if(event instanceof MessageReceivedEvent) {
			MessageReceivedEvent mre = (MessageReceivedEvent) event;
			for(Chat c : chats.values()) {
				if(c.serverId.equals(mre.getGuild().getId()) && c.channelId.equals(mre.getChannel().getId())) {
					if(mre.getMessage().getEmbeds().size() > 0) {
						ImageInfo ii = mre.getMessage().getEmbeds().get(0).getImage();
						if(ii != null) {
							c.addEntry(new Chat.ChatEntry(ii.getUrl(), mre.getAuthor().getId(), true));
						}else {
							c.addEntry(new Chat.ChatEntry(mre.getMessage().getContentRaw(), mre.getAuthor().getId(), false));
						}
					}else {
						c.addEntry(new Chat.ChatEntry(mre.getMessage().getContentRaw(), mre.getAuthor().getId(), false));
					}
				}
			}
		}
	}
}
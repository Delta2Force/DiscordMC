package me.delta2force.discordmc.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import org.bukkit.inventory.meta.MapMeta;

import me.delta2force.discordmc.DiscordMCPlugin;
import me.delta2force.discordmc.maprenderer.DiscordMapRenderer;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.entities.impl.InviteImpl;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class DiscordMCUtils implements EventListener{
	private DiscordMCPlugin discordMC;
	private JDA jdaClient;
	public HashMap<UUID, DiscordSession> sessions = new HashMap<UUID, DiscordSession>();
	
	public DiscordMCUtils(DiscordMCPlugin discordMC) {
		this.discordMC = discordMC;
		try {
			this.jdaClient = new JDABuilder(discordMC.getConfig().getString("botToken")).build();
			this.jdaClient.awaitReady();
			this.jdaClient.addEventListener(this);
		} catch (LoginException | InterruptedException e) {
			Bukkit.broadcastMessage(getPrefix() + ChatColor.RED + "There was an error while logging in using DiscordMC!"
					+ "Tell the admins to look in the console! I'm outta here.");
			Bukkit.getServer().getPluginManager().disablePlugin(discordMC);
			e.printStackTrace();
		}
	}
	
	public DiscordSession getPlayerSession(Player p) {
		return sessions.get(p.getUniqueId());
	}
	
	public void addPlayer(Player p) {
		sessions.put(p.getUniqueId(), new DiscordSession(p.getUniqueId()));
		p.sendMessage(getPrefix() + ChatColor.YELLOW + "The interface is being set up...");
		setupInterface(p);
	}
	
	public void setupInterface(Player p) {
		List<Guild> guilds = jdaClient.getGuilds();
		Location topLoc = randomTopCoordinate();
		int x = 0;
		for(Guild g : guilds) {
			topLoc.clone().add(x, 0, 0).getBlock().setType(Material.SMOOTH_STONE);
			topLoc.clone().add(x, -1, 0).getBlock().setType(Material.SMOOTH_STONE);
			
			ItemFrame itf = (ItemFrame) topLoc.getWorld().spawnEntity(topLoc.clone().add(x, 0, -1), EntityType.ARMOR_STAND);
			itf.setFacingDirection(BlockFace.NORTH);
			itf.setItem(createNamedMapWithURL(g.getIconUrl(), g.getName(), topLoc.getWorld()));
			
			Block b = topLoc.clone().add(x, -1, -1).getBlock();
			b.setType(Material.DARK_OAK_BUTTON);
			Directional directional = (Directional) b.getBlockData();
			directional.setFacing(BlockFace.NORTH);
			b.setBlockData(directional);
		}
		p.setGameMode(GameMode.SPECTATOR);
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
		ItemStack it = new ItemStack(Material.FILLED_MAP);
		MapMeta mm = (MapMeta) it.getItemMeta();
		mm.setDisplayName(name);
		mm.setMapView(Bukkit.createMap(world));
		mm.getMapView().addRenderer(new DiscordMapRenderer(url, discordMC));
		it.setItemMeta(mm);
		return it;
	}
	
	public World getWorld() {
		WorldCreator wc = new WorldCreator("discord");
		wc.type(WorldType.FLAT);
		return wc.createWorld();
	}
	
	public Location randomTopCoordinate() {
		Random r = new Random();
		return new Location(getWorld(), r.nextInt(2000000)-1000000, 255, r.nextInt(2000000)-1000000);
	}
	
	public String getPrefix() {
		return ChatColor.BLUE + "[DiscordMC] ";
	}
	
	public JDA getClient() {
		return jdaClient;
	}
	
	public void spawnHologram(Location l, String name) {
        ArmorStand as = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
        as.setCustomName(name);
        as.setCustomNameVisible(true);
        as.setGravity(false);
        as.setVisible(false);
        as.setInvulnerable(true);
        as.setCollidable(false);
    }

	@Override
	public void onEvent(Event event) {
		if(event instanceof MessageReceivedEvent) {
			MessageReceivedEvent mre = (MessageReceivedEvent) event;
			if(mre.getMessage().getInvites().size() > 0) {
				for(String s : mre.getMessage().getInvites()) {
					Invite i = Invite.resolve(jdaClient, s).complete();
					mre.getChannel().sendMessage("Joined " + i.getGuild().getName());
				}
			}
		}
	}
}
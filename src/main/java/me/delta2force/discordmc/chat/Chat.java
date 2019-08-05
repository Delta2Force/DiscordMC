package me.delta2force.discordmc.chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import com.google.gson.Gson;

import me.delta2force.discordmc.utils.DiscordMCUtils;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class Chat {
	public Location topRight;
	public DiscordMCUtils utils;
	public ArrayList<ChatEntry> entries;
	public String channelId;
	public Player owner;
	public String serverId;
	private ArrayList<ItemFrame> itemFrames;
	private ArrayList<Block> signs;
	private ArmorStand titleArmorStand;
	
	public Chat(Location topRight, Player owner, DiscordMCUtils utils, String serverId) {
		this.topRight = topRight;
		this.utils = utils;
		this.owner = owner;
		this.serverId = serverId;
		this.channelId = utils.getClient().getGuildById(serverId).getDefaultChannel().getId();
		this.entries = new ArrayList<>();
		this.itemFrames = new ArrayList<>();
		this.signs = new ArrayList<>();
		build();
		update();
	}
	
	public void addEntry(ChatEntry entry) {
		if(entries.size() > 5) {
			entries.remove(0);
		}
		entries.add(entry);
		update();
	}
	
	public void changeChannel(String newChannel) {
		this.channelId = newChannel;
		entries.clear();
		update();
	}
	
	public void update() {
		Bukkit.getScheduler().runTask(utils.getDiscordMC(), new Runnable() {
			
			@Override
			public void run() {
				for(ItemFrame itf : itemFrames) {
					itf.teleport(itf.getLocation().add(0, -100, 0));	
					itf.remove();
				}
				itemFrames.clear();
				for(Block b : signs) {
					b.setType(Material.AIR);
				}
				signs.clear();
				if(titleArmorStand != null) {
					titleArmorStand.remove();
				}
				titleArmorStand = spawnHologram(topRight.clone().add(4, 1, 0), utils.getClient().getTextChannelById(channelId).getName());
				Location topLeft = topRight.clone().add(7, 0, -1);
				for(ChatEntry entry : entries) {
					Location loc = topLeft.add(0, -1, 0);
					ItemFrame authorPic = (ItemFrame) topLeft.getWorld().spawnEntity(loc, EntityType.ITEM_FRAME);
					authorPic.setFacingDirection(BlockFace.NORTH);
					User author = utils.getClient().getUserById(entry.authorId);
					authorPic.setItem(utils.createNamedMapWithURL(author.getAvatarUrl(), author.getName(), topLeft.getWorld()));
					itemFrames.add(authorPic);
					
					if(entry.isImage) {
						ItemFrame image = (ItemFrame) topLeft.getWorld().spawnEntity(loc.clone().add(-1, 0, 0), EntityType.ITEM_FRAME);
						image.setFacingDirection(BlockFace.NORTH);
						image.setItem(utils.createMapWithURL(entry.message, topLeft.getWorld()));
						itemFrames.add(image);
					}else {
						if(entry.message.length() < 15) {
							Location currentSignLoc = loc.clone().add(-1, 0, 0);
							Sign currentSign = createWallSign(currentSignLoc);
							currentSign.setLine(0, entry.message);
							currentSign.update();
						}else{
							String[] lines = entry.message.split("(?<=\\G.{15})");
							Location currentSignLoc = loc.clone().add(-1, 0, 0);
							int signIndex = 0;
							Sign currentSign = createWallSign(currentSignLoc);
							for(String s : lines) {
								if(signIndex == 4) {
									signIndex = 0;
									currentSignLoc.add(-1, 0, 0);
									currentSign = createWallSign(currentSignLoc);
								}
								currentSign.setLine(signIndex, s);
								currentSign.update();
								signIndex++;
							}
						}
					}
				}
			}
		});
	}
	
	public Sign createWallSign(Location loc) {
		Block b = loc.getBlock();
		b.setType(Material.OAK_WALL_SIGN);
		Sign currentSign = (Sign) b.getState();
		Directional directional = (Directional) b.getBlockData();
		directional.setFacing(BlockFace.NORTH);
		b.setBlockData(directional);
		signs.add(b);
		return currentSign;
	}
	
	public void build() {
		for(int x = topRight.getBlockX();x<topRight.getBlockX()+8;x++) {
			for(int y = topRight.getBlockY();y>topRight.getBlockY()-8;y--) {
				Location loc = new Location(topRight.getWorld(), x, y, topRight.getZ());
				loc.getBlock().setType(Material.GRAY_WOOL);
			}
			Location loc = new Location(topRight.getWorld(), x, topRight.getBlockY()-8, topRight.getZ()-1);
			loc.getBlock().setType(Material.SLIME_BLOCK);
			loc = new Location(topRight.getWorld(), x, topRight.getBlockY()-8, topRight.getZ()-2);
			loc.getBlock().setType(Material.SLIME_BLOCK);
			loc = new Location(topRight.getWorld(), x, topRight.getBlockY()-8, topRight.getZ()-3);
			loc.getBlock().setType(Material.SLIME_BLOCK);
		}
		
		double y = 0;
		for(TextChannel tc : utils.getClient().getGuildById(serverId).getTextChannels()) {
			spawnHologram(topRight.clone().add(10, (-1)-y, -2), "#"+tc.getName());
			y+=0.5;
		}
		owner.teleport(topRight.clone().add(0,1,0));
	}
	
	public ArmorStand spawnHologram(Location l, String name) {
        return utils.spawnHologram(l, name);
    }
	
	public static class ChatEntry {
		public String authorId;
		public String message;
		public boolean isImage;
		
		public ChatEntry(String message, String authorId, boolean isImage) {
			this.message=message;
			this.authorId=authorId;
			this.isImage=isImage;
		}
	}
}

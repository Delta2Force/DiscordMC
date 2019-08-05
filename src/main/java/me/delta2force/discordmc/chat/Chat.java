package me.delta2force.discordmc.chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import me.delta2force.discordmc.utils.DiscordMCUtils;
import net.dv8tion.jda.core.entities.User;

public class Chat {
	public Location topRight;
	public DiscordMCUtils utils;
	public ArrayList<ChatEntry> entries;
	public String channelId;
	public Player owner;
	public String serverId;
	private ArrayList<ItemFrame> itemFrames;
	private ArrayList<Sign> signs;
	private ArmorStand titleArmorStand;
	
	public Chat(Location topRight, Player owner, DiscordMCUtils utils, String serverId) {
		this.topRight = topRight;
		this.utils = utils;
		this.owner = owner;
		this.serverId = serverId;
		build();
	}
	
	public void addEntry(ChatEntry entry) {
		if(entries.size() > 7) {
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
		titleArmorStand.remove();
		titleArmorStand = spawnHologram(topRight.clone().add(4, 1, 0), utils.getClient().getTextChannelById(channelId).getName());
		for(ItemFrame itf : itemFrames) {
			itf.remove();
		}
		for(Sign s : signs) {
			s.setType(Material.AIR);
		}
		
		Location topLeft = topRight.clone().add(7, 0, -1);
		for(ChatEntry entry : entries) {
			Location loc = topLeft.clone().add(0, -1, 0);
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
				String[] lines = entry.message.split("(?<=\\G.{15})");
				Location currentSignLoc = loc.clone().add(-1, 0, 0);
				int signIndex = 0;
				Sign currentSign = (Sign) currentSignLoc.getBlock();
				currentSign.setType(Material.OAK_SIGN);
				signs.add(currentSign);
				for(String s : lines) {
					if(signIndex == 3) {
						signIndex = 0;
						currentSignLoc.add(-1, 0, 0);
						currentSign = (Sign) currentSignLoc.getBlock();
						currentSign.setType(Material.OAK_SIGN);
						signs.add(currentSign);
					}
					currentSign.setLine(signIndex, s);
				}
			}
		}
	}
	
	public void build() {
		for(int x = topRight.getBlockX();x<topRight.getBlockX()+8;x++) {
			for(int y = topRight.getBlockY();y>topRight.getBlockY()-8;y--) {
				Location loc = new Location(topRight.getWorld(), x, y, topRight.getZ());
				loc.getBlock().setType(Material.GRAY_WOOL);
			}
			Location loc = new Location(topRight.getWorld(), x, topRight.getBlockY()-8, topRight.getZ()-1);
			loc.getBlock().setType(Material.GRAY_WOOL);
			loc = new Location(topRight.getWorld(), x, topRight.getBlockY()-8, topRight.getZ()-1);
			loc.getBlock().setType(Material.GRAY_WOOL);
			loc = new Location(topRight.getWorld(), x, topRight.getBlockY()-8, topRight.getZ()-2);
			loc.getBlock().setType(Material.GRAY_WOOL);
			loc = new Location(topRight.getWorld(), x, topRight.getBlockY()-8, topRight.getZ()-3);
			loc.getBlock().setType(Material.GRAY_WOOL);
		}
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

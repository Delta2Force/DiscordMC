package me.delta2force.discordmc.chat;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import me.delta2force.discordmc.utils.DiscordMCUtils;

public class Chat {
	public Location topRight;
	public DiscordMCUtils utils;
	public ArrayList<ChatEntry> entries;
	public String channelId;
	public Player owner;
	
	public Chat(Location topRight, Player owner, DiscordMCUtils utils, String serverId) {
		this.topRight=topRight;
		this.utils=utils;
		this.owner = owner;
		//utils.getClient().getGuildById(serverId)
	}
	
	public void build() {
		
	}
	
	public ArmorStand spawnHologram(Location l, String name) {
        return utils.spawnHologram(l, name);
    }
	
	public static class ChatEntry {
		public String authorId;
		public String message;
	}
}

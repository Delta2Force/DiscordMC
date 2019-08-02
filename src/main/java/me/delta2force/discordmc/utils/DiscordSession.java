package me.delta2force.discordmc.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DiscordSession {
	private UUID uuid;
	
	public DiscordSession(UUID uuid) {
		this.uuid=uuid;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}
}

package me.delta2force.discordmc.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DiscordSession {
	private UUID uuid;
	//private SessionState state;
	
	public DiscordSession(UUID uuid) {
		this.uuid = uuid;
		//this.state = SessionState.SERVER_WALL;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}
	
	/*public SessionState getState() {
		return state;
	}
	
	public void setState(SessionState state) {
		this.state = state;
	}
	
	public static enum SessionState{
		SERVER_WALL, CHAT
	}*/
}

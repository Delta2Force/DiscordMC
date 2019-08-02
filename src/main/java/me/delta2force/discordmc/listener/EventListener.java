package me.delta2force.discordmc.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.delta2force.discordmc.DiscordMCPlugin;

public class EventListener implements Listener{
	private DiscordMCPlugin discordMC;
	
	public EventListener(DiscordMCPlugin discordMC) {
		this.discordMC=discordMC;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		
	}
}

package me.delta2force.discordmc.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.delta2force.discordmc.DiscordMCPlugin;
import me.delta2force.discordmc.interaction.Interaction;
import me.delta2force.discordmc.interaction.Interaction.InteractiveEnum;
import me.delta2force.discordmc.utils.IntVector;

public class EventListener implements Listener{
	private DiscordMCPlugin discordMC;
	
	public EventListener(DiscordMCPlugin discordMC) {
		this.discordMC=discordMC;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if(discordMC.getUtils().interactions.containsKey(new IntVector(event.getClickedBlock().getLocation()))) {
			Interaction i = discordMC.getUtils().interactions.get(new IntVector(event.getClickedBlock().getLocation()));
			discordMC.getUtils().executeInteraction(i, event.getPlayer());
		}
	}
}

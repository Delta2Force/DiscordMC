package me.delta2force.discordmc;

import org.bukkit.plugin.java.JavaPlugin;

import me.delta2force.discordmc.listener.EventListener;

public class DiscordMCPlugin extends JavaPlugin{
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
	}
}

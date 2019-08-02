package me.delta2force.discordmc.utils;

import java.util.HashMap;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.delta2force.discordmc.DiscordMCPlugin;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.EventListener;

public class DiscordMCUtils implements EventListener{
	private DiscordMCPlugin discordMC;
	private JDA jdaClient;
	public HashMap<UUID, DiscordSession> sessions = new HashMap<UUID, DiscordSession>();
	
	public DiscordMCUtils(DiscordMCPlugin discordMC) {
		this.discordMC = discordMC;
		try {
			this.jdaClient = new JDABuilder(discordMC.getConfig().getString("botToken")).build();
			this.jdaClient.addEventListener(this);
		} catch (LoginException e) {
			Bukkit.broadcastMessage(getPrefix() + ChatColor.RED + "There was an error while logging in using DiscordMC! Tell the admins to look in the console! I'm outta here.");
			Bukkit.getServer().getPluginManager().disablePlugin(discordMC);
			e.printStackTrace();
		}
	}
	
	public void addPlayer(Player p) {
		sessions.put(p.getUniqueId(), new DiscordSession(p.getUniqueId()));
	}
	
	public String getPrefix() {
		return ChatColor.BLUE + "[DiscordMC] ";
	}
	
	public JDA getClient() {
		return jdaClient;
	}

	@Override
	public void onEvent(Event event) {
	}
}
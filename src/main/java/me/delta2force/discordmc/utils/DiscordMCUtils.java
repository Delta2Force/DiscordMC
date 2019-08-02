package me.delta2force.discordmc.utils;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.delta2force.discordmc.DiscordMCPlugin;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class DiscordMCUtils {
	private DiscordMCPlugin discordMC;
	private JDA jdaClient;
	
	public DiscordMCUtils(DiscordMCPlugin discordMC) {
		this.discordMC = discordMC;
		try {
			this.jdaClient = new JDABuilder(discordMC.getConfig().getString("botToken")).build();
		} catch (LoginException e) {
			Bukkit.broadcastMessage(ChatColor.BLUE + "[DiscordMC] " + ChatColor.RED + "There was an error while logging in using DiscordMC! Tell the admins to look in the console! I'm outta here.");
			Bukkit.getServer().getPluginManager().disablePlugin(discordMC);
			e.printStackTrace();
		}
	}
	
	public JDA getClient() {
		return jdaClient;
	}
}

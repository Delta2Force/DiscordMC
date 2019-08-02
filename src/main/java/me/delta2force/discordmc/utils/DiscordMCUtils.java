package me.delta2force.discordmc.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import me.delta2force.discordmc.DiscordMCPlugin;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
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
			Bukkit.broadcastMessage(getPrefix() + ChatColor.RED + "There was an error while logging in using DiscordMC!"
					+ "Tell the admins to look in the console! I'm outta here.");
			Bukkit.getServer().getPluginManager().disablePlugin(discordMC);
			e.printStackTrace();
		}
	}
	
	public DiscordSession getPlayerSession(Player p) {
		return sessions.get(p.getUniqueId());
	}
	
	public void addPlayer(Player p) {
		sessions.put(p.getUniqueId(), new DiscordSession(p.getUniqueId()));
		p.sendMessage(getPrefix() + ChatColor.YELLOW + "The interface is being set up...");
		setupInterface(p);
	}
	
	public void setupInterface(Player p) {
		List<Guild> guilds = jdaClient.getGuilds();
		int i = 0;
		for(Guild g : guilds) {
			Location topLoc = randomTopCoordinate();
			i++;
			p.sendMessage(getPrefix() + ChatColor.GREEN + "Guild " + g.getName() + " set up. ("+i+"/"+guilds.size()+")");
		}
	}
	
	public World getWorld() {
		WorldCreator wc = new WorldCreator("discord");
		wc.type(WorldType.FLAT);
		return wc.createWorld();
	}
	
	public Location randomTopCoordinate() {
		Random r = new Random();
		return new Location(getWorld(), r.nextInt(2000000)-1000000, 255, r.nextInt(2000000)-1000000);
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
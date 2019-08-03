package me.delta2force.discordmc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.delta2force.discordmc.listener.EventListener;
import me.delta2force.discordmc.utils.DiscordMCUtils;
import net.md_5.bungee.api.ChatColor;

public class DiscordMCPlugin extends JavaPlugin{
	private DiscordMCUtils utils;
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
		saveDefaultConfig();
		reloadConfig();
		utils = new DiscordMCUtils(this);
	}
	
	@Override
	public void onDisable() {
		utils.getClient().shutdownNow();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!p.isOp()) {
				p.sendMessage(ChatColor.RED  + "This can only be executed as an OP!");
			}else {
				if(command.getName().equals("discord")) {
					utils.addPlayer(p);
				}
			}
		}else {
			sender.sendMessage(ChatColor.RED + "This can only be executed as a player!");
		}
		return true;
	}
	
	public DiscordMCUtils getUtils() {
		return utils;
	}
}

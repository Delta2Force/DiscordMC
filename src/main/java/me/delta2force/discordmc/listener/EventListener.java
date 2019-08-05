package me.delta2force.discordmc.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.delta2force.discordmc.DiscordMCPlugin;
import me.delta2force.discordmc.chat.Chat;
import me.delta2force.discordmc.chat.Chat.ChatEntry;
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
		for(IntVector iv : discordMC.getUtils().interactions.keySet()) {
			if(iv.equals(new IntVector(event.getClickedBlock().getLocation()))){
				Interaction i = discordMC.getUtils().interactions.get(iv);
				discordMC.getUtils().executeInteraction(i, event.getPlayer());
			}
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		if(discordMC.getUtils().chats.containsKey(p.getUniqueId())) {
			Chat c = discordMC.getUtils().chats.get(p.getUniqueId());
			if(event.getMessage().toLowerCase().startsWith("discordmc: ")) {
				String channelName = event.getMessage().toLowerCase().split("discordmc: ")[1];
				discordMC.getUtils().chats.get(p.getUniqueId()).changeChannel(discordMC.getUtils().getClient().getGuildById(c.serverId).getTextChannelsByName(channelName, true).get(0).getId());
			}
			discordMC.getUtils().getClient().getTextChannelById(c.channelId).sendMessage(event.getMessage());
			c.addEntry(new ChatEntry(event.getMessage(), discordMC.getUtils().getClient().getSelfUser().getId(), false));
		}
	}
}

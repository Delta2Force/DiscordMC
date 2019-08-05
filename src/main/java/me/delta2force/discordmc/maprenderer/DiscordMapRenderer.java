package me.delta2force.discordmc.maprenderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import me.delta2force.discordmc.DiscordMCPlugin;

public class DiscordMapRenderer extends MapRenderer{
	private BufferedImage img;
	
	public DiscordMapRenderer(String url, DiscordMCPlugin plugin) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				Bukkit.getLogger().info("Downloading image from this url: " + url);
				try {
					img = ImageIO.read(new URL(url));
				} catch (MalformedURLException e) {
					Bukkit.getLogger().info("Error while downloading " + url + " (Malformed URL)");
					e.printStackTrace();
				} catch (IOException e) {
					Bukkit.getLogger().info("Error while downloading " + url + " (Input/Output Error (disk full?))");
					e.printStackTrace();
				}
				Bukkit.getLogger().info("Resizing image from " + url);
				BufferedImage nimg = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
				nimg.createGraphics().drawImage(img, 0, 0, 128, 128, null);
				img=nimg;
				Bukkit.getLogger().info("Done!");
			}
		});
	}

	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		if(img != null) {
			canvas.drawImage(0, 0, img);
		}
	}
}

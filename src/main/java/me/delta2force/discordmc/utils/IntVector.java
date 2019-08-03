package me.delta2force.discordmc.utils;

import org.bukkit.Location;

public class IntVector {
	public final int x;
	public final int y;
	public final int z;
	
	public IntVector(int x, int y, int z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public IntVector(Location loc) {
		this.x = loc.getBlockX();
		this.y = loc.getBlockY();
		this.z = loc.getBlockZ();
	}
}

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
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IntVector) {
			IntVector iv = (IntVector) obj;
			if(iv.x == x && iv.y == y && iv.z == z) {
				return true;
			}
		}
		if(obj instanceof Location) {
			Location iv = (Location) obj;
			if(iv.getBlockX() == x && iv.getBlockY() == y && iv.getBlockZ() == z) {
				return true;
			}
		}
		return false;
	}
}

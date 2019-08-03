package me.delta2force.discordmc.utils;

public class Interaction {
	public final InteractiveEnum interactionType;
	public final String value;
	
	public Interaction(InteractiveEnum interactionType, String value) {
		this.interactionType = interactionType;
		this.value = value;
	}
	
	public InteractiveEnum getInteractionType() {
		return interactionType;
	}
	
	public String getValue() {
		return value;
	}
	
	public static enum InteractiveEnum{
		JOIN_SERVER, CHANGE_CHANNEL
	}
}

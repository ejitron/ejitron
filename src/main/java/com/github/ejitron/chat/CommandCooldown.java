package com.github.ejitron.chat;

public class CommandCooldown {
	
	private String channel;
	private String command;
	private int cooldown;
	
	public CommandCooldown(String channel, String command, int cooldown) {
		this.channel = channel;
		this.command = command;
		this.cooldown = cooldown;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public String getCommand() {
		return command;
	}
	
	public int getCooldown() {
		return cooldown;
	}
	
}

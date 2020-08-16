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
	
	/**
	 * Retrieves the channel
	 * @return {@link java.lang.String String} object with the channel name
	 */
	public String getChannel() {
		return channel;
	}
	
	/**
	 * Retrieves the command
	 * @return {@link java.lang.String String} object with the command
	 */
	public String getCommand() {
		return command;
	}
	
	/**
	 * Retrieves the current set cooldown
	 * @return {@link java.lang.Integer Integer} object with the current cooldown
	 */
	public int getCooldown() {
		return cooldown;
	}
	
}

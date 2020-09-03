package com.github.ejitron.chat;

/**
 * A custom command that contains:<br>
 * <ul>
 * <li>Which channel it belongs to</li>
 * <li>What the command is</li>
 * <li>What reply the command gives</li>
 * </ul>
 * @see CommandCooldown
 */
public class CustomCommand {
	private final String channel;
	private final String command;
	private final String reply;
	
	public CustomCommand(String channel, String command, String reply) {
		this.channel = channel;
		this.command = command;
		this.reply = reply;
	}
	
	/**
	 * Retrieves the channel this command belongs to.
	 * @return a {@link java.lang.String String} channel name
	 */
	public String getChannel() {
		return channel;
	}
	
	/**
	 * Retrieves the command name.
	 * @return a {@link java.lang.String String} command
	 */
	public String getCommand() {
		return command;
	}
	
	/**
	 * Retrieves the command reply.
	 * @return a {@link java.lang.String String} reply
	 */
	public String getReply() {
		return reply;
	}
}

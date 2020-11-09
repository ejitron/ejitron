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
	private int count;
	
	public CustomCommand(String channel, String command, String reply, int count) {
		this.channel = channel;
		this.command = command;
		this.reply = reply;
		this.count = count;
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

	/**
	 * Retrieves the number of times command has been used
	 * @return a {@link java.lang.Integer Integer} reply
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Sets the count of the command.
	 * @param newCount a {@link java.lang.Integer Integer} count
	 */
	public void setCount(int newCount) {
		count = newCount;
	}
}

package com.github.ejitron.chat.user;

public class WatchTime {
	
	private final String channel;
	private final String user;
	private final int minutes;
	
	public WatchTime(String channel, String user, int minutes) {
		this.channel = channel;
		this.user = user;
		this.minutes = minutes;
	}
	
	/**
	 * Retrieves the channel name that the time was recorded in
	 * @return a {@link java.lang.String String} channel name
	 */
	public String getChannel() {
		return channel;
	}
	
	/**
	 * Retrieves the user that's been recorded
	 * @return a {@link java.lang.String String} user name
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * Retrieves the total amount of minutes the user has watched for
	 * @return the total amount of minutes as a {@link java.lang.Integer Integer}
	 */
	public int getMinutes() {
		return minutes;
	}
	
}

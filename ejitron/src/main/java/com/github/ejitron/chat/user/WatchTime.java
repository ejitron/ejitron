package com.github.ejitron.chat.user;
/*
 * TODO Document WatchTime
 */
public class WatchTime {
	
	private final String channel;
	private final String user;
	private final int minutes;
	
	public WatchTime(String channel, String user, int minutes) {
		this.channel = channel;
		this.user = user;
		this.minutes = minutes;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public String getUser() {
		return user;
	}
	
	public int getMinutes() {
		return minutes;
	}
	
}

package com.github.ejitron.chat;

import java.util.Map;

import com.github.ejitron.Bot;

public class Chat {
	
	/**
	 * Sends a chat message in the specified {@code channel}
	 * @param channel a {@link java.lang.String String} channel name
	 * @param message a {@link java.lang.String String} message
	 */
	public void sendMessage(String channel, String message) {
		Bot.twitchClient.getChat().sendMessage(channel, message);
	}
	
	/**
	 * Checks if the user with the specified tags has moderator-rights.
	 * @param tags a {@link java.util.Map Map} object containing message tags
	 * @return {@code true} if yes
	 */
	public boolean isModerator(Map<String, String> tags) {
		return (tags.containsKey("badges") && tags.get("badges") != null) && (tags.get("badges").contains("broadcaster") || tags.get("badges").contains("moderator"));
	}

}

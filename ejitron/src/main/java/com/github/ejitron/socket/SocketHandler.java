package com.github.ejitron.socket;

import com.github.ejitron.Bot;
import com.github.ejitron.chat.Chat;
import com.github.ejitron.sql.channels.Channel;
import com.github.twitch4j.chat.TwitchChat;

import java.util.Map;

public class SocketHandler {
	
	public void onMessageReceived(String message) {
		String[] args = message.split("\\s+");
		
		if(args.length <= 1) // We need at least 2 args to actually do anything here
			return;
		
		String setting = args[0];
		String channel = args[1];

		// Someone pressed "Join chat" in the dashboard
		if(setting.equalsIgnoreCase("join")) {
			Channel channels = new Channel();
			Chat chat = new Chat();
			Map<String, Integer> registeredChannels = channels.getAddedChannels();
			TwitchChat chatClient = Bot.twitchClient.getChat();

			if(!registeredChannels.containsKey(channel.toLowerCase())) // Make sure that channel is registered
				return;

			if(chatClient.isChannelJoined(channel)) // Ignore if we're already in chat
				return;

			chatClient.joinChannel(channel);

			if(registeredChannels.get(channel) == 1) { // Let's check if this channel is completely new
				chat.sendMessage(channel, "Hello, " + channel + "! I'm super excited to be here. "
						+ "First things first; in order for me to function properly I need moderator permissions. "
						+ "You can grant me this by typing /mod ejitron "
						+ "If you want me to check the status of the setup, type !eji check");
			} else {
				chat.sendMessage(channel, "/me just entered the building");
			}
		}
	}
	
}

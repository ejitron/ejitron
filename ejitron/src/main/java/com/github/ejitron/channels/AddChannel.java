package com.github.ejitron.channels;

import java.util.List;
import java.util.Map;

import com.github.ejitron.Bot;
import com.github.ejitron.chat.Chat;
import com.github.ejitron.sql.channels.Channel;
import com.github.twitch4j.chat.TwitchChat;

public class AddChannel {
	
	public AddChannel() {
		Channel channels = new Channel();
		TwitchChat twitchChatClient = Bot.twitchClient.getChat();
		List<String> currentChannels = Bot.twitchClient.getChat().getCurrentChannels(); // The channels we're in now
		Map<String, Integer> savedChannels = channels.getAddedChannels(); // List of added channels
		
		savedChannels.forEach((channel, newStatus) -> {
			if(newStatus == 1) { // If we're not in a registered channel
				joinChannel(channel);

				Chat chat = new Chat();
				chat.sendMessage(channel, "Hello, " + channel + "! I'm super excited to be here. "
						+ "First things first; in order for me to function properly I need moderator permissions. "
						+ "You can grant me this by typing /mod ejitron "
						+ "And once that's done you can head over to http://ejitron.tv to finish setting me up!");
				
				channels.updateChannelStatus(channel, 0);
			}
		});
		
		currentChannels.forEach(channel -> { // Leave all channels that are removed from the database
			if(savedChannels.get(channel) == null) {
				twitchChatClient.leaveChannel(channel);
			}
		});
	}
	
	/**
	 * Joins a channel and executes the necessary methods, such as refreshing the channel token
	 * @param channel a {@link java.lang.String String} channel name
	 */
	public void joinChannel(String channel) {
		Channel channels = new Channel();
		TwitchChat twitchChatClient = Bot.twitchClient.getChat();
		
		if(!twitchChatClient.isChannelJoined(channel))
			twitchChatClient.joinChannel(channel);
		
		// Refresh the auth token
		channels.refreshChannelOAuth2(channel);
	}
	
}

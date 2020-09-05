package com.github.ejitron.channels;

import java.util.List;

import com.github.ejitron.Bot;
import com.github.ejitron.sql.channels.Channel;

public class AddChannel {
	
	public AddChannel() {
		Channel channels = new Channel();
		List<String> currentChannels = Bot.twitchClient.getChat().getCurrentChannels(); // The channels we're in now
		List<String> savedChannels = channels.getAddedChannels(); // List of added channels
		
		savedChannels.forEach(channel -> {
			if(!currentChannels.contains(channel)) // If we're not in a registered channel
				joinChannel(channel);
		});
	}
	
	/**
	 * Joins a channel and executes the necessary methods, such as refreshing the channel token
	 * @param channel a {@link java.lang.String String} channel name
	 */
	public void joinChannel(String channel) {
		Channel channels = new Channel();
		
		if(!Bot.twitchClient.getChat().isChannelJoined(channel))
			Bot.twitchClient.getChat().joinChannel(channel);
		
		// Refresh the auth token
		channels.refreshChannelOAuth2(channel);
	}
	
}

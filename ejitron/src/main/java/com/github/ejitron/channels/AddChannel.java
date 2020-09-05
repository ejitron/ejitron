package com.github.ejitron.channels;

import java.util.List;

import com.github.ejitron.Bot;
import com.github.ejitron.chat.Chat;
import com.github.ejitron.sql.channels.Channel;

public class AddChannel {
	
	public AddChannel() {
		Channel channels = new Channel();
		List<String> currentChannels = Bot.twitchClient.getChat().getCurrentChannels(); // The channels we're in now
		List<String> savedChannels = channels.getAddedChannels(); // List of added channels
		
		savedChannels.forEach(channel -> {
			if(!currentChannels.contains(channel)) { // If we're not in a registered channel
				joinChannel(channel);

				Chat chat = new Chat();
				chat.sendMessage(channel, "Hello, " + channel + "! I'm super excited to be here. "
						+ "First things first; in order for me to function properly I need moderator permissions. "
						+ "You can grant me this by typing /mod ejitron "
						+ "And once that's done you can head over to ejitron.tv to finish setting me up!");
			}
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

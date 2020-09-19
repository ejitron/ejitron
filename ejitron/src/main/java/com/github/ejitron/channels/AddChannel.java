package com.github.ejitron.channels;

import java.util.List;
import java.util.Map;

import com.github.ejitron.Bot;
import com.github.ejitron.chat.Chat;
import com.github.ejitron.helix.User;
import com.github.ejitron.sql.channels.Channel;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.pubsub.TwitchPubSub;

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
						+ "If you want me to check the status of the setup, type !eji check");
				
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
		User user = new User();
		TwitchChat twitchChatClient = Bot.twitchClient.getChat();
		TwitchPubSub twitchPubSub = Bot.twitchClient.getPubSub();
		
		if(!twitchChatClient.isChannelJoined(channel))
			twitchChatClient.joinChannel(channel);
		
		twitchPubSub.listenForModerationEvents(Bot.chatOauth, "567800258", user.getUserFromChannel(channel).getId());
		
		// Refresh the auth token
		channels.refreshChannelOAuth2(channel);
	}
	
}

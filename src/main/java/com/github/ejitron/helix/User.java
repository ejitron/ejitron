package com.github.ejitron.helix;

import java.util.Arrays;

import com.github.ejitron.Bot;
import com.github.twitch4j.helix.domain.UserList;

public class User {
	
	/**
	 * Retrieves the {@link com.github.twitch4j.helix.domain.User User} of a channel.
	 * @param channel the channel name
	 * @return a {@link com.github.twitch4j.helix.domain.User User} object
	 */
	public com.github.twitch4j.helix.domain.User getUserFromChannel(String channel) {
		UserList usrList = Bot.twitchClient.getHelix().getUsers(Bot.chatOauth.getAccessToken(), null, Arrays.asList(channel)).execute();
		
		return usrList.getUsers().get(0);
	}

}

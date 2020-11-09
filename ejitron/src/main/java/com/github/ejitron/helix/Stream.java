package com.github.ejitron.helix;

import java.util.Arrays;

import com.github.ejitron.Bot;
import com.github.twitch4j.helix.domain.StreamList;

public class Stream {

	/**
	 * Fetches a stream object for the specified channel name
	 * @param channel a channel name
	 * @return {@link com.github.twitch4j.helix.domain.Stream Stream} object
	 */
	public com.github.twitch4j.helix.domain.Stream getStream(String channel) {
		StreamList streamList = Bot.twitchClient.getHelix().getStreams(
				Bot.chatOauth.getAccessToken(), 
				null, 
				null, 
				1,
				null, 
				null, 
				null, 
				Arrays.asList(channel))
				.execute();
		
		return (streamList.getStreams().size() > 0 ? streamList.getStreams().get(0) : null);
	}
}

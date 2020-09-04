package com.github.ejitron.helix;

import java.util.Arrays;

import com.github.ejitron.Bot;
import com.github.twitch4j.helix.domain.StreamList;

public class Stream {
	public com.github.twitch4j.helix.domain.Stream getStream(String channel) {
		StreamList streamList = Bot.twitchClient.getHelix().getStreams(
				Bot.chatOauth.getAccessToken(), 
				null, 
				null, 
				1, 
				null, 
				null, 
				null, 
				null, 
				Arrays.asList(channel))
				.execute();
		
		return streamList.getStreams().get(0);
	}
}

package com.github.ejitron.helix;

import java.util.Arrays;

import com.github.ejitron.Bot;
import com.github.ejitron.sql.channels.Channel;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.helix.domain.CommercialList;
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

	/**
	 * Runs an ad on the specified channel which lasts the specified time
	 * @param channel a channel name
	 * @param time {@link java.lang.Integer int} in seconds the ad will last
	 * @return {@code true if success}
	 */
	public boolean runCommercial(String channel, int time) {
		Channel channels = new Channel();
		User user = new User();
		OAuth2Credential oauth = channels.getChannelOAuth2(channel);

		CommercialList comList = Bot.twitchClient.getHelix().startCommercial(
				oauth.getAccessToken(),
				user.getUserFromChannel(channel).getId(),
				time)
				.execute();

		return comList.getCommercials().get(0).getMessage().isEmpty();
	}
}

package com.github.ejitron.helix;

import com.github.ejitron.Bot;
import com.github.ejitron.sql.Channels;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.helix.domain.CreateClip;
import com.github.twitch4j.helix.domain.CreateClipList;
import com.netflix.hystrix.exception.HystrixRuntimeException;

public class Clip {
	
	/**
	 * Creates a clip at the exact moment the method is called
	 * @param channel the channel to clip from
	 * @return a {@link com.github.twitch4j.helix.domain.CreateClip CreateClip} object with the clip data
	 * @throws HystrixRuntimeException Channel is not live.
	 */
	public CreateClip createClip(String channel) throws HystrixRuntimeException {
		Channels channels = new Channels();
		User user = new User();
		OAuth2Credential oauth = new OAuth2Credential(channel, channels.getChannelAccessToken(channel));
		
		CreateClipList clipData = Bot.twitchClient.getHelix().createClip(oauth.getAccessToken(), user.getUserFromChannel(channel).getId(), false).execute();
		
		return clipData.getData().get(0);
	}
	
}

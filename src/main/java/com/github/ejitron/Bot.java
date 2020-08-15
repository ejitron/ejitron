package com.github.ejitron;

import com.github.ejitron.sql.Channels;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

public class Bot {
	
	public static TwitchClient twitchClient;
	
	public static OAuth2Credential chatOauth = new OAuth2Credential("ejitron", Credentials.BOT_OAUTH.getValue());
	
	/*
	 * Constructor
	 */
	public Bot() {
		twitchClient = TwitchClientBuilder.builder()
				.withEnableHelix(true)
				.withEnableKraken(true)
				.withEnableTMI(true)
				.withEnableChat(true)
				.withChatAccount(chatOauth)
				.build();
	}
	
	public void start() {
		Channels channels = new Channels();
		
		channels.getAddedChannels().forEach(channel -> {
			if(!twitchClient.getChat().isChannelJoined(channel))
				twitchClient.getChat().joinChannel(channel);
		});
	}
	
}

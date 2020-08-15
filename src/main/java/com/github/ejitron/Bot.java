package com.github.ejitron;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

public class Bot {
	
	public static TwitchClient twitchClient;
	
	public static OAuth2Credential chatOauth = new OAuth2Credential("ejitron", "");
	
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
		/*
		 * TODO
		 * Join all channels registered
		 */
	}
	
}

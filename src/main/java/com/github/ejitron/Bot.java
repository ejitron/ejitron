package com.github.ejitron;

import java.util.List;

import com.github.ejitron.events.chat.DefaultModCommands;
import com.github.ejitron.events.chat.DefaultUserCommands;
import com.github.ejitron.sql.channels.Channels;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

public class Bot {
	
	private static EventManager eventManager = new EventManager();
	
	public static TwitchClient twitchClient;
	
	public static OAuth2Credential chatOauth = new OAuth2Credential("ejitron", Credentials.BOT_OAUTH.getValue());
	
	/*
	 * Constructor
	 */
	public Bot() {
		eventManager.registerEventHandler(new SimpleEventHandler());
		
		twitchClient = TwitchClientBuilder.builder()
				.withEventManager(eventManager)
				.withEnableHelix(true)
				.withEnableKraken(true)
				.withEnableTMI(true)
				.withEnableChat(true)
				.withChatAccount(chatOauth)
				.build();
	}
	
	public void loadConfiguration() {
		// Events
		eventManager.getEventHandler(SimpleEventHandler.class).registerListener(new DefaultModCommands());
		eventManager.getEventHandler(SimpleEventHandler.class).registerListener(new DefaultUserCommands());
	}
	
	public void start() {
		loadConfiguration();
		
		Channels channels = new Channels();
		List<String> joinedChannels = channels.getAddedChannels();
		
		joinedChannels.forEach(channel -> {
			if(!twitchClient.getChat().isChannelJoined(channel))
				twitchClient.getChat().joinChannel(channel);
		});
	}
	
}

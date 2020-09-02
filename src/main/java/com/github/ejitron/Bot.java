package com.github.ejitron;

import java.util.List;

import com.github.ejitron.chat.CommandTimer;
import com.github.ejitron.chat.events.DefaultModCommand;
import com.github.ejitron.chat.events.DefaultUserCommand;
import com.github.ejitron.sql.channels.Channel;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

public class Bot {
	
	private static EventManager eventManager = new EventManager();
	
	public static TwitchClient twitchClient;
	
	public static OAuth2Credential chatOauth = new OAuth2Credential("ejitron", Credential.BOT_OAUTH.getValue());
	
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
		eventManager.getEventHandler(SimpleEventHandler.class).registerListener(new DefaultModCommand());
		eventManager.getEventHandler(SimpleEventHandler.class).registerListener(new DefaultUserCommand());
	}
	
	public void start() {
		loadConfiguration();
		
		// Start the timer for command cooldowns
		CommandTimer.startCooldown();
		
		Channel channels = new Channel();
		List<String> joinedChannels = channels.getAddedChannels();
		
		joinedChannels.forEach(channel -> {
			if(!twitchClient.getChat().isChannelJoined(channel))
				twitchClient.getChat().joinChannel(channel);
			
			// Refresh the auth token
			channels.refreshChannelOAuth2(channel);
		});
	}
	
}

package com.github.ejitron;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.github.ejitron.chat.CommandTimer;
import com.github.ejitron.chat.CustomCommand;
import com.github.ejitron.chat.events.CustomCommandEvent;
import com.github.ejitron.chat.events.DefaultModCommandEvent;
import com.github.ejitron.chat.events.DefaultUserCommandEvent;
import com.github.ejitron.chat.user.WatchTime;
import com.github.ejitron.oauth.Credential;
import com.github.ejitron.sql.channels.Channel;
import com.github.ejitron.sql.commands.Command;
import com.github.ejitron.threads.Hour;
import com.github.ejitron.threads.Minute;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

public class Bot {
	
	private static EventManager eventManager = new EventManager();
	
	public static TwitchClient twitchClient;
	
	public static OAuth2Credential chatOauth = new OAuth2Credential("twitch", Credential.BOT_OAUTH.getValue());
	
	public static List<CustomCommand> customCommandsList = new ArrayList<CustomCommand>();
	public static List<WatchTime> watchTimeList = new ArrayList<WatchTime>();
	
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
		eventManager.getEventHandler(SimpleEventHandler.class).registerListener(new DefaultModCommandEvent());
		eventManager.getEventHandler(SimpleEventHandler.class).registerListener(new DefaultUserCommandEvent());
		eventManager.getEventHandler(SimpleEventHandler.class).registerListener(new CustomCommandEvent());
		
		Command command = new Command();
		command.getCustomCommands().forEach(customCommand -> {
			customCommandsList.add(customCommand);
		});
	}
	
	public void start() {
		loadConfiguration();
		
		// Start the timer for command cooldowns
		CommandTimer.startCooldown();
		
		Channel channels = new Channel();
		List<String> joinedChannels = channels.getAddedChannels();
		
		// Loop through all registered channels and join
		joinedChannels.forEach(channel -> {
			if(!twitchClient.getChat().isChannelJoined(channel))
				twitchClient.getChat().joinChannel(channel);
			
			// Refresh the auth token
			channels.refreshChannelOAuth2(channel);
		});
		
		// Make sure we keep updating the channel OAuth tokens
		Timer timer = new Timer();
		// Timer thread for each hour
		timer.scheduleAtFixedRate(new Hour(), 60*60*1000, 60*60*1000);
		// Timer thread for each minute
		timer.scheduleAtFixedRate(new Minute(), 60*1000, 60*1000);
		
		// Update the local commands list
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				Command command = new Command();
				customCommandsList = command.getCustomCommands();
			}
		}, 15*1000, 15*1000);
		
	}
	
}

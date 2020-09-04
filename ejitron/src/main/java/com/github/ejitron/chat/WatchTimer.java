package com.github.ejitron.chat;

import java.util.List;
import java.util.TimerTask;

import com.github.ejitron.Bot;
import com.github.ejitron.helix.Stream;
import com.github.ejitron.sql.channels.Channel;
import com.github.ejitron.sql.channels.Setting;
import com.github.twitch4j.tmi.domain.Chatters;

public class WatchTimer extends TimerTask {

	@Override
	public void run() {
		Stream stream = new Stream();
		Setting setting = new Setting();
		Channel channel = new Channel();
		List<String> joinedChannels = channel.getAddedChannels();
		
		joinedChannels.forEach(ch -> {
			if(setting.getChannelSetting(ch, "watchtime") == 1) { // Make sure the watchtime setting is enabled
				if(stream.getStream(ch) == null) // If the stream is offline, don't count!
					return;
				
				Chatters chatList = Bot.twitchClient.getMessagingInterface().getChatters(ch).execute();
				
				chatList.getAllViewers().forEach(viewer -> {
					/*
					 * TODO
					 * Remove known lurkers
					 * Add to local list
					 * Save to DB(?)
					 */
					
				});
			}
		});
	}

}

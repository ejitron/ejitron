package com.github.ejitron.chat.user;

import java.util.List;
import java.util.TimerTask;

import com.github.ejitron.Bot;
import com.github.ejitron.helix.Stream;
import com.github.ejitron.sql.channels.Channel;
import com.github.ejitron.sql.channels.Setting;
import com.github.ejitron.sql.user.WatchTimeListing;
import com.github.twitch4j.tmi.domain.Chatters;

public class WatchTimer extends TimerTask {

	@Override
	public void run() {
		Stream stream = new Stream();
		Setting setting = new Setting();
		Channel channel = new Channel();
		List<String> joinedChannels = channel.getAddedChannels();
		List<WatchTime> watchList = Bot.watchTimeList;
		
		joinedChannels.forEach(ch -> {
			if(stream.getStream(ch) == null) // If the stream is offline, don't count!
				return;
			
			if(setting.getChannelSetting(ch, "watchtime") == 1) { // Make sure the watchtime setting is enabled
				Chatters chatList = Bot.twitchClient.getMessagingInterface().getChatters(ch).execute();
				
				chatList.getAllViewers().forEach(viewer -> {
					/*
					 * TODO Remove known lurkers
					 */
					if(viewer.equalsIgnoreCase(ch))
						return;
					
					for(int i = 0; i < watchList.size(); i++) {
						WatchTime current = watchList.get(i);
						
						if(current.getUser().equalsIgnoreCase(viewer))
							watchList.set(i, new WatchTime(ch, viewer, current.getMinutes() + 1)); // increment the watched minutes
						else
							watchList.add(new WatchTime(ch, viewer, 1)); // Create a new entry for viewer
					}
				});
			}
		});
		
		Bot.watchTimeList = watchList; // Update the watch list to our modified one
		
		WatchTimeListing watchTimeListing = new WatchTimeListing();
		List<WatchTime> savedWatchTime = watchTimeListing.getSavedWatchTime();
		
		watchList.forEach(c -> {
			savedWatchTime.forEach(s -> {
				if(s.getUser().equalsIgnoreCase(c.getUser())) {
					if(s.getMinutes() != c.getMinutes()) // If stored watchtime is different, save the current one!
						watchTimeListing.setWatchTime(c.getChannel(), c.getUser(), c.getMinutes());
				} else {
					watchTimeListing.addWatchTime(c.getChannel(), c.getUser(), c.getMinutes()); // If not stored, create a new entry
				}
			});
		});
	}

}

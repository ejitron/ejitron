package com.github.ejitron.chat.user;

import java.util.List;

import com.github.ejitron.Bot;
import com.github.ejitron.helix.Stream;
import com.github.ejitron.sql.channels.Channel;
import com.github.ejitron.sql.channels.Setting;
import com.github.ejitron.sql.user.WatchTimeListing;
import com.github.twitch4j.tmi.domain.Chatters;

public class WatchTimer {

	public WatchTimer() {
		Stream stream = new Stream();
		Setting setting = new Setting();
		Channel channel = new Channel();
		WatchTimeListing watchTimeListing = new WatchTimeListing();
		List<String> joinedChannels = channel.getAddedChannels();
		List<WatchTime> watchList = Bot.watchTimeList;
		
		joinedChannels.forEach(ch -> {
			if(stream.getStream(ch) == null) // If the stream is offline, don't count!
				return;
			
			if(setting.getChannelSetting(ch, "watchtime") == 1) { // Make sure the watchtime setting is enabled
				Chatters chatList = Bot.twitchClient.getMessagingInterface().getChatters(ch).execute();
				List<String> allViewers = chatList.getAllViewers();
				
				if(allViewers.size() < 1) // Skip if there's nobody watching
					return;
				
				allViewers.forEach(viewer -> {
					if(watchTimeListing.getKnownLurkers().contains(viewer) || viewer.equalsIgnoreCase(ch)) // Don't record known lurkers, or the channel broadcaster
						return;
					
					boolean userDone = false; // Used to check if a viewer has been processed
						
					for(int i = 0; i < watchList.size(); i++) { // Check if the viewer is in the local list already
						WatchTime current = watchList.get(i);
						
						if(current.getChannel().equalsIgnoreCase(ch) && current.getUser().equalsIgnoreCase(viewer)) {
							watchList.set(i, new WatchTime(ch, viewer, current.getMinutes() + 1)); // increment the watched minutes
							userDone = true;
							break;
						}
					}
					
					if(!userDone) // User is not in the local list yet
						watchList.add(new WatchTime(ch, viewer, 1)); // Create a new entry for viewer
					
					return;
				});
			}
		});
		
		Bot.watchTimeList = watchList; // Update the watch list to our modified one
		
		List<WatchTime> savedWatchTime = watchTimeListing.getSavedWatchTime();
		
		watchList.forEach(cl -> {
			boolean userDone = false; // Used to check if user has been processed yet
			
			for(int i = 0; i < savedWatchTime.size(); i++) {
				WatchTime current = savedWatchTime.get(i);
				
				if(current.getChannel().equalsIgnoreCase(cl.getChannel()) && current.getUser().equalsIgnoreCase(cl.getUser()) && current.getMinutes() != cl.getMinutes()) { // If stored watchtime is different, save the current one!
					watchTimeListing.setWatchTime(cl.getChannel(), cl.getUser(), cl.getMinutes());
					userDone = true;
					return;
				}
			}
			
			if(!userDone)
				watchTimeListing.addWatchTime(cl.getChannel(), cl.getUser(), cl.getMinutes()); // If not stored, create a new entry
			
			return;
		});
	}

}

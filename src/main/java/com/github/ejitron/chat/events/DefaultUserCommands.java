package com.github.ejitron.chat.events;

import com.github.ejitron.chat.Chat;
import com.github.ejitron.chat.CommandTimer;
import com.github.ejitron.helix.Clip;
import com.github.ejitron.helix.User;
import com.github.ejitron.sql.channels.Setting;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;
import com.github.twitch4j.helix.domain.CreateClip;
import com.netflix.hystrix.exception.HystrixRuntimeException;

public class DefaultUserCommands {
	
	@EventSubscriber
	public void onChat(IRCMessageEvent e) {
		if(!e.getMessage().isPresent() || e.getUser() == null) // Return since this is most likely back-end messages sent in chat
			return;
		
		String[] args = e.getMessage().get().split("\\s+");
		String author = e.getTags().get("display-name");
		String channel = e.getChannel().getName();
		
		Chat chat = new Chat();
		Setting settings = new Setting();
		
		// The command is in a cooldown
		if(CommandTimer.isInCooldown(channel, args[0]))
			return;
		
		/*
		 * !clip
		 * Creates a clip and sends to chat
		 */
		if(args[0].equalsIgnoreCase("!clip") && settings.getChannelSetting(channel, "clip_cmd") == 1) {
			Clip clip = new Clip();
			CommandTimer.addToCooldown(channel, args[0]);
			
			try {
				CreateClip createdClip = clip.createClip(channel);
				chat.sendMessage(channel, author + " just clipped this: https://clips.twitch.tv/" + createdClip.getId());
			} catch (HystrixRuntimeException ex) {
				chat.sendMessage(channel, channel + " is not live.");
			}
			
			return;
		}
		
		/*
		 * !followage
		 * Retrieves the amount of time the user has followed the channel
		 */
		if(args[0].equalsIgnoreCase("!followage") && settings.getChannelSetting(channel, "followage_cmd") == 1) {
			User user = new User();
			String age = user.getFollowAge(author, channel);
			CommandTimer.addToCooldown(channel, args[0]);
			
			if(age == null) {
				chat.sendMessage(channel, author + " is not following " + channel + ".");
				return;
			}
			
			chat.sendMessage(channel, author + " has been following " + channel + " for " + age + ".");
			return;
		}
	}
	
}
package com.github.ejitron.chat.events;

import com.github.ejitron.chat.Chat;
import com.github.ejitron.helix.User;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.pubsub.domain.ChatModerationAction.ModerationAction;
import com.github.twitch4j.pubsub.events.ChatModerationEvent;

public class ModerationEvent {
	
	@EventSubscriber
	public void onModChange(ChatModerationEvent e) {
		if(!"ejitron".equalsIgnoreCase(e.getData().getTargetUserLogin())) // If it doesn't have to do with the bot, skip it
			return;
		
		User user = new User();
		Chat chat = new Chat();
		
		String channel = user.getUserFromId(e.getChannelId()).getDisplayName();
		
		if(e.getData().getModerationAction().equals(ModerationAction.MOD)) { // We got MOD permissions!
			chat.sendMessage(channel, "I'm modded! Now you can continue setting me up over at ejitron.tv");
		}
		
		else if(e.getData().getModerationAction().equals(ModerationAction.UNMOD)) { // Un-modded the bot
			chat.sendMessage(channel, "I'm un-modded! This means that I will not work properly.. Make sure to mod me again to ensure that I will function properly!");
		}
		
		return;
	}
	
}

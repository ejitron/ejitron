package com.github.ejitron.chat.events;

import com.github.ejitron.chat.Automessage;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;

public class UserChatEvent {
	@EventSubscriber
	public void onChat(IRCMessageEvent e) {
		if(!e.getMessage().isPresent() || e.getUser() == null) // Return since this is most likely back-end messages sent in chat
			return;

		if(e.getUser().getName().equalsIgnoreCase("ejitron")) // We don't want ourselves to count..
			return;

		if(Automessage.chatCount.containsKey(e.getChannel().getName()))
			Automessage.chatCount.replace(e.getChannel().getName(), Automessage.chatCount.get(e.getChannel().getName()) + 1);
		else
			Automessage.chatCount.put(e.getChannel().getName().toLowerCase(), 1);
	}
}

package com.github.ejitron.chat.events;

import com.github.ejitron.chat.Chat;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.GiftSubscriptionsEvent;
import com.github.twitch4j.chat.events.channel.SubscriptionEvent;

public class SubscribeEvent {
	
	// Someone subscribes
	@EventSubscriber
	public void onSubscribe(SubscriptionEvent e) {
		Chat chat = new Chat();
		
		String user = e.getUser().getName();
		int months = e.getMonths();
		int streak = e.getSubStreak();
		
		if(e.getGifted()) // Stop if it's gifted. We have another method for that.
			return;
		
		/*
		 * TODO
		 * Send customized message to channel chat.
		 */
		
		if(months <= 1) {
			// First timer
			chat.sendMessage(e.getChannel().getName(), "");
		} else {
			// Streak
			chat.sendMessage(e.getChannel().getName(), "");
		}
	}
	
	// Someone gifts subscribtions
	@EventSubscriber
	public void onGiftedSubscription(GiftSubscriptionsEvent e) {
		Chat chat = new Chat();
		
		String user = e.getUser().getName();
		int count = e.getCount();
		int totalGifted = e.getTotalCount();
		
		/*
		 * TODO
		 * Send customized message to channel chat.
		 */
		chat.sendMessage(e.getChannel().getName(), "");
	}
}

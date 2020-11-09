package com.github.ejitron.chat.events;

import com.github.ejitron.chat.Chat;
import com.github.ejitron.sql.channels.Setting;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.GiftSubscriptionsEvent;
import com.github.twitch4j.chat.events.channel.SubscriptionEvent;

public class SubscribeEvent {
	
	// Someone subscribes
	@EventSubscriber
	public void onSubscribe(SubscriptionEvent e) {
		Chat chat = new Chat();
		Setting setting = new Setting();
		
		String user = e.getUser().getName();
		int months = e.getMonths();
		int streak = e.getSubStreak();
		
		if(e.getGifted()) // Stop if it's gifted. We have another method for that.
			return;
		
		String message;
		
		if(months <= 1) { // First timer
			message = setting.getChannelSettingString(e.getChannel().getName(), "sub_message_first")
					.replace("[user]", user);
		} else { // Streak
			message = setting.getChannelSettingString(e.getChannel().getName(), "sub_message_recurring")
					.replace("[user]", user)
					.replace("[months]", String.valueOf(months))
					.replace("[streak]", String.valueOf(streak));
		}

		chat.sendMessage(e.getChannel().getName(), message);
	}
	
	// Someone gifts subscribtions
	@EventSubscriber
	public void onGiftedSubscription(GiftSubscriptionsEvent e) {
		Chat chat = new Chat();
		Setting setting = new Setting();
		
		String user = e.getUser().getName();
		int count = e.getCount();
		int totalGifted = e.getTotalCount();
		
		String message = setting.getChannelSettingString(e.getChannel().getName(), "sub_message_gifted")
				.replace("[user]", user)
				.replace("[amount]", String.valueOf(count))
				.replace("[total]", String.valueOf(totalGifted));
		
		chat.sendMessage(e.getChannel().getName(), message);
	}
}

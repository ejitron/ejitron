package com.github.ejitron.chat;

import com.github.ejitron.sql.channels.AutomessageList;
import com.github.ejitron.sql.channels.Setting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Automessage {

	public static Map<String, Integer> chatCount = new HashMap<>();
	private Map<String, Integer> pagination = new HashMap<>();
	private Map<String, List<String>> channelMessages = new HashMap<>();

	/**
	 * Fires the automessage checks and sends out a message if ready
	 */
	public void runAutomessage() {
		Setting setting = new Setting();
		Chat chat = new Chat();

		channelMessages.forEach((ch, list) -> {
			if(setting.getChannelSetting(ch, "automessage_count") > chatCount.get(ch)) // Skip if the chat count is not reached yet
				return;

			chat.sendMessage(ch, list.get(pagination.get(ch))); // Send our message

			if(list.size() <= (pagination.get(ch) + 1)) // If pagination is at the last item, reset!
				pagination.replace(ch, 0);
			else
				pagination.replace(ch, pagination.get(ch) + 1);

			chatCount.replace(ch, 0); // Reset the chat count
		});
	}

	/**
	 * Injects the saved automessages into the local list
	 */
	public void injectAutomessageList() {
		AutomessageList automessageList = new AutomessageList();

		automessageList.getChannelsWithAutomessage().forEach(ch -> {
			chatCount.put(ch, 0);
			channelMessages.put(ch, automessageList.getChannelAutomessages(ch));
			pagination.put(ch, 0);
		});
	}

	/**
	 * Updates the local list of a channels automessage.
	 * @param channel a channel name
	 */
	public static void updateAutomessageList(String channel) {
		AutomessageList automessageList = new AutomessageList();
		Automessage automessage = new Automessage();

		if(automessage.channelMessages.containsKey(channel))
			automessage.channelMessages.replace(channel, automessageList.getChannelAutomessages(channel));
		else {
			automessage.channelMessages.put(channel, automessageList.getChannelAutomessages(channel));
			automessage.pagination.put(channel, 0);
		}
	}

}

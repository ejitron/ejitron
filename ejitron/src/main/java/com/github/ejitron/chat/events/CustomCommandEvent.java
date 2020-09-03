package com.github.ejitron.chat.events;

import com.github.ejitron.Bot;
import com.github.ejitron.chat.Chat;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;

public class CustomCommandEvent {
	
	@EventSubscriber
	public void onCustomCommand(IRCMessageEvent e) {
		if(!e.getMessage().isPresent() || e.getUser() == null) // Return since this is most likely back-end messages sent in chat
			return;
		
		String[] args = e.getMessage().get().split("\\s+");
		String user = e.getTags().get("display-name");
		String channel = e.getChannel().getName();
		
		Chat chat = new Chat();
		
		Bot.customCommandsList.forEach(customCommand -> { // Loop through entire commands list to see if our command is in here!
			if(args[0].equalsIgnoreCase(customCommand.getCommand()) && channel.equalsIgnoreCase(customCommand.getChannel())) {
				// Format the reply
				String reply = customCommand.getReply()
						.replace("[user]", user)
						.replace("[@user]", "@" + user);
				
				chat.sendMessage(channel, reply);
				return;
			}
		});
		
		if(!chat.isModerator(e.getTags())) // Don't continue this unless the user is a moderator or broadcaster
			return;
		
		if(!args[0].equalsIgnoreCase("!cmd")) // Only listen to !cmd from this point on
			return;
		
		if(args.length == 1) { // Basic channel-command list
			chat.sendMessage(channel, "@" + user + " All commands available in this channel are available at: https://ejitron.tv/c/" + channel);
			return;
		}
		
		if(args.length < 3) // We need at least 3 total arguments to setup commands
			return;
		
		String setting = args[1];
		
		if(setting.equalsIgnoreCase("add")) {
			// For this we need 4 arguments:
			// 1. The command
			// 2. The setting
			// 3. Command name
			// 4. Reply
			if(args.length < 4) {
				chat.sendMessage(channel, "@" + user + " not enough arguments!");
			}
			
			String name = args[2];
			String reply = "";
			for(int i = 3; i < args.length; i++) { // Build the reply
				if(i == args.length-1) { // Don't include a space in the last word!
					reply += args[i];
				}
				
				reply += args[i] + " ";
			}
			
			/*
			 * TODO
			 * Save the command
			 */
			
			chat.sendMessage(channel, "@" + user + " Saved the new command " + name + ": " + reply);
			return;
		}
	}
	
}
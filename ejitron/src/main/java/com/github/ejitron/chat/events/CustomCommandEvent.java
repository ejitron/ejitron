package com.github.ejitron.chat.events;

import com.github.ejitron.Bot;
import com.github.ejitron.chat.Chat;
import com.github.ejitron.sql.commands.Command;
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
		
		if(!chat.isModerator(e.getTags()) // Don't continue this unless the user is a moderator or broadcaster
				&& !args[0].equalsIgnoreCase("!cmd")) // Only listen to !cmd from this point on
			return;
		
		if(args.length == 1) { // Basic channel-command list
			chat.sendMessage(channel, "@" + user + " All commands available in this channel are available at: https://ejitron.tv/c/" + channel);
			return;
		}
		
		if(args.length < 3) // We need at least 3 total arguments to setup commands
			return;
		
		String setting = args[1];
		
		if("add".equalsIgnoreCase(setting)) {
			// For this we need 4 arguments:
			// 1. The command
			// 2. The setting
			// 3. Command name <--
			// 4. Reply
			if(args.length < 4) {
				chat.sendMessage(channel, "@" + user + " not enough arguments!");
				return;
			}
			
			
			String name = args[2];
			String reply = "";
			for(int i = 3; i < args.length; i++) { // Build the reply
				if(i == args.length-1) { // Don't include a space in the last word!
					reply += args[i];
					break;
				}
				
				reply += args[i] + " ";
			}

			addCommand(channel, user, name, reply);
			return;
		}
		
		else if("edit".equalsIgnoreCase(setting)) {
			// For this we need 4 arguments:
			// 1. The command
			// 2. The setting
			// 3. Command name <--
			// 4. A new reply
			if(args.length < 4) {
				chat.sendMessage(channel, "@" + user + " not enough arguments!");
				return;
			}
			
			String name = args[2];
			String newReply = "";
			for(int i = 3; i < args.length; i++) { // Build the reply
				if(i == args.length-1) { // Don't include a space in the last word!
					newReply += args[i];
					break;
				}
				
				newReply += args[i] + " ";
			}
			
			editCommand(channel, user, name, newReply);
			return;
		}
		
		else if("delete".equalsIgnoreCase(setting)) {
			// For this we need 3 arguments:
			// 1. The command
			// 2. The setting
			// 3. Command name <--

			String name = args[2];
			
			deleteCommand(channel, user, name);
			return;
		}
	}
	
	public void addCommand(String channel, String user, String name, String reply) {
		Command command = new Command();
		Chat chat = new Chat();
		
		// Make sure the command doesn't exist yet!
		if(command.commandExists(channel, name)) {
			chat.sendMessage(channel, "@" + user + " That command already exists.");
			return;
		}
		
		if(command.addCustomCommand(channel, name, reply)) {
			chat.sendMessage(channel, "@" + user + " Saved the new command " + name);
			return;
		}
		
		chat.sendMessage(channel, "@" + user + " Could not save the command " + name + ". Try again later!");
	}
	
	public void editCommand(String channel, String user, String name, String newReply) {
		Command command = new Command();
		Chat chat = new Chat();
		
		// Make sure the command exist!
		if(!command.commandExists(channel, name)) {
			chat.sendMessage(channel, "@" + user + " That command does not exist.");
			return;
		}
		
		if(command.editCustomCommand(channel, name, newReply)) {
			chat.sendMessage(channel, "@" + user + " Saved the command " + name);
			return;
		}
		
		chat.sendMessage(channel, "@" + user + " Could not save the command " + name + ". Try again later!");
	}
	
	public void deleteCommand(String channel, String user, String name) {
		Command command = new Command();
		Chat chat = new Chat();
		
		// Make sure the command exist!
		if(!command.commandExists(channel, name)) {
			chat.sendMessage(channel, "@" + user + " That command does not exist.");
			return;
		}
		
		if(command.deleteCustomCommand(channel, name)) {
			chat.sendMessage(channel, "@" + user + " Successfully deleted the command " + name + ".");
			return;
		}
		
		chat.sendMessage(channel, "@" + user + " Could not delete the command " + name + ". Try again later!");
	}
	
}

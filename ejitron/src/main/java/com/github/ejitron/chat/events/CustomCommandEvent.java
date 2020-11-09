package com.github.ejitron.chat.events;

import com.github.ejitron.Bot;
import com.github.ejitron.chat.Chat;
import com.github.ejitron.chat.CommandTimer;
import com.github.ejitron.chat.CustomCommand;
import com.github.ejitron.helix.User;
import com.github.ejitron.sql.commands.Command;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;

import java.util.Map;

public class CustomCommandEvent {
	
	@EventSubscriber
	public void onCustomCommand(IRCMessageEvent e) {
		if(!e.getMessage().isPresent() || e.getUser() == null) // Return since this is most likely back-end messages sent in chat
			return;
		
		String[] args = e.getMessage().get().split("\\s+");
		String user = e.getTags().get("display-name");
		String channel = e.getChannel().getName();
		
		Chat chat = new Chat();
		
		// The command is in a cooldown
		if(CommandTimer.isInCooldown(channel, args[0]))
			return;
		
		if(checkCustomCommand(chat, args, channel, user, e.getTags()))
			return;
		
		if(!args[0].equalsIgnoreCase("!cmd")) // Only listen to !cmd from this point on
			return;
		
		if(args.length == 1) { // Basic channel-command list
			chat.sendMessage(channel, "@" + user + " All commands available in this channel are available at: https://ejitron.tv/docs/c/" + channel);
			CommandTimer.addToCooldown(channel, args[0]);
			return;
		}
		
		if(!chat.isModerator(e.getTags())) // Don't continue this unless the user is a moderator or broadcaster
			return;

		if(args.length < 3) {
			if(args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("edit")) { // We need at least 3 total arguments to setup commands
				chat.sendMessage(channel, "@" + user + " More information on https://ejitron.tv/docs. Correct usage: !cmd add/edit !mycommand some cool response!");
				return;
			} else if(args[1].equalsIgnoreCase("delete")) {
				chat.sendMessage(channel, "@" + user + " More information on https://ejitron.tv/docs. Correct usage: !cmd delete !mycommand");
				return;
			} else {
				chat.sendMessage(channel, "@" + user + " More information on https://ejitron.tv/docs. Applicable arguments are: add, edit or delete.");
				return;
			}
		}

		if("add".equalsIgnoreCase(args[1])) {
			addCommand(args, channel, user);
			return;
		}
		
		else if("edit".equalsIgnoreCase(args[1])) {
			editCommand(args, channel, user);
			return;
		}
		
		else if("delete".equalsIgnoreCase(args[1])) {
			deleteCommand(args, channel, user);
			return;
		}
	}
	
	private boolean checkCustomCommand(Chat chat, String[] args, String channel, String user, Map<String, String> tags) {
		boolean status = false;
		User ejiUser = new User();
		
		for(CustomCommand customCommand : Bot.customCommandsList) { // Loop through entire commands list to see if our command is in here!
			if(args[0].equalsIgnoreCase(customCommand.getCommand()) && channel.equalsIgnoreCase(customCommand.getChannel())) {
				customCommand.setCount(customCommand.getCount() + 1); // Increment usage

				// Format the reply
				String reply = customCommand.getReply();
				if(reply.contains("[user]"))
					reply = reply.replace("[user]", user);
				if(reply.contains("[@user]"))
					reply = reply.replace("[@user]", "@" + user);
				if(reply.contains("[followage]")) {
					String followage = ejiUser.getFollowAge(user, channel);
					if(followage != null)
						reply = reply.replace("[followage]", ejiUser.getFollowAge(user, channel));
					else
						reply = reply.replace("[followage]", "error");
				}
				if(reply.contains("[channel]"))
					reply = reply.replace("[channel]", channel);
				if(reply.contains("[count]"))
					reply = reply.replace("[count]", String.valueOf(customCommand.getCount()));

				chat.sendMessage(channel, reply);

				if(!chat.isModerator(tags))
					CommandTimer.addToCooldown(channel, customCommand.getCommand());

				status = true;
				Command command = new Command();
				command.updateCommandCount(customCommand.getChannel(), customCommand.getCommand(), customCommand.getCount());
				break;
			}
		}
		
		return status;
	}
	
	private void addCommand(String[] args, String channel, String user) {
		Command command = new Command();
		Chat chat = new Chat();
		
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
		StringBuilder reply = new StringBuilder();
		for(int i = 3; i < args.length; i++) { // Build the reply
			if(i == args.length-1) { // Don't include a space in the last word!
				reply.append(args[i]);
				break;
			}
			
			reply.append(args[i]).append(" ");
		}
		
		// Make sure the command doesn't exist yet!
		if(command.commandExists(channel, name)) {
			chat.sendMessage(channel, "@" + user + " That command already exists.");
			return;
		}
		
		if(command.addCustomCommand(channel, name, reply.toString())) {
			chat.sendMessage(channel, "@" + user + " Saved the new command " + name);
			return;
		}
		
		chat.sendMessage(channel, "@" + user + " Could not save the command " + name + ". Try again later!");
	}
	
	private void editCommand(String[] args, String channel, String user) {
		Command command = new Command();
		Chat chat = new Chat();
		
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
		StringBuilder newReply = new StringBuilder();
		for(int i = 3; i < args.length; i++) { // Build the reply
			if(i == args.length-1) { // Don't include a space in the last word!
				newReply.append(args[i]);
				break;
			}
			
			newReply.append(args[i]).append(" ");
		}
		
		// Make sure the command exist!
		if(!command.commandExists(channel, name)) {
			chat.sendMessage(channel, "@" + user + " That command does not exist.");
			return;
		}

		if(command.editCustomCommand(channel, name, newReply.toString())) {
			chat.sendMessage(channel, "@" + user + " Saved the command " + name);
			return;
		}
		
		chat.sendMessage(channel, "@" + user + " Could not save the command " + name + ". Try again later!");
	}
	
	private void deleteCommand(String[] args, String channel, String user) {
		Command command = new Command();
		Chat chat = new Chat();
		
		// For this we need 3 arguments:
		// 1. The command
		// 2. The setting
		// 3. Command name <--

		String name = args[2];
		
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

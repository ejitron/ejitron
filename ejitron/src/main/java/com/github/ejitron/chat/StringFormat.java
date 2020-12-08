package com.github.ejitron.chat;

import com.github.ejitron.helix.Stream;
import com.github.ejitron.helix.User;
import com.github.ejitron.time.TimeFormat;

public class StringFormat {

	/**
	 * Formats a message to translate the available variables.
	 * @param message a message string to format
	 * @param channel a channel name
	 * @param command specify if this is for a command or not
	 * @param customCommand if {@code command} is true then pass the {@link CustomCommand CustomCommand} object
	 * @param user a user string name
	 * @param args a string array with arguments written by the user
	 * @return a {@link java.lang.String String} object with the formatted string
	 */
	public String formatStringWithVariables(String message, String channel, boolean command, CustomCommand customCommand, String user, String[] args) {
		User helixUser = new User();
		if(command) {
			if (message.contains("[user]"))
				message = message.replace("[user]", user);
			if (message.contains("[@user]"))
				message = message.replace("[@user]", "@" + user);
			if (message.contains("[touser]"))
				message = message.replace("[touser]", (args.length > 1 ? args[1] : user));
			if (message.contains("[@touser]"))
				message = message.replace("[@touser]", (args.length > 1 ? "@" + args[1] : "@" + user));
			if (message.contains("[userid]"))
				message = message.replace("[userid]", helixUser.getUserFromChannel(user).getId());
			if (message.contains("[followage]")) {
				String followage = helixUser.getFollowAge(user, channel);
				if (followage != null)
					message = message.replace("[followage]", helixUser.getFollowAge(user, channel));
				else
					message = message.replace("[followage]", "error");
			}
			if (message.contains("[count]"))
				message = message.replace("[count]", String.valueOf(customCommand.getCount()));
		}
		if(message.contains("[channel]"))
			message = message.replace("[channel]", channel);

		if(message.contains("[uptime]")) {
			Stream stream = new Stream();
			if(stream.getStream(channel) != null)
				message = message.replace("[uptime]", TimeFormat.formatDuration(stream.getStream(channel).getUptime()));
			else
				message = message.replace("[uptime]", "offline");
		}

		if(message.contains("[followers]")) {
			Stream stream = new Stream();
			message = message.replace("[followers]", String.valueOf(stream.getFollowers(channel)));
		}

		if(message.contains("[subcount]")) {
			Stream stream = new Stream();
			message = message.replace("[subcount]", String.valueOf(stream.getSubscribers(channel)));
		}

		return message;
	}
}

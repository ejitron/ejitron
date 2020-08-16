package com.github.ejitron.events.chat;

import com.github.ejitron.helix.ChannelInfo;
import com.github.ejitron.sql.channels.Setting;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;

public class DefaultModCommands {
	
	@EventSubscriber
	public void onModChat(IRCMessageEvent e) {
		if(!e.getMessage().isPresent() || e.getUser() == null) // Return since this is most likely back-end messages sent in chat
			return;
		
		String[] args = e.getMessage().get().split("\\s+");
		String user = e.getTags().get("display-name");
		String channel = e.getChannel().getName();
		
		Chat chat = new Chat();
		Setting settings = new Setting();
		
		if(!chat.isModerator(e.getTags())) // These are mod/broadcaster only commands.
			return;
		
		/*
		 * !title
		 * Gets/Sets the current title
		 */
		if(args[0].equalsIgnoreCase("!title") && settings.getChannelSetting(channel, "title_cmd") == 1) {
			ChannelInfo chanInfo = new ChannelInfo();
			
			if(args.length < 2) {
				String title = chanInfo.getChannelInfo(channel).getTitle();
				
				chat.sendMessage(channel, "Current title set to: " + title);
				return;
			}
			
			String title = "";
			for(int i = 1; i < args.length; i++) { // Build the title string
				if(i == args.length-1)
					title += args[i];
				else
					title += args[i] + " ";
			}
			
			chanInfo.setChannelInfo(channel, null, title);
			chat.sendMessage(channel, user + " Updated title to: " + title);
			return;
		}
		
		/*
		 * !game
		 * Gets/Sets the current game
		 */
		if(args[0].equalsIgnoreCase("!game") && settings.getChannelSetting(channel, "game_cmd") == 1) {
			ChannelInfo chanInfo = new ChannelInfo();
			
			if(args.length < 2) {
				String game = chanInfo.getChannelInfo(channel).getGameName();
				
				chat.sendMessage(channel, channel + " is currently playing: " + game);
				return;
			}
			
			String game = "";
			for(int i = 1; i < args.length; i++) { // Build the game string
				if(i == args.length-1)
					game += args[i];
				else
					game += args[i] + " ";
			}
			
			chanInfo.setChannelInfo(channel, game, null);
			chat.sendMessage(channel, user + " Set the game to: " + game);
			return;
		}
		
	}
	
}

package com.github.ejitron.chat.events;

import com.github.ejitron.chat.Chat;
import com.github.ejitron.chat.CommandTimer;
import com.github.ejitron.helix.ChannelInfo;
import com.github.ejitron.helix.User;
import com.github.ejitron.sql.channels.Setting;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;

public class DefaultModCommandEvent {
	
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
		
		// The command is in a cooldown
		if(CommandTimer.isInCooldown(channel, args[0]))
			return;
		
		/*
		 * !eji
		 * Basic command for bot-specific duties
		 */
		if("!eji".equalsIgnoreCase(args[0])) {
			if(args.length < 2)
				return;
			
			if("check".equalsIgnoreCase(args[1])) {
				User helixUser = new User();

				if(helixUser.isBotModerator(channel))
					chat.sendMessage(channel, "I'm a moderator and we're all good! Remember that you can manage me over on ejitron.tv");
				else
					chat.sendMessage(channel, "It doesn't seem like I'm a moderator in this chat. Type /mod ejitron to fix this!");
			}
			
			return;
		}
		
		/*
		 * !title
		 * Gets/Sets the current title
		 */
		if("!title".equalsIgnoreCase(args[0]) && settings.getChannelSetting(channel, "title_cmd") == 1) {
			ChannelInfo chanInfo = new ChannelInfo();
			CommandTimer.addToCooldown(channel, args[0]);
			
			if(args.length < 2) {
				String title = chanInfo.getChannelInfo(channel).getTitle();
				
				chat.sendMessage(channel, "Current title set to: " + title);
				return;
			}
			
			StringBuilder title = new StringBuilder();
			for(int i = 1; i < args.length; i++) { // Build the title string
				if(i == args.length-1)
					title.append(args[i]);
				else
					title.append(args[i]).append(" ");
			}
			
			chanInfo.setChannelInfo(channel, null, title.toString());
			chat.sendMessage(channel, user + " Updated title to: " + title);
			return;
		}
		
		/*
		 * !game
		 * Gets/Sets the current game
		 */
		if("!game".equalsIgnoreCase(args[0]) && settings.getChannelSetting(channel, "game_cmd") == 1) {
			ChannelInfo chanInfo = new ChannelInfo();
			CommandTimer.addToCooldown(channel, args[0]);
			
			if(args.length < 2) {
				String game = chanInfo.getChannelInfo(channel).getGameName();
				
				chat.sendMessage(channel, channel + " is currently playing: " + game);
				return;
			}
			
			StringBuilder game = new StringBuilder();
			for(int i = 1; i < args.length; i++) { // Build the game string
				if(i == args.length-1)
					game.append(args[i]);
				else
					game.append(args[i]).append(" ");
			}
			
			chanInfo.setChannelInfo(channel, game.toString(), null);
			chat.sendMessage(channel, user + " Set the game to: " + game);
			return;
		}
		
	}
	
}

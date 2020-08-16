package com.github.ejitron.helix;

import java.util.Arrays;

import org.jetbrains.annotations.Nullable;

import com.github.ejitron.Bot;
import com.github.ejitron.sql.Channels;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.helix.domain.ChannelInformation;

public class ChannelInfo {
	
	/**
	 * Sets the {@link com.github.twitch4j.helix.domain.ChannelInformation ChannelInformation} of the specified {@code channel}.
	 * @param channel name of the channel
	 * @param gameName name of the game. Must be exact match
	 * @param title new title
	 * @see #getChannelInfo(String)
	 */
	public void setChannelInfo(String channel, @Nullable String gameName, @Nullable String title) {
		Channels channels = new Channels();
		Game game = new Game();
		User user = new User();
		OAuth2Credential oauth = new OAuth2Credential(channel, channels.getChannelAccessToken(channel));
		
		ChannelInformation chanInfo = new ChannelInformation()
				.withGameId(game.getGameId(gameName))
				.withTitle(title);
		
		Bot.twitchClient.getHelix().updateChannelInformation(oauth.getAccessToken(), user.getUserFromChannel(channel).getId(), chanInfo).execute();
	}
	
	/**
	 * Fetches the {@link com.github.twitch4j.helix.domain.ChannelInformation ChannelInformation} of the specified channel.
	 * @param channel name of the channel
	 * @return a {@link com.github.twitch4j.helix.domain.ChannelInformation ChannelInformation} object
	 */
	public ChannelInformation getChannelInfo(String channel) {
		User user = new User();
		return Bot.twitchClient.getHelix().getChannelInformation(Bot.chatOauth.getAccessToken(), Arrays.asList(user.getUserFromChannel(channel).getId())).execute().getChannels().get(0);
	}
	
}

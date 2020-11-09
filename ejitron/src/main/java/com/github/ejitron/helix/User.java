package com.github.ejitron.helix;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import com.github.ejitron.Bot;
import com.github.ejitron.sql.channels.Channel;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.domain.FollowList;
import com.github.twitch4j.helix.domain.ModeratorList;
import com.github.twitch4j.helix.domain.UserList;

public class User {
	
	/**
	 * Retrieves the {@link com.github.twitch4j.helix.domain.User User} of a channel.
	 * @param channel the channel name
	 * @return a {@link com.github.twitch4j.helix.domain.User User} object
	 * @see #getUserFromId(String)
	 */
	public com.github.twitch4j.helix.domain.User getUserFromChannel(String channel) {
		UserList usrList = Bot.twitchClient.getHelix().getUsers(Bot.chatOauth.getAccessToken(), null, Arrays.asList(channel)).execute();
		
		return usrList.getUsers().get(0);
	}
	
	/**
	 * Retrieves the {@link com.github.twitch4j.helix.domain.User User} of a userId
	 * @param userId a {@link java.lang.String String} userId
	 * @return a {@link com.github.twitch4j.helix.domain.User User} object
	 * @see #getUserFromChannel(String)
	 */
	public com.github.twitch4j.helix.domain.User getUserFromId(String userId) {
		UserList usrList = Bot.twitchClient.getHelix().getUsers(Bot.chatOauth.getAccessToken(), Arrays.asList(userId), null).execute();
		
		return usrList.getUsers().get(0);
	}
	
	/**
	 * Retrieves the time a {@code user} has followed the {@code channel} as a string.
	 * @param user the user to fetch the follow age from
	 * @param channel the channel the user is following
	 * @return a formatted {@link java.lang.String String} with the time.<br>
	 * Or {@code null} if not following.
	 */
	public String getFollowAge(String user, String channel) {
		Channel channels = new Channel();
		OAuth2Credential oauth = channels.getChannelOAuth2(channel);

		if(user.equalsIgnoreCase(channel))
			return null;
		
		FollowList resList = Bot.twitchClient.getHelix().getFollowers(oauth.getAccessToken(), getUserFromChannel(user).getId(), getUserFromChannel(channel).getId(), null, 1).execute();
		
		if(resList.getFollows().size() < 1)
			return null;
		
		Instant followDate = resList.getFollows().get(0).getFollowedAtInstant();
		LocalDateTime currentDate = LocalDateTime.now();
		LocalDateTime tempDate = LocalDateTime.ofInstant(followDate, ZoneOffset.UTC);
		
		long years = tempDate.until(currentDate, ChronoUnit.YEARS);
		tempDate = tempDate.plusYears(years);
		
		long months = tempDate.until(currentDate, ChronoUnit.MONTHS);
		tempDate = tempDate.plusMonths(months);
		
		long days = tempDate.until(currentDate, ChronoUnit.DAYS);
		
		if(years > 0)
			return years + " years, " + months + " months and " + days + " days";
		else if(months > 0)
			return months + " months and " + days + " days";
		else
			return days + " days";
	}
	
	/**
	 * Checks to see if the bot account has moderator status in the channel.
	 * @param channel a {@link java.lang.String String} channel name to check inside
	 * @return {@code true} if yes
	 */
	public boolean isBotModerator(String channel) {
		Channel channels = new Channel();
		OAuth2Credential oauth = channels.getChannelOAuth2(channel);
		TwitchHelix helix = Bot.twitchClient.getHelix();
		
		ModeratorList mods = helix.getModerators(oauth.getAccessToken(), getUserFromChannel(channel).getId(), Arrays.asList("567800258"), null).execute();
		
		return mods.getModerators() != null;
	}

}

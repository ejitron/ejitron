package com.github.ejitron.helix;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import com.github.ejitron.Bot;
import com.github.ejitron.sql.Channels;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.helix.domain.FollowList;
import com.github.twitch4j.helix.domain.UserList;

public class User {
	
	/**
	 * Retrieves the {@link com.github.twitch4j.helix.domain.User User} of a channel.
	 * @param channel the channel name
	 * @return a {@link com.github.twitch4j.helix.domain.User User} object
	 */
	public com.github.twitch4j.helix.domain.User getUserFromChannel(String channel) {
		UserList usrList = Bot.twitchClient.getHelix().getUsers(Bot.chatOauth.getAccessToken(), null, Arrays.asList(channel)).execute();
		
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
		Channels channels = new Channels();
		OAuth2Credential oauth = new OAuth2Credential(channel, channels.getChannelAccessToken(channel));
		
		FollowList resList = Bot.twitchClient.getHelix().getFollowers(oauth.getAccessToken(), getUserFromChannel(user).getId(), getUserFromChannel(channel).getId(), null, 1).execute();
		
		if(resList.getFollows().size() < 1)
			return null;
		
		LocalDateTime followDate = resList.getFollows().get(0).getFollowedAt();
		LocalDateTime currentDate = LocalDateTime.now();
		LocalDateTime tempDate = LocalDateTime.from(followDate);
		
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

}

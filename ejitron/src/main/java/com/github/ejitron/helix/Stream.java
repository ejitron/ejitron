package com.github.ejitron.helix;

import com.github.ejitron.Bot;
import com.github.ejitron.sql.channels.Channel;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.helix.domain.CommercialList;
import com.github.twitch4j.helix.domain.FollowList;
import com.github.twitch4j.helix.domain.StreamList;
import com.github.twitch4j.helix.domain.SubscriptionList;

import java.util.Arrays;

public class Stream {

	/**
	 * Fetches a stream object for the specified channel name
	 * @param channel a channel name
	 * @return {@link com.github.twitch4j.helix.domain.Stream Stream} object
	 */
	public com.github.twitch4j.helix.domain.Stream getStream(String channel) {
		StreamList streamList = Bot.twitchClient.getHelix().getStreams(
				Bot.chatOauth.getAccessToken(), 
				null, 
				null, 
				1,
				null, 
				null, 
				null, 
				Arrays.asList(channel))
				.execute();
		
		return (streamList.getStreams().size() > 0 ? streamList.getStreams().get(0) : null);
	}

	/**
	 * Runs an ad on the specified channel which lasts the specified time
	 * @param channel a channel name
	 * @param time {@link java.lang.Integer int} in seconds the ad will last
	 * @return {@code true if success}
	 */
	public boolean runCommercial(String channel, int time) {
		Channel channels = new Channel();
		User user = new User();
		OAuth2Credential oauth = channels.getChannelOAuth2(channel);

		CommercialList comList = Bot.twitchClient.getHelix().startCommercial(
				oauth.getAccessToken(),
				user.getUserFromChannel(channel).getId(),
				time)
				.execute();

		return comList.getCommercials().get(0).getMessage().isEmpty();
	}

	/**
	 * Gets the total amount of followers that a channel has.
	 * @param channel a channel name
	 * @return a {@link java.lang.Integer Integer} followers object
	 */
	public int getFollowers(String channel) {
		Channel channels = new Channel();
		User user = new User();
		OAuth2Credential oauth = channels.getChannelOAuth2(channel);

		FollowList followList = Bot.twitchClient.getHelix().getFollowers(
				oauth.getAccessToken(),
				null,
				user.getUserFromChannel(channel).getId(),
				null,
				null)
				.execute();

		return followList.getTotal();
	}

	/**
	 * Gets the total amount of subscribers that a channel has.
	 * @param channel a channel name
	 * @return a {@link java.lang.Integer Integer} object with total subscribers count
	 */
	public int getSubscribers(String channel) {
		Channel channels = new Channel();
		User user = new User();

		if(user.getUserFromChannel(channel).getBroadcasterType().isEmpty()) // If the channel has no subscription feature, just skip it!
			return -1;

		OAuth2Credential oauth = channels.getChannelOAuth2(channel);

		SubscriptionList subscriptionList = Bot.twitchClient.getHelix().getSubscriptions(
				oauth.getAccessToken(),
				user.getUserFromChannel(channel).getId(),
				null,
				null,
				null)
				.execute();

		int subs = subscriptionList.getSubscriptions().size();
		int response = subscriptionList.getSubscriptions().size();
		String pagination = subscriptionList.getPagination().getCursor();

		do {
			subscriptionList = Bot.twitchClient.getHelix().getSubscriptions(
					oauth.getAccessToken(),
					user.getUserFromChannel(channel).getId(),
					pagination,
					null,
					null)
					.execute();
			subs += subscriptionList.getSubscriptions().size();
			response = subscriptionList.getSubscriptions().size();
			pagination = subscriptionList.getPagination().getCursor();
		} while(response > 0);

		return subs;

	}
}

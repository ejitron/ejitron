package com.github.ejitron.sql.channels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.github.ejitron.Credentials;
import com.github.ejitron.Identity;
import com.github.ejitron.helix.User;
import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.credentialmanager.identityprovider.TwitchIdentityProvider;

public class Channels {
	
	/**
	 * Retrieves all the channels that registered the bot from the database.
	 * @return a {@link java.util.List List} with channel names as {@link java.lang.String String} values.<br>
	 * If there was an exception it returns {@code null}
	 * @see #getChannelAccessToken(String)
	 */
	public List<String> getAddedChannels() {
		ResultSet result;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credentials.DB_HOST.getValue() + ":3306/" + Credentials.DB_NAME.getValue() + "?serverTimezone=UTC",
					Credentials.DB_USER.getValue(),
					Credentials.DB_PASS.getValue());
			
			Statement stmt = con.createStatement();
			result = stmt.executeQuery("SELECT channel FROM channels;");

			List<String> channels = new ArrayList<String>();

			while (result.next()) {
				channels.add(result.getString(1));
			}
			
			stmt.close();
			result.close();
			con.close();

			return channels;

		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	/**
	 * Retrieves the {@link com.github.philippheuer.credentialmanager.domain.OAuth2Credential OAuth2Credential} access token for the specified channel
	 * @param channel a {@link java.lang.String String} channel name
	 * @return The access token, or {@code null} if failed
	 * @see #getChannelRefreshToken(String)
	 */
	public String getChannelAccessToken(String channel) {
		ResultSet result;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credentials.DB_HOST.getValue() + ":3306/" + Credentials.DB_NAME.getValue() + "?serverTimezone=UTC",
					Credentials.DB_USER.getValue(),
					Credentials.DB_PASS.getValue());
			
			PreparedStatement pstmt = con.prepareStatement("SELECT token FROM channels WHERE channel=?;");
			pstmt.setString(1, channel);
			result = pstmt.executeQuery();

			String token = "";

			while (result.next()) {
				token = result.getString(1);
			}
			
			pstmt.close();
			result.close();
			con.close();

			return token;

		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	/**
	 * Retrieves the {@link com.github.philippheuer.credentialmanager.domain.OAuth2Credential OAuth2Credential} refresh token for the specified channel
	 * @param channel a {@link java.lang.String String} channel name
	 * @return The <b>refresh</b> token, or {@code null} if failed
	 * @see #getChannelAccessToken(String)
	 */
	public String getChannelRefreshToken(String channel) {
		ResultSet result;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credentials.DB_HOST.getValue() + ":3306/" + Credentials.DB_NAME.getValue() + "?serverTimezone=UTC",
					Credentials.DB_USER.getValue(),
					Credentials.DB_PASS.getValue());
			
			PreparedStatement pstmt = con.prepareStatement("SELECT refresh_token FROM channels WHERE channel=?;");
			pstmt.setString(1, channel);
			result = pstmt.executeQuery();

			String token = "";

			while (result.next()) {
				token = result.getString(1);
			}
			
			pstmt.close();
			result.close();
			con.close();

			return token;

		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	/**
	 * Retrieves the OAuth2 instance for the specified channel
	 * @param channel a {@link java.lang.String String} channel name
	 * @return a refreshed {@link com.github.philippheuer.credentialmanager.domain.OAuth2Credential OAuth2Credential}
	 * @see #refreshChannelOAuth2(String)
	 */
	public OAuth2Credential getChannelOAuth2(String channel) {
		OAuth2Credential oauth = new OAuth2Credential(channel, getChannelAccessToken(channel), getChannelRefreshToken(channel), null, null, null, null);
		
		Identity identity = new Identity();
		TwitchIdentityProvider identityProvider = identity.getIdentityProvider();
		
		User user = new User();
		String userId = user.getUserFromChannel(channel).getId();
		
		CredentialManager credentialManager = CredentialManagerBuilder.builder().build();
		credentialManager.registerIdentityProvider(identityProvider);
		credentialManager.addCredential(identityProvider.getProviderName(), oauth);
		
		return credentialManager.getOAuth2CredentialByUserId(userId).get();
	}
	
	/**
	 * Refreshes a channel {@link com.github.philippheuer.credentialmanager.domain.OAuth2Credential OAuth2Credential} and stores it.
	 * @param channel a {@link java.lang.String String} channel name
	 * @see #getChannelOAuth2(String)
	 */
	public void refreshChannelOAuth2(String channel) {
		OAuth2Credential oauth = getChannelOAuth2(channel);
		
		Identity identity = new Identity();
		TwitchIdentityProvider identityProvider = identity.getIdentityProvider();
		
		OAuth2Credential refreshed = identityProvider.refreshCredential(oauth).get();
		
		/*
		 * TODO
		 * Save the new access & refresh token
		 */
	}
}

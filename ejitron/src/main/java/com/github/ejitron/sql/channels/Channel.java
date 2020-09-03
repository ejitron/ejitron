package com.github.ejitron.sql.channels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.github.ejitron.oauth.Credential;
import com.github.ejitron.oauth.Identity;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;

public class Channel {
	
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
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
					Credential.DB_USER.getValue(),
					Credential.DB_PASS.getValue());
			
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
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
					Credential.DB_USER.getValue(),
					Credential.DB_PASS.getValue());
			
			PreparedStatement pstmt = con.prepareStatement("SELECT access_token FROM channels WHERE channel=?;");
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
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
					Credential.DB_USER.getValue(),
					Credential.DB_PASS.getValue());
			
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
	 * @return a complete {@link com.github.philippheuer.credentialmanager.domain.OAuth2Credential OAuth2Credential}
	 * @see #refreshChannelOAuth2(String)
	 */
	public OAuth2Credential getChannelOAuth2(String channel) {
		OAuth2Credential oauth = new OAuth2Credential("twitch", getChannelAccessToken(channel), getChannelRefreshToken(channel), null, null, null, null);
		
		Identity identity = new Identity();
		TwitchIdentityProvider identityProvider = identity.getIdentityProvider();
		
		return identityProvider.getAdditionalCredentialInformation(oauth).get();
	}
	
	/**
	 * Refreshes a channel {@link com.github.philippheuer.credentialmanager.domain.OAuth2Credential OAuth2Credential} and stores it.
	 * @param channel a {@link java.lang.String String} channel name
	 * @see #getChannelOAuth2(String)
	 */
	public boolean refreshChannelOAuth2(String channel) {
		OAuth2Credential oauth = getChannelOAuth2(channel);
		
		Identity identity = new Identity();
		TwitchIdentityProvider identityProvider = identity.getIdentityProvider();
		
		OAuth2Credential refreshed = identityProvider.refreshCredential(oauth).get();
		
		// Revoke the old auth token
		identity.getIdentityProvider().revokeCredential(oauth);
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
						Credential.DB_USER.getValue(),
						Credential.DB_PASS.getValue());

			PreparedStatement pstmt = con.prepareStatement("UPDATE channels SET access_token=?, refresh_token=? WHERE channel=?");
			pstmt.setString(1, refreshed.getAccessToken());
			pstmt.setString(2, refreshed.getRefreshToken());
			pstmt.setString(3, channel);
			pstmt.executeUpdate();

			con.close();

			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
}

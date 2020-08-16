package com.github.ejitron.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.github.ejitron.Credentials;

public class Channels {
	
	/**
	 * Retrieves all the channels that registered the bot from the database.
	 * @return a {@link java.util.List List} with channel names as {@link java.lang.String String} values.<br>
	 * If there was an exception it returns {@code null}
	 * @see #getChannelAccessToken(String)
	 */
	public List<String> getAddedChannels() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credentials.DB_HOST.getValue() + ":3306/" + Credentials.DB_NAME.getValue() + "?serverTimezone=UTC",
					Credentials.DB_USER.getValue(),
					Credentials.DB_PASS.getValue());
			
			Statement stmt = con.createStatement();

			ResultSet result;
			result = stmt.executeQuery("SELECT channel FROM channels;");
			stmt.close();

			List<String> channels = new ArrayList<String>();

			while (result.next()) {
				channels.add(result.getString(1));
			}
			
			result.close();
			con.close();

			return channels;

		} catch (Exception e) {
			System.out.println(e);
			throw null;
		}
	}
	
	/**
	 * Retrieves the {@link com.github.philippheuer.credentialmanager.domain.OAuth2Credential OAuth2Credential} access token for the specified channel
	 * @param channel a {@link java.lang.String String} channel name
	 * @return The access token, or {@code null} if failed
	 */
	public String getChannelAccessToken(String channel) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credentials.DB_HOST.getValue() + ":3306/" + Credentials.DB_NAME.getValue() + "?serverTimezone=UTC",
					Credentials.DB_USER.getValue(),
					Credentials.DB_PASS.getValue());
			
			PreparedStatement pstmt = con.prepareStatement("SELECT token FROM channels WHERE channel=?;");
			pstmt.setString(1, channel);
			
			ResultSet result;
			result = pstmt.executeQuery();
			pstmt.close();

			String token = "";

			while (result.next()) {
				token = result.getString(1);
			}
			
			result.close();
			con.close();

			return token;

		} catch (Exception e) {
			System.out.println(e);
			throw null;
		}
	}
}

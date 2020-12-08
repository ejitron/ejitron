package com.github.ejitron.sql.channels;

import com.github.ejitron.oauth.Credential;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AutomessageList {

	/**
	 * Fetches all the automessages a channel has saved
	 * @param channel a channel name
	 * @return a {@link java.util.List List} filled with messages
	 */
	public List<String> getChannelAutomessages(String channel) {
		ResultSet result;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
					Credential.DB_USER.getValue(),
					Credential.DB_PASS.getValue());

			PreparedStatement pstmt = con.prepareStatement("SELECT message FROM automessages WHERE channel=?;");
			pstmt.setString(1, channel);
			result = pstmt.executeQuery();

			List<String> messages = new ArrayList<>();

			while (result.next()) {
				messages.add(result.getString(1));
			}

			pstmt.close();
			result.close();
			con.close();

			return messages;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retrieves a list of channels that has automessages saved.
	 * @return a {@link java.util.List List} of channel names
	 */
	public List<String> getChannelsWithAutomessage() {
		ResultSet result;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
					Credential.DB_USER.getValue(),
					Credential.DB_PASS.getValue());

			PreparedStatement pstmt = con.prepareStatement("SELECT channel FROM automessages;");
			result = pstmt.executeQuery();

			List<String> channels = new ArrayList<>();

			while (result.next()) {
				if(!channels.contains(result.getString(1)))
					channels.add(result.getString(1));
			}

			pstmt.close();
			result.close();
			con.close();

			return channels;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

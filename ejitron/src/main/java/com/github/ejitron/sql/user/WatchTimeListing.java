package com.github.ejitron.sql.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.github.ejitron.chat.user.WatchTime;
import com.github.ejitron.oauth.Credential;

public class WatchTimeListing {
	
	/**
	 * Retrieves all saved watchtime as a {@link java.util.List List}
	 * @return a {@link java.util.List List} containing all saved {@link com.github.ejitron.chat.user.WatchTime WatchTime}
	 */
	public List<WatchTime> getSavedWatchTime() {
		ResultSet result;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
					Credential.DB_USER.getValue(),
					Credential.DB_PASS.getValue());
			
			Statement stmt = con.createStatement();
			result = stmt.executeQuery("SELECT channel,user,minutes FROM watchtime;");

			List<WatchTime> watchTime = new ArrayList<>();

			while (result.next()) {
				watchTime.add(
						new WatchTime(
								result.getString(1),
								result.getString(2),
								result.getInt(3)
							)
					);
			}
			
			stmt.close();
			result.close();
			con.close();

			return watchTime;

		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	/**
	 * Retrieves a list of all known lurkers, bots and other accounts we don't want to record!
	 * @return a {@link java.util.List List} containing lurker names
	 */
	public List<String> getKnownLurkers() {
		ResultSet result;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
					Credential.DB_USER.getValue(),
					Credential.DB_PASS.getValue());
			
			Statement stmt = con.createStatement();
			result = stmt.executeQuery("SELECT lurker FROM lurkers;");

			List<String> lurkers = new ArrayList<>();

			while (result.next()) {
				lurkers.add(result.getString(1));
			}
			
			stmt.close();
			result.close();
			con.close();

			return lurkers;

		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	/**
	 * Sets the saved watch time for a specific user in a specific channel.
	 * @param channel a channel name
	 * @param user the name of the user
	 * @param minutes the total watched minutes in {@link java.lang.Integer Integer}
	 * @return {@code true} if success
	 * @see #addWatchTime(String, String, int)
	 */
	public boolean setWatchTime(String channel, String user, int minutes) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
						Credential.DB_USER.getValue(),
						Credential.DB_PASS.getValue());

			PreparedStatement pstmt = con.prepareStatement("UPDATE watchtime SET user=?, minutes=? WHERE channel=? AND user=?");
			pstmt.setString(1, user);
			pstmt.setInt(2, minutes);
			pstmt.setString(3, channel);
			pstmt.setString(4, user);
			pstmt.executeUpdate();

			con.close();

			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	/**
	 * Adds a new user to the saved watch time list.
	 * @param channel a channel name
	 * @param user the user name
	 * @param minutes the total minutes watched as a {@link java.lang.Integer Integer}
	 * @return {@code true} if success
	 * @see #setWatchTime(String, String, int)
	 */
	public boolean addWatchTime(String channel, String user, int minutes) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
						Credential.DB_USER.getValue(),
						Credential.DB_PASS.getValue());

			PreparedStatement pstmt = con.prepareStatement("INSERT INTO watchtime(`channel`,`user`,`minutes`) VALUES(?,?,?);");
			pstmt.setString(1, channel);
			pstmt.setString(2, user);
			pstmt.setInt(3, minutes);
			pstmt.executeUpdate();

			con.close();

			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
}

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
	 * TODO Document
	 * @return
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

			List<WatchTime> watchTime = new ArrayList<WatchTime>();

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
	 * TODO Document
	 * @return
	 */
	public List<String> getKnownLurkers() {
		ResultSet result;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
					Credential.DB_USER.getValue(),
					Credential.DB_PASS.getValue());
			
			Statement stmt = con.createStatement();
			result = stmt.executeQuery("SELECT channel,user,minutes FROM watchtime;");

			List<String> lurkers = new ArrayList<String>();

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
	 * TODO Document
	 * @param channel
	 * @param user
	 * @param minutes
	 * @return
	 */
	public boolean setWatchTime(String channel, String user, int minutes) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
						Credential.DB_USER.getValue(),
						Credential.DB_PASS.getValue());

			PreparedStatement pstmt = con.prepareStatement("UPDATE watchtime SET user=?, minutes=? WHERE channel=?");
			pstmt.setString(1, user);
			pstmt.setInt(2, minutes);
			pstmt.setString(3, channel);
			pstmt.executeUpdate();

			con.close();

			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	/**
	 * TODO Document
	 * @param channel
	 * @param user
	 * @param minutes
	 * @return
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

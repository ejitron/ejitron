package com.github.ejitron.sql.channels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.github.ejitron.oauth.Credential;

public class Setting {
	
	/**
	 * Retrieves the value of a specific setting for the specified channel.
	 * @param channel the channel name
	 * @param setting the setting name
	 * @return a {@link java.lang.Integer Integer} object with the value.
	 * @see #getChannelSettingString(String, String)
	 */
	public int getChannelSetting(String channel, String setting) {
		ResultSet result;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
					Credential.DB_USER.getValue(),
					Credential.DB_PASS.getValue());
			
			PreparedStatement pstmt = con.prepareStatement("SELECT `" + setting + "` FROM settings WHERE channel=?;");
			pstmt.setString(1, channel);
			result = pstmt.executeQuery();

			int settingStatus = 0;

			while (result.next()) {
				settingStatus = result.getInt(1);
			}
			
			pstmt.close();
			result.close();
			con.close();

			return settingStatus;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Retrieves the string value of a setting.
	 * @param channel the channel name
	 * @param setting the setting name
	 * @return a {@link java.lang.String String} object with the value.
	 * @see #getChannelSetting(String, String)
	 */
	public String getChannelSettingString(String channel, String setting) {
		ResultSet result;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
					Credential.DB_USER.getValue(),
					Credential.DB_PASS.getValue());
			
			PreparedStatement pstmt = con.prepareStatement("SELECT `" + setting + "` FROM settings WHERE channel=?;");
			pstmt.setString(1, channel);
			result = pstmt.executeQuery();

			String settingValue = "";

			while (result.next()) {
				settingValue = result.getString(1);
			}
			
			pstmt.close();
			result.close();
			con.close();

			return settingValue;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

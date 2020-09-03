package com.github.ejitron.sql.channels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.github.ejitron.chat.CustomCommand;
import com.github.ejitron.oauth.Credential;

public class Command {
	
	/**
	 * Retrieves a list of all custom commands stored in the database
	 * @return a {@link java.util.List List} of {@link com.github.ejitron.chat.CustomCommand CustomCommand} objects
	 */
	public List<CustomCommand> getCustomCommands() {
		ResultSet result;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
					Credential.DB_USER.getValue(),
					Credential.DB_PASS.getValue());
			
			Statement stmt = con.createStatement();
			result = stmt.executeQuery("SELECT channel,command,reply FROM commands;");

			List<CustomCommand> commandList = new ArrayList<CustomCommand>();

			while (result.next()) {
				commandList.add(new CustomCommand(result.getString(1), result.getString(2), result.getString(3)));
			}
			
			stmt.close();
			result.close();
			con.close();

			return commandList;

		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
}

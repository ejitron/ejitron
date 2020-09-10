package com.github.ejitron.sql.commands;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.github.ejitron.Bot;
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
	
	/**
	 * Adds a {@link com.github.ejitron.chat.CustomCommand.CustomCommand CustomCommand} to the database and the local commands list
	 * @param channel the channel the command belongs to
	 * @param command the command name
	 * @param reply the reply for the command
	 * @return {@code true} if success
	 * @see #deleteCustomCommand(String, String)
	 * @see #editCustomCommand(String, String, String)
	 */
	public boolean addCustomCommand(String channel, String command, String reply) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
						Credential.DB_USER.getValue(),
						Credential.DB_PASS.getValue());

			PreparedStatement pstmt = con.prepareStatement("INSERT INTO commands(`channel`,`command`,`reply`) VALUES(?,?,?);");
			pstmt.setString(1, channel);
			pstmt.setString(2, command);
			pstmt.setString(3, reply);
			pstmt.executeUpdate();

			con.close();
			
			Bot.customCommandsList.add(new CustomCommand(channel, command, reply));

			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	/**
	 * Deletes a {@link com.github.ejitron.chat.CustomCommand.CustomCommand CustomCommand} from the database and the local commands list
	 * @param channel the channel the command belongs to
	 * @param command the command name
	 * @return {@code true} if success
	 * @see #addCustomCommand(String, String, String)
	 * @see #editCustomCommand(String, String, String)
	 */
	public boolean deleteCustomCommand(String channel, String command) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
						Credential.DB_USER.getValue(),
						Credential.DB_PASS.getValue());

			PreparedStatement pstmt = con.prepareStatement("DELETE FROM commands WHERE channel=? AND command=?");
			pstmt.setString(1, channel);
			pstmt.setString(2, command);
			pstmt.executeUpdate();

			con.close();
			
			// Remove the command from the local list as well!
			for(int i = 0; i < Bot.customCommandsList.size(); i++) {
				CustomCommand curCommand = Bot.customCommandsList.get(i);
				
				if(curCommand.getChannel().equalsIgnoreCase(channel) && curCommand.getCommand().equalsIgnoreCase(command))
					Bot.customCommandsList.remove(i);
			}

			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	/**
	 * Edits an existing {@link com.github.ejitron.chat.CustomCommand.CustomCommand CustomCommand} 
	 * and stores the updated 
	 * one in the database and the local commands list.<br>
	 * <b>Make sure the command exists. Will not do anything otherwise</b>
	 * @param channel the channel the command belongs to
	 * @param command the command name
	 * @param newReply the new reply the command is supposed to send
	 * @return {@code true} if success
	 * @see #commandExists(String, String)
	 * @see #addCustomCommand(String, String, String)
	 * @see #deleteCustomCommand(String, String)
	 */
	public boolean editCustomCommand(String channel, String command, String newReply) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
						Credential.DB_USER.getValue(),
						Credential.DB_PASS.getValue());

			PreparedStatement pstmt = con.prepareStatement("UPDATE commands SET reply=? WHERE channel=? AND command=?");
			pstmt.setString(1, newReply);
			pstmt.setString(2, channel);
			pstmt.setString(3, command);
			pstmt.executeUpdate();

			con.close();
			
			// Remove the command from the local list as well!
			for(int i = 0; i < Bot.customCommandsList.size(); i++) {
				CustomCommand curCommand = Bot.customCommandsList.get(i);
				
				if(curCommand.getChannel().equalsIgnoreCase(channel) && curCommand.getCommand().equalsIgnoreCase(command))
					Bot.customCommandsList.set(i, new CustomCommand(channel, command, newReply));
			}

			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	/**
	 * Checks the database if the given command exists.
	 * @param channel the channel the command belongs to
	 * @param command the command name
	 * @return {@code true} if yes
	 */
	public boolean commandExists(String channel, String command) {
		ResultSet result;
		List<CustomCommand> commandList = new ArrayList<CustomCommand>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Credential.DB_HOST.getValue() + ":3306/" + Credential.DB_NAME.getValue() + "?serverTimezone=UTC",
					Credential.DB_USER.getValue(),
					Credential.DB_PASS.getValue());
			
			Statement stmt = con.createStatement();
			result = stmt.executeQuery("SELECT channel,command FROM commands;");

			while (result.next()) {
				commandList.add(new CustomCommand(result.getString(1), result.getString(2), null));
			}
			
			stmt.close();
			result.close();
			con.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		
		for(int i = 0; i < commandList.size(); i++) {
			CustomCommand curCommand = commandList.get(i);
			
			if(curCommand.getChannel().equalsIgnoreCase(channel) && curCommand.getCommand().equalsIgnoreCase(command))
				return true;
		}
		
		return false;
	}
}

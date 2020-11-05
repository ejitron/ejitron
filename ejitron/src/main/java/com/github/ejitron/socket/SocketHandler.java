package com.github.ejitron.socket;

public class SocketHandler {
	
	public void onMessageReceived(String message) {
		String args[] = message.split("\\s+");
		String setting = args[0];
		String channel = args[1];
		
		/*
		 * Example message:
		 * cmd rlhypr
		 * Will refresh the local list of commands for the channel of rlHypr
		 */
	}
	
}

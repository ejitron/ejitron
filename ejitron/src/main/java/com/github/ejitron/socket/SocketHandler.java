package com.github.ejitron.socket;

public class SocketHandler {
	
	public void onMessageReceived(String message) {
		String[] args = message.split("\\s+");
		
		if(args.length <= 1) // We need at least 2 args to actually do anything here
			return;
		
		String setting = args[0];
		String channel = args[1];
		
		/*
		 * Example message:
		 * cmd rlhypr
		 * Will refresh the local list of commands for the channel of rlHypr
		 */
	}
	
}

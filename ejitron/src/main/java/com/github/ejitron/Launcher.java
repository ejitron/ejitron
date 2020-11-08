package com.github.ejitron;

import java.io.IOException;

import com.github.ejitron.socket.WebSocket;

public final class Launcher {
	
	private Launcher() {}
	
	public static void main(String[] args) {
		Bot bot = new Bot();
		
		// Load configuration and join channels
		bot.start();
		
		// Launch the websocket server
		WebSocket webSocket = new WebSocket();
		try {
			webSocket.start(6967);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

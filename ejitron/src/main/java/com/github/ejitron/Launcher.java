package com.github.ejitron;

import java.io.IOException;

import com.github.ejitron.socket.WebSocket;

public final class Launcher {
	
	private Launcher() {}
	
	public static void main(String[] args) throws IOException {
		Bot bot = new Bot();
		WebSocket webSocket = new WebSocket();
		
		// Launch the websocket server
		webSocket.start(6967);
		
		// Load configuration and join channels
		bot.start();
	}
}

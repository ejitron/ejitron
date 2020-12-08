package com.github.ejitron;

import java.io.IOException;

import com.github.ejitron.socket.WebSocket;

public final class Launcher {
	
//	private Launcher() {}
	
	public static void main(String[] args) {
		// Load configuration and join channels
		Bot bot = new Bot();
		bot.start();

		// Launch the websocket server
		WebSocket webSocket = new WebSocket();
		try {
			System.out.println("Websocket launching...");
			webSocket.start(6967);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

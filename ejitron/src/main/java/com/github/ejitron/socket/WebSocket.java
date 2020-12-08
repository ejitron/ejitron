package com.github.ejitron.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class WebSocket {
	
	/**
	 * Starts the actual websocket server.
	 * @param port Which port to have the server to listen to.
	 * @throws IOException
	 */
	public void start(int port) throws IOException {
		String clientSentence;
		ServerSocket welcomeSocket = new ServerSocket(port);

		while(true) {
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			clientSentence = inFromClient.readLine();
			connectionSocket.close();

			SocketHandler socketHandler = new SocketHandler();
			socketHandler.onMessageReceived(clientSentence);
		}
	}
}

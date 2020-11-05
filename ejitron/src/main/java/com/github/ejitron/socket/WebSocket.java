package com.github.ejitron.socket;

import java.io.*;
import java.net.*;

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
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientSentence = inFromClient.readLine();
			connectionSocket.close();

			SocketHandler socketHandler = new SocketHandler();
			socketHandler.onMessageReceived(clientSentence);
		}
	}
}

package com.github.ejitron;

public class Launcher {
	public static void main(String[] args) {
		Bot bot = new Bot();
		
		// Load configuration and join channels
		bot.start();
	}
}
package com.github.ejitron.threads;

import java.util.TimerTask;

import com.github.ejitron.chat.user.WatchTimer;

public class Minute extends TimerTask {
	
	@SuppressWarnings("unused")
	@Override
	public void run() {
		WatchTimer watchTimer = new WatchTimer();
	}

}

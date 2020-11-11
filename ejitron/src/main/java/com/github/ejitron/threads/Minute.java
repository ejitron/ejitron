package com.github.ejitron.threads;

import java.util.TimerTask;

import com.github.ejitron.channels.AddChannel;
import com.github.ejitron.chat.user.WatchTimer;

public class Minute extends TimerTask {

	@Override
	public void run() {
		new WatchTimer();
		new AddChannel();
	}

}

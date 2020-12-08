package com.github.ejitron.threads;

import java.util.TimerTask;

import com.github.ejitron.oauth.RefreshToken;

public class Hour extends TimerTask {

	@Override
	public void run() {
		new RefreshToken();
	}

}

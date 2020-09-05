package com.github.ejitron.threads;

import java.util.TimerTask;

import com.github.ejitron.oauth.RefreshToken;

public class Hour extends TimerTask {

	@SuppressWarnings("unused")
	@Override
	public void run() {
		RefreshToken refreshToken = new RefreshToken();
	}

}

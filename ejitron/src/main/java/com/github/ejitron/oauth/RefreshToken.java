package com.github.ejitron.oauth;

import java.util.TimerTask;

import com.github.ejitron.sql.channels.Channel;

public class RefreshToken extends TimerTask {

	@Override
	public void run() {
		Channel channel = new Channel();
		
		// Refresh the tokens for each channel added!
		channel.getAddedChannels().forEach(ch -> {
			channel.refreshChannelOAuth2(ch);
		});
	}

}

package com.github.ejitron.oauth;

import com.github.ejitron.sql.channels.Channel;

public class RefreshToken {

	public RefreshToken() {
		Channel channel = new Channel();
		
		// Refresh the tokens for each channel added!
		channel.getAddedChannels().forEach(ch -> {
			channel.refreshChannelOAuth2(ch);
		});
	}

}

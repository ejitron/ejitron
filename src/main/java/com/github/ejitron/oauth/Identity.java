package com.github.ejitron.oauth;

import com.github.twitch4j.auth.providers.TwitchIdentityProvider;

public class Identity {
	private final TwitchIdentityProvider identityProvider = new TwitchIdentityProvider("client_id", "client_secret", "redirect_uri");
	
	/**
	 * Retrieves the Twitch Identity Provider instance
	 * @return a {@link com.github.philippheuer.credentialmanager.identityprovider.TwitchIdentityProvider TwitchIdentityProvider} instance
	 */
	public TwitchIdentityProvider getIdentityProvider() {
		return identityProvider;
	}
}

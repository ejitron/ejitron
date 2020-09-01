package com.github.ejitron;

import com.github.philippheuer.credentialmanager.identityprovider.TwitchIdentityProvider;

public class Identity {
	private static final TwitchIdentityProvider identityProvider = new TwitchIdentityProvider("client_id", "client_secret", "redirect_uri");
	
	/**
	 * Retrieves the Twitch Identity Provider instance
	 * @return a {@link com.github.philippheuer.credentialmanager.identityprovider.TwitchIdentityProvider TwitchIdentityProvider} instance
	 */
	public TwitchIdentityProvider getIdentityProvider() {
		return identityProvider;
	}
}

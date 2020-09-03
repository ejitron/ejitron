package com.github.ejitron.oauth;

public enum Credential {
	BOT_OAUTH("chat_oauth"),
	DB_HOST("host"),
	DB_NAME("name"),
	DB_USER("user"),
	DB_PASS("pw");
	
	private String val;

	Credential(String string) {
		this.val = string;
	}
	
	/**
	 * Gets the value of the {@link com.github.ejitron.oauth.Credential Credential}
	 * @return {@link java.lang.String String} value of the credential
	 */
	public String getValue() {
		return val;
	}
	
}

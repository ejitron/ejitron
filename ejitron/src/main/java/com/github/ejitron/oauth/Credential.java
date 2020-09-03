package com.github.ejitron;

public enum Credential {
	BOT_OAUTH("bot_oauth"),
	DB_HOST("host"),
	DB_NAME("name"),
	DB_USER("user"),
	DB_PASS("password");
	
	private String val;

	Credential(String string) {
		this.val = string;
	}
	
	/**
	 * Gets the value of the {@link com.github.ejitron.Credential Credential}
	 * @return {@link java.lang.String String} value of the credential
	 */
	public String getValue() {
		return val;
	}
	
}

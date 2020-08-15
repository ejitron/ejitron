package com.github.ejitron;

public enum Credentials {
	BOT_OAUTH("BOT_ACCESS_TOKEN"),
	DB_HOST("DB_HOST"),
	DB_NAME("DB_NAME"),
	DB_USER("DB_USER"),
	DB_PASS("DB_PASSWD");
	
	private String val;

	Credentials(String string) {
		this.val = string;
	}
	
	/**
	 * Gets the value of the {@link com.github.ejitron.Credentials Credential}
	 * @return {@link java.lang.String String} value of the credential
	 */
	public String getValue() {
		return val;
	}
	
}

package com.github.ejitron;

public enum Credential {
	BOT_OAUTH("oauth:buyf4ys99lhj120a0b28l748ueuzev"),
	DB_HOST("pgda.xyz"),
	DB_NAME("ejitron"),
	DB_USER("ejitron_user"),
	DB_PASS("Jooh150601");
	
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

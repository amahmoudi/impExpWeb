package com.bluescale.business.mail;

import javax.mail.Message.RecipientType;

public final class Recipient {

	private final String name;

	private final String address;

	private final RecipientType type;

	public Recipient(final String name, final String address, final RecipientType type) {
		this.name = name;
		this.address = address;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public RecipientType getType() {
		return type;
	}
}
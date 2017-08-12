package com.bluescale.business.mail;

import javax.activation.DataSource;

final class AttachmentResource {

	private final String name;

	private final DataSource dataSource;

	public AttachmentResource(final String name, final DataSource dataSource) {
		this.name = name;
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public String getName() {
		return name;
	}
}
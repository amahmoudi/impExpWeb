package com.bluescale.business.mail;

import java.util.Properties;

import javax.mail.Session;

public enum TransportStrategy {

	SMTP_PLAIN {
		@Override
		public Properties generateProperties() {
			final Properties props = super.generateProperties();
			props.put("mail.transport.protocol", "smtp");
			return props;
		};

		@Override
		String propertyNameHost() {
			return "mail.smtp.host";
		};

		@Override
		String propertyNamePort() {
			return "mail.smtp.port";
		};

		@Override
		String propertyNameUsername() {
			return "mail.smtp.username";
		};

		@Override
		String propertyNameAuthenticate() {
			return "mail.smtp.auth";
		};
	},

	SMTP_SSL {

		@Override
		public Properties generateProperties() {
			final Properties props = super.generateProperties();
			props.put("mail.transport.protocol", "smtps");
			props.put("mail.smtps.quitwait", "false");
			return props;
		};

		@Override
		String propertyNameHost() {
			return "mail.smtps.host";
		};

		@Override
		String propertyNamePort() {
			return "mail.smtps.port";
		};

		@Override
		String propertyNameUsername() {
			return "mail.smtps.username";
		};

		@Override
		String propertyNameAuthenticate() {
			return "mail.smtps.auth";
		};
	},

	SMTP_TLS {

		@Override
		public Properties generateProperties() {
			final Properties props = super.generateProperties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.starttls.enable", "true");
			return props;
		}

		@Override
		String propertyNameHost() {
			return "mail.smtp.host";
		};

		@Override
		String propertyNamePort() {
			return "mail.smtp.port";
		};

		@Override
		String propertyNameUsername() {
			return "mail.smtp.username";
		};

		@Override
		String propertyNameAuthenticate() {
			return "mail.smtp.auth";
		};

	};

	public Properties generateProperties() {
		return new Properties();
	}

	abstract String propertyNameHost();

	abstract String propertyNamePort();

	abstract String propertyNameUsername();

	abstract String propertyNameAuthenticate();
};
package com.bluescale.business.mail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;
import javax.mail.Message.RecipientType;
import javax.mail.util.ByteArrayDataSource;

public class Email {
	private Recipient fromRecipient;

	private Recipient replyToRecipient;

	private String text;

	private String textHTML;

	private String subject;

	private final List<Recipient> recipients;

	private final List<AttachmentResource> embeddedImages;

	private final List<AttachmentResource> attachments;

	private final Map<String, String> headers;

	public Email() {
		recipients = new ArrayList<Recipient>();
		embeddedImages = new ArrayList<AttachmentResource>();
		attachments = new ArrayList<AttachmentResource>();
		headers = new HashMap<String, String>();
	}

	public void setFromAddress(final String name, final String fromAddress) {
		fromRecipient = new Recipient(name, fromAddress, null);
	}

	public void setReplyToAddress(final String name, final String replyToAddress) {
		replyToRecipient = new Recipient(name, replyToAddress, null);
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public void setTextHTML(final String textHTML) {
		this.textHTML = textHTML;
	}

	public void addRecipient(final String name, final String address, final RecipientType type) {
		recipients.add(new Recipient(name, address, type));
	}

	public void addEmbeddedImage(final String name, final byte[] data, final String mimetype) {
		final ByteArrayDataSource dataSource = new ByteArrayDataSource(data, mimetype);
		dataSource.setName(name);
		addEmbeddedImage(name, dataSource);
	}

	public void addEmbeddedImage(final String name, final DataSource imagedata) {
		embeddedImages.add(new AttachmentResource(name, imagedata));
	}

	public void addHeader(final String name, final Object value) {
		headers.put(name, String.valueOf(value));
	}

	public void addAttachment(final String name, final byte[] data, final String mimetype) {
		final ByteArrayDataSource dataSource = new ByteArrayDataSource(data, mimetype);
		dataSource.setName(name);
		addAttachment(name, dataSource);
	}

	public void addAttachment(final String name, final DataSource filedata) {
		attachments.add(new AttachmentResource(name, filedata));
	}

	public Recipient getFromRecipient() {
		return fromRecipient;
	}

	public Recipient getReplyToRecipient() {
		return replyToRecipient;
	}

	public String getSubject() {
		return subject;
	}

	public String getText() {
		return text;
	}

	public String getTextHTML() {
		return textHTML;
	}

	public List<AttachmentResource> getAttachments() {
		return Collections.unmodifiableList(attachments);
	}

	public List<AttachmentResource> getEmbeddedImages() {
		return Collections.unmodifiableList(embeddedImages);
	}

	public List<Recipient> getRecipients() {
		return Collections.unmodifiableList(recipients);
	}

	public Map<String, String> getHeaders() {
		return Collections.unmodifiableMap(headers);
	}

	public static class Builder {
		private Recipient fromRecipient;

		private Recipient replyToRecipient;

		private String text;

		private String textHTML;

		private String subject;

		private final List<Recipient> recipients;

		private final List<AttachmentResource> embeddedImages;

		private final List<AttachmentResource> attachments;

		private final Map<String, String> headers;

		private Email email;

		public Builder() {
			recipients = new ArrayList<Recipient>();
			embeddedImages = new ArrayList<AttachmentResource>();
			attachments = new ArrayList<AttachmentResource>();
			headers = new HashMap<String, String>();
		}

		/**
		 *
		 */
		public Email build() {
			return new Email(this);
		}

		public Builder from(final String name, final String fromAddress) {
			this.fromRecipient = new Recipient(name, fromAddress, null);
			return this;
		}

		public Builder replyTo(final String name, final String replyToAddress) {
			this.replyToRecipient = new Recipient(name, replyToAddress, null);
			return this;
		}

		public Builder subject(final String subject) {
			this.subject = subject;
			return this;
		}

		/**
		 * Sets the {@link #text}.
		 */
		public Builder text(final String text) {
			this.text = text;
			return this;
		}

		public Builder textHTML(final String textHTML) {
			this.textHTML = textHTML;
			return this;
		}

		public Builder to(final String name, final String address) {
			recipients.add(new Recipient(name, address, RecipientType.TO));
			return this;
		}

		public Builder to(final Recipient recipient) {
			recipients.add(new Recipient(recipient.getName(), recipient.getAddress(), RecipientType.TO));
			return this;
		}

		public Builder cc(final String name, final String address) {
			recipients.add(new Recipient(name, address, RecipientType.CC));
			return this;
		}

		public Builder cc(final Recipient recipient) {
			recipients.add(new Recipient(recipient.getName(), recipient.getAddress(), RecipientType.CC));
			return this;
		}

		public Builder bcc(final String name, final String address) {
			recipients.add(new Recipient(name, address, RecipientType.BCC));
			return this;
		}

		public Builder bcc(final Recipient recipient) {
			recipients.add(new Recipient(recipient.getName(), recipient.getAddress(), RecipientType.BCC));
			return this;
		}

		public Builder embedImage(final String name, final byte[] data, final String mimetype) {
			final ByteArrayDataSource dataSource = new ByteArrayDataSource(data, mimetype);
			dataSource.setName(name);
			return embedImage(name, dataSource);
		}

		public Builder embedImage(final String name, final DataSource imagedata) {
			embeddedImages.add(new AttachmentResource(name, imagedata));
			return this;
		}

		public Builder addHeader(final String name, final Object value) {
			headers.put(name, String.valueOf(value));
			return this;
		}

		public void addAttachment(final String name, final byte[] data, final String mimetype) {
			final ByteArrayDataSource dataSource = new ByteArrayDataSource(data, mimetype);
			dataSource.setName(name);
			addAttachment(name, dataSource);
		}

		public void addAttachment(final String name, final DataSource filedata) {
			attachments.add(new AttachmentResource(name, filedata));
		}

	}

	private Email(Builder builder) {
		recipients = builder.recipients;
		embeddedImages = builder.embeddedImages;
		attachments = builder.attachments;
		headers = builder.headers;

		fromRecipient = builder.fromRecipient;
		replyToRecipient = builder.replyToRecipient;
		text = builder.text;
		textHTML = builder.textHTML;
		subject = builder.subject;
	}
}
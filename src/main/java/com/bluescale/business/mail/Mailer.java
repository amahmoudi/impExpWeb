package com.bluescale.business.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mailer {

	private static final Logger logger = LoggerFactory.getLogger(Mailer.class);

	private static final String CHARACTER_ENCODING = "UTF-8";

	private final Session session;

	private TransportStrategy transportStrategy;

	private EmailAddressValidationCriteria emailAddressValidationCriteria;

	public Mailer(final Session session) {
		this.session = session;
		this.emailAddressValidationCriteria = null;
	}

	public Mailer(final String host, final Integer port, final String username, final String password,
			final TransportStrategy transportStrategy) {
		// we're doing these validations manually instead of using Apache
		// Commons to avoid another dependency
		if (host == null || host.trim().equals("")) {
			throw new MailException(MailException.MISSING_HOST);
		} else if ((password != null && !password.trim().equals(""))
				&& (username == null || username.trim().equals(""))) {
			throw new MailException(MailException.MISSING_USERNAME);
		}
		this.transportStrategy = transportStrategy;
		this.session = createMailSession(host, port, username, password);
		this.emailAddressValidationCriteria = null;
	}

	protected Session createMailSession(final String host, final Integer port, final String username,
			final String password) {
		if (transportStrategy == null) {
			logger.warn("Transport Strategy not set, using plain SMTP strategy instead!");
			transportStrategy = TransportStrategy.SMTP_PLAIN;
		}
		Properties props = transportStrategy.generateProperties();
		props.put(transportStrategy.propertyNameHost(), host);
		if (port != null) {
			props.put(transportStrategy.propertyNamePort(), String.valueOf(port));
		} else {
			// let JavaMail's Transport objects determine deault port base don
			// the used protocol
		}

		if (username != null) {
			props.put(transportStrategy.propertyNameUsername(), username);
		}

		if (password != null) {
			props.put(transportStrategy.propertyNameAuthenticate(), "true");
			return Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
		} else {
			return Session.getInstance(props);
		}
	}

	/**
	 * Overloaded constructor which produces a new {@link Session} on the fly,
	 * using default vanilla SMTP transport protocol.
	 * 
	 * @param host
	 *            The address URL of the SMTP server to be used.
	 * @param port
	 *            The port of the SMTP server.
	 * @param username
	 *            An optional username, may be <code>null</code>.
	 * @param password
	 *            An optional password, may be <code>null</code>, but only if
	 *            username is <code>null</code> as well.
	 * @see #Mailer(String, Integer, String, String, TransportStrategy)
	 */
	public Mailer(final String host, final Integer port, final String username, final String password) {
		this(host, port, username, password, TransportStrategy.SMTP_PLAIN);
	}

	/**
	 * In case Simple Java Mail falls short somehow, you can get a hold of the
	 * internal {@link Session} instance to debug or tweak. Please let us know
	 * why you are needing this on
	 * https://github.com/bbottema/simple-java-mail/issues.
	 */
	public Session getSession() {
		logger.warn(
				"Providing access to Session instance for emergency fall-back scenario. Please let us know why you need it.");
		logger.warn("\t>https://github.com/bbottema/simple-java-mail/issues");
		return session;
	}

	public void setDebug(final boolean debug) {
		session.setDebug(debug);
	}

	public void applyProperties(final Properties properties) {
		session.getProperties().putAll(properties);
	}

	public final void sendMail(final Email email) throws MailException {
		if (validate(email)) {
			try {
				// create new wrapper for each mail being sent (enable sending
				// multiple emails with one mailer)
				final MimeEmailMessageWrapper messageRoot = new MimeEmailMessageWrapper();
				// fill and send wrapped mime message parts
				final Message message = prepareMessage(email, messageRoot);
				logSession(session, transportStrategy);
				message.saveChanges(); // some headers and id's will be set for
										// this specific message
				Transport transport = session.getTransport();
				try {
					transport.connect();
					transport.sendMessage(message, message.getAllRecipients());
				} finally {
					transport.close();
				}
			} catch (final UnsupportedEncodingException e) {
				logger.error(e.getMessage(), e);
				throw new MailException(String.format(MailException.INVALID_ENCODING, e.getMessage()));
			} catch (final MessagingException e) {
				logger.error(e.getMessage(), e);
				throw new MailException(String.format(MailException.GENERIC_ERROR, e.getMessage()), e);
			}
		}
	}

	private void logSession(Session session, TransportStrategy transportStrategy) {
		Properties properties = session.getProperties();
		final String specifics;
		if (transportStrategy != null) {
			final String logmsg = "starting mail session (host: %s, port: %s, username: %s, authenticate: %s, transport: %s)";
			specifics = String.format(logmsg, properties.get(transportStrategy.propertyNameHost()),
					properties.get(transportStrategy.propertyNamePort()),
					properties.get(transportStrategy.propertyNameUsername()),
					properties.get(transportStrategy.propertyNameAuthenticate()), transportStrategy);
		} else {
			specifics = properties.toString();
		}
		logger.debug(String.format("starting mail session (%s)", specifics));
	}

	public boolean validate(final Email email) throws MailException {
		if (email.getText() == null && email.getTextHTML() == null) {
			throw new MailException(MailException.MISSING_CONTENT);
		} else if (email.getSubject() == null || email.getSubject().equals("")) {
			throw new MailException(MailException.MISSING_SUBJECT);
		} else if (email.getRecipients().size() == 0) {
			throw new MailException(MailException.MISSING_RECIPIENT);
		} else if (email.getFromRecipient() == null) {
			throw new MailException(MailException.MISSING_SENDER);
		} else if (emailAddressValidationCriteria != null) {
			if (!EmailValidationUtil.isValid(email.getFromRecipient().getAddress(), emailAddressValidationCriteria)) {
				throw new MailException(String.format(MailException.INVALID_SENDER, email));
			}
			for (final Recipient recipient : email.getRecipients()) {
				if (!EmailValidationUtil.isValid(recipient.getAddress(), emailAddressValidationCriteria)) {
					throw new MailException(String.format(MailException.INVALID_RECIPIENT, email));
				}
			}
			if (email.getReplyToRecipient() != null) {
				if (!EmailValidationUtil.isValid(email.getReplyToRecipient().getAddress(),
						emailAddressValidationCriteria)) {
					throw new MailException(String.format(MailException.INVALID_REPLYTO, email));
				}
			}
		}
		return true;
	}

	private Message prepareMessage(final Email email, final MimeEmailMessageWrapper messageRoot)
			throws MessagingException, UnsupportedEncodingException {
		final MimeMessage message = new MimeMessage(session);
		// set basic email properties
		message.setSubject(email.getSubject(), CHARACTER_ENCODING);
		message.setFrom(new InternetAddress(email.getFromRecipient().getAddress(), email.getFromRecipient().getName(),
				CHARACTER_ENCODING));
		setReplyTo(email, message);
		setRecipients(email, message);
		// fill multipart structure
		setTexts(email, messageRoot.multipartAlternativeMessages);
		setEmbeddedImages(email, messageRoot.multipartRelated);
		setAttachments(email, messageRoot.multipartRoot);
		message.setContent(messageRoot.multipartRoot);
		setHeaders(email, message);
		message.setSentDate(new Date());
		return message;
	}

	private void setRecipients(final Email email, final Message message)
			throws UnsupportedEncodingException, MessagingException {
		for (final Recipient recipient : email.getRecipients()) {
			final Address address = new InternetAddress(recipient.getAddress(), recipient.getName(),
					CHARACTER_ENCODING);
			message.addRecipient(recipient.getType(), address);
		}
	}

	private void setReplyTo(final Email email, final Message message)
			throws UnsupportedEncodingException, MessagingException {
		final Recipient replyToRecipient = email.getReplyToRecipient();
		if (replyToRecipient != null) {
			InternetAddress replyToAddress = new InternetAddress(replyToRecipient.getAddress(),
					replyToRecipient.getName(), CHARACTER_ENCODING);
			message.setReplyTo(new Address[] { replyToAddress });
		}
	}

	private void setTexts(final Email email, final MimeMultipart multipartAlternativeMessages)
			throws MessagingException {
		if (email.getText() != null) {
			final MimeBodyPart messagePart = new MimeBodyPart();
			messagePart.setText(email.getText(), CHARACTER_ENCODING);
			multipartAlternativeMessages.addBodyPart(messagePart);
		}
		if (email.getTextHTML() != null) {
			final MimeBodyPart messagePartHTML = new MimeBodyPart();
			messagePartHTML.setContent(email.getTextHTML(), "text/html; charset=\"" + CHARACTER_ENCODING + "\"");
			multipartAlternativeMessages.addBodyPart(messagePartHTML);
		}
	}

	private void setEmbeddedImages(final Email email, final MimeMultipart multipartRelated) throws MessagingException {
		for (final AttachmentResource embeddedImage : email.getEmbeddedImages()) {
			multipartRelated.addBodyPart(getBodyPartFromDatasource(embeddedImage, Part.INLINE));
		}
	}

	private void setAttachments(final Email email, final MimeMultipart multipartRoot) throws MessagingException {
		for (final AttachmentResource resource : email.getAttachments()) {
			multipartRoot.addBodyPart(getBodyPartFromDatasource(resource, Part.ATTACHMENT));
		}
	}

	private void setHeaders(final Email email, final Message message)
			throws UnsupportedEncodingException, MessagingException {
		// add headers (for raw message headers we need to 'fold' them using
		// MimeUtility
		for (Map.Entry<String, String> header : email.getHeaders().entrySet()) {
			String headerName = header.getKey();
			String headerValue = MimeUtility.encodeText(header.getValue(), CHARACTER_ENCODING, null);
			String foldedHeaderValue = MimeUtility.fold(headerName.length() + 2, headerValue);
			message.addHeader(header.getKey(), foldedHeaderValue);
		}
	}

	private BodyPart getBodyPartFromDatasource(final AttachmentResource resource, final String dispositionType)
			throws MessagingException {
		final BodyPart attachmentPart = new MimeBodyPart();
		final DataSource ds = resource.getDataSource();
		// setting headers isn't working nicely using the javax mail API, so
		// let's do that manually
		attachmentPart.setDataHandler(new DataHandler(resource.getDataSource()));
		attachmentPart.setFileName(resource.getName());
		attachmentPart.setHeader("Content-Type",
				ds.getContentType() + "; filename=" + ds.getName() + "; name=" + ds.getName());
		attachmentPart.setHeader("Content-ID", String.format("<%s>", ds.getName()));
		attachmentPart.setDisposition(dispositionType + "; size=0");
		return attachmentPart;
	}

	private class MimeEmailMessageWrapper {

		private final MimeMultipart multipartRoot;

		private final MimeMultipart multipartRelated;

		private final MimeMultipart multipartAlternativeMessages;

		MimeEmailMessageWrapper() {
			multipartRoot = new MimeMultipart("mixed");
			final MimeBodyPart contentRelated = new MimeBodyPart();
			multipartRelated = new MimeMultipart("related");
			final MimeBodyPart contentAlternativeMessages = new MimeBodyPart();
			multipartAlternativeMessages = new MimeMultipart("alternative");
			try {
				// construct mail structure
				multipartRoot.addBodyPart(contentRelated);
				contentRelated.setContent(multipartRelated);
				multipartRelated.addBodyPart(contentAlternativeMessages);
				contentAlternativeMessages.setContent(multipartAlternativeMessages);
			} catch (final MessagingException e) {
				logger.error(e.getMessage(), e);
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

	public void setEmailAddressValidationCriteria(EmailAddressValidationCriteria emailAddressValidationCriteria) {
		this.emailAddressValidationCriteria = emailAddressValidationCriteria;
	}
}

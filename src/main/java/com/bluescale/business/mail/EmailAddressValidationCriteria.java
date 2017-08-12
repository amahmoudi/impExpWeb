package com.bluescale.business.mail;

public class EmailAddressValidationCriteria {

	private final boolean allowDomainLiterals;
	private final boolean allowQuotedIdentifiers;

	public static final EmailAddressValidationCriteria RFC_COMPLIANT = new EmailAddressValidationCriteria(true, true);

	public EmailAddressValidationCriteria(boolean allowDomainLiterals, boolean allowQuotedIdentifiers) {
		this.allowDomainLiterals = allowDomainLiterals;
		this.allowQuotedIdentifiers = allowQuotedIdentifiers;
	}

	public final boolean isAllowDomainLiterals() {
		return allowDomainLiterals;
	}

	public final boolean isAllowQuotedIdentifiers() {
		return allowQuotedIdentifiers;
	}
}
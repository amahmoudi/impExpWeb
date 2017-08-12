/*
 * Copyright 2008 Les Hazlewood Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package com.bluescale.business.mail;

import java.util.regex.Pattern;

public final class EmailValidationUtil {

	public static boolean isValid(final String email) {
		return isValid(email, EmailAddressValidationCriteria.RFC_COMPLIANT);
	}

	public static boolean isValid(final String email,
			final EmailAddressValidationCriteria emailAddressValidationCriteria) {
		return buildValidEmailPattern(emailAddressValidationCriteria).matcher(email).matches();
	}

	protected static Pattern buildValidEmailPattern(EmailAddressValidationCriteria parameterObject) {
		final String wsp = "[ \\t]";
		final String fwsp = wsp + "*";
		final String dquote = "\\\"";
		final String noWsCtl = "\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F";
		final String asciiText = "[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F]";
		final String quotedPair = "(\\\\" + asciiText + ")";
		final String atext = "[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]";
		final String atom = fwsp + atext + "+" + fwsp;
		final String dotAtomText = atext + "+" + "(" + "\\." + atext + "+)*";
		final String dotAtom = fwsp + "(" + dotAtomText + ")" + fwsp;
		final String qtext = "[" + noWsCtl + "\\x21\\x23-\\x5B\\x5D-\\x7E]";
		final String qcontent = "(" + qtext + "|" + quotedPair + ")";
		final String quotedString = dquote + "(" + fwsp + qcontent + ")*" + fwsp + dquote;
		final String word = "((" + atom + ")|(" + quotedString + "))";
		final String phrase = word + "+"; // one or more words.
		final String letter = "[a-zA-Z]";
		final String letDig = "[a-zA-Z0-9]";
		final String letDigHyp = "[a-zA-Z0-9-]";
		final String rfcLabel = letDig + "(" + letDigHyp + "{0,61}" + letDig + ")?";
		final String rfc1035DomainName = rfcLabel + "(\\." + rfcLabel + ")*\\." + letter + "{2,6}";
		final String dtext = "[" + noWsCtl + "\\x21-\\x5A\\x5E-\\x7E]";
		final String dcontent = dtext + "|" + quotedPair;
		final String domainLiteral = "\\[" + "(" + fwsp + dcontent + "+)*" + fwsp + "\\]";
		final String rfc2822Domain = "(" + dotAtom + "|" + domainLiteral + ")";
		final String domain = parameterObject.isAllowDomainLiterals() ? rfc2822Domain : rfc1035DomainName;
		final String localPart = "((" + dotAtom + ")|(" + quotedString + "))";
		final String addrSpec = localPart + "@" + domain;
		final String angleAddr = "<" + addrSpec + ">";
		final String nameAddr = "(" + phrase + ")?" + fwsp + angleAddr;
		final String mailbox = nameAddr + "|" + addrSpec;
		final String patternString = parameterObject.isAllowQuotedIdentifiers() ? mailbox : addrSpec;
		return Pattern.compile(patternString);
	}
}
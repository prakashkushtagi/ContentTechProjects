/**
 * Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.domain.nyredev;

import static com.trgr.dockets.core.util.StringUtil.linefeedConcat;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

import com.trgr.dockets.core.util.IndentableStrBuilder;
import com.trgr.dockets.core.util.LineReader;

public final class PersonName {

	private final String prefix;
	
	private final String firstName;
	
	private final String middleName;
	
	private final String lastName;
	
	private final String seniority;
	
	private final String suffix;
	
	private final String fullNameWithPrefixFirst;
	
	private final String fullNameWithLastFirst;
	
	public PersonName(
			final String prefix,
			final String firstName,
			final String middleName,
			final String lastName,
			final String seniority,
			final String suffix,
			final String fullNameWithPrefixFirst,
			final String fullNameWithLastFirst) {
		this.prefix = prefix;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.seniority = seniority;
		this.suffix = suffix;
		this.fullNameWithPrefixFirst = fullNameWithPrefixFirst;
		this.fullNameWithLastFirst = fullNameWithLastFirst;
	}
	
	public PersonName(final LineReader lineReader) {
		this(lineReader.getValue(1),
				lineReader.getValue(2),
				lineReader.getValue(3),
				lineReader.getValue(4),
				lineReader.getValue(5),
				lineReader.getValue(6),
				lineReader.getValue(7),
				lineReader.getValue(8));
	}
	
	public String getFullNameWithPrefixFirst(){
		return fullNameWithPrefixFirst;
	}
	
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	public String toLineSplitString() {
		return linefeedConcat(
				prefix,firstName,middleName,lastName,seniority,suffix,fullNameWithPrefixFirst,fullNameWithLastFirst);
	}
	
	public final void toXML(final StringBuilder b) {
		b.append("<JudgeName>");
		appendElem(b,"Prefix",prefix);
		appendElem(b,"First",firstName);
		appendElem(b,"Middle",middleName);
		appendElem(b,"Last",lastName);
		appendElem(b,"Seniority",seniority);
		appendElem(b,"Suffix",suffix);
		appendElem(b,"NamePrefixFirst",fullNameWithPrefixFirst);
		appendElem(b,"NameLastFirst",fullNameWithLastFirst);
		b.append("</JudgeName>");
	}
	
	private static void appendElem(final StringBuilder b, final String tag, final String string) {
		if (StringUtils.isBlank(string)){
			return;
		}
		
		b.append("<" + tag + ">" + string + "</" + tag + ">");
	}
	
	public final void toBuffer(final IndentableStrBuilder b) {
		b.appendSection("Name",new Runnable() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void run() {
				b.appendLabeledValue("Prefix", prefix);
				b.appendLabeledValue("First", firstName);
				b.appendLabeledValue("Middle", middleName);
				b.appendLabeledValue("Last", lastName);
				b.appendLabeledValue("Seniority", seniority);
				b.appendLabeledValue("Suffix", suffix);
				b.appendLabeledValue("Full with prefix first", fullNameWithPrefixFirst);
				b.appendLabeledValue("Full with last first", fullNameWithLastFirst);
			}
		});
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonName other = (PersonName) obj;
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (middleName == null) {
			if (other.middleName != null)
				return false;
		} else if (!middleName.equals(other.middleName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (seniority == null) {
			if (other.seniority != null)
				return false;
		} else if (!seniority.equals(other.seniority))
			return false;
		if (suffix == null) {
			if (other.suffix != null)
				return false;
		} else if (!suffix.equals(other.suffix))
			return false;
		if (fullNameWithPrefixFirst == null) {
			if (other.fullNameWithPrefixFirst != null)
				return false;
		} else if (!fullNameWithPrefixFirst.equals(other.fullNameWithPrefixFirst))
			return false;
		if (fullNameWithLastFirst == null) {
			if (other.fullNameWithLastFirst != null)
				return false;
		} else if (!fullNameWithLastFirst.equals(other.fullNameWithLastFirst))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		int prime = 7;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
		result = prime * result + ((middleName == null) ? 0 : middleName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((seniority == null) ? 0 : seniority.hashCode());
		result = prime * result + ((suffix == null) ? 0 : suffix.hashCode());
		result = prime * result + ((fullNameWithPrefixFirst == null) ? 0 : fullNameWithPrefixFirst.hashCode());
		result = prime * result + ((fullNameWithLastFirst == null) ? 0 : fullNameWithLastFirst.hashCode());
		return result;
	}
}

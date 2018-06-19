/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.domain.nyredev;

import static com.trgr.dockets.core.util.StringUtil.linefeedConcat;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.trgr.dockets.core.util.IndentableStrBuilder;
import com.trgr.dockets.core.util.LineReader;

public final class Judge {

	private final String id;
	
	private final PersonName name;
	
	private final String type;
	
	public Judge(final String id, final PersonName name, final String type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}
	
	public Judge(final LineReader lineReader) {
		this(lineReader.getValue(0),new PersonName(lineReader),lineReader.getValue(9));
	}
	
	public final String getId() {
		return id;
	}
	
	public PersonName getPersonName(){
		return name;
	}
	
	@Override
	public final String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	public String toLineSplitString() {
		return linefeedConcat(id,name.toLineSplitString(),type);
	}
	
	public String toXML(final StringBuilder b) {
		b.append("<Judge JudgeID=\"" + id + "\">");
		name.toXML(b);
		b.append("<JudgeType>" + type + "</JudgeType>");
		b.append("</Judge>");
		return b.toString();
	}
	
	public final void toBuffer(final IndentableStrBuilder b) {
		b.appendSection("Judge", new Runnable() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void run() {
				b.appendLabeledValue("Id",id);
				name.toBuffer(b);
				b.appendLabeledValue("Type",type);
			}
		});
	}
}

/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent.data;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.trgr.dockets.core.util.IndentableStrBuilder;

public final class CharData extends XmlData {
	
	private final String string;

	public CharData(final String string) {
		this.string = string;
	}
	
	@Override
	public final boolean hasContent() {
		return isNotBlank(string);
	}
	
	public final String getString() {
		return string;
	}
	
	@Override
	public final String toString() {
		return string;
	}
	
	@Override
	public final void toBuffer(final IndentableStrBuilder b) {
		b.appendLines(string);
	}
}

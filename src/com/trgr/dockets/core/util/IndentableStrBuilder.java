/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

import static com.trgr.dockets.core.util.StringUtil.colonSpaceConcat;

public final class IndentableStrBuilder {

	private final StringBuilder fB = new StringBuilder();
	
	private int fIndentLevel = 0;
	
	private void indent() {
		for (int i = 0; i < fIndentLevel; i++) {
			fB.append("    ");
		}
	}
	
	public final void appendLines(final Object... lines) {
		for (final Object line: lines) {
			if (line == null) return;
			indent();
			fB.append(line);
			fB.append("\n");
		}
	}
	
	public final void appendSection(final String header, final Runnable runnable) {
		appendLines(header);
		fIndentLevel++;
		runnable.run();
		fIndentLevel--;}
	
	public final void appendSection(final Runnable runnable) {appendSection(null,runnable);}
	
	public final void appendLabeledValue(final String label, final Object value) {
		appendLines(colonSpaceConcat(label,value));}
	
	@Override
	public final String toString() {return fB.toString();}
}

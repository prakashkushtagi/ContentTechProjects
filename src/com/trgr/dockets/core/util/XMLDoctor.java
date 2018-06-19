/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited. 
 */

package com.trgr.dockets.core.util;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

public class XMLDoctor {
	
	static final Logger log = Logger.getLogger(XMLDoctor.class);
	
	/**
	 * Corrects non-well-formed attributes by assuming the largest 
	 * pair of double quotes before each starting attribute pattern 
	 * and escaping all unsafe XML characters inbetween
	 * @param input
	 */
	public static String correctAttributes(String input) {
		
		String original = input;
		
		int depthSanity = 0;
		
		Set<Integer> charsToFix = incrementalCorrectAttributes(input);
		while (charsToFix.size() > 0) {
			StringBuilder incrementalFix = new StringBuilder();
			for (int i = 0; i < input.length(); i++) {
				String ch = String.valueOf(input.charAt(i));
				if (charsToFix.contains(i)) {
					ch = StringEscapeUtils.escapeXml10(ch);
				}
				incrementalFix.append(ch);
			}
			input = incrementalFix.toString();
			
			charsToFix = incrementalCorrectAttributes(input);
			
			depthSanity++;
			//Because this is potentially an infinite loop, implemented a sanity check to make sure we don't ever go infinite
			if (depthSanity > 100) {
				log.error("Returning original source XML; too many repeated calls to attempt to correct XML. "
						+ "Other downstream steps may fail due to non-well-formed XML.");
				return original;
			}
		}
		
		return input;
	}
	
	private static Set<Integer> incrementalCorrectAttributes(String input) {
		boolean inElement = false;
		boolean startFound = false;
		
		String ch = null;
		String lastChar = null;
		String ampPhrase = null;
		
		Set<Integer> invalidCharSet = new HashSet<Integer>();
		Integer lastDoubleQuote = null;
		Integer startAttributePos = null;
		
		for (int i = 0; i < input.length(); i++) {
			ch = String.valueOf(input.charAt(i));
			if (i + 6 < input.length()) {
				ampPhrase = input.substring(i, i+6);
			} else {
				ampPhrase = input.substring(i, input.length());
			}
			if (null == lastChar) {
				lastChar = ch;
				if (ch.equals("<")) {
					startFound = true;
					inElement = true;
				}
			} else {
				if (ch.equals("<") && !startFound) {
					startFound = true;
					inElement = true;
				} else if (ch.equals(">") && inElement && 
						((lastDoubleQuote == null && startAttributePos == null) 
							|| 
						 (lastDoubleQuote != null && startAttributePos != null))) {
					//if zero, reset for the next start element and keep going
					if (invalidCharSet.size() == 0) {
						startFound = false;
						inElement = false;
						lastDoubleQuote = null;
						startAttributePos = null;
					//else return the invalidCharSet
					} else {
						return invalidCharSet;
					}
				} else if ((lastChar + ch).equals("=\"") && inElement) {
					if (null == startAttributePos) {
						startAttributePos = i;
					} else {
						if (invalidCharSet.size() > 0) {
							return invalidCharSet;
						} else {
							//reset position of start and last double quotes
							startAttributePos = i;
							lastDoubleQuote = null;
						}
					}
				} else if (ch.equals("\"")) {
					if (null == lastDoubleQuote) {
						lastDoubleQuote = i;
					} else {
						invalidCharSet.add(lastDoubleQuote);
						lastDoubleQuote = i;
					}
				} else if (inElement && (ch.equals("<") || ch.equals(">") || ch.equals("\'"))) {
					invalidCharSet.add(i);
				} else if (inElement && ch.equals("&") && !ampPhrase.matches("&(lt|gt|amp|quot|apos|#\\w+);.{0,4}")) {
					invalidCharSet.add(i);
				}
				lastChar = ch;
			}
		}
		return invalidCharSet;
	}
}

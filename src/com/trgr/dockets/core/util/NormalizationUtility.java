/**
 Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class NormalizationUtility {

	public static final List<String> suffixList = createSuffixList();

	private static List<String> createSuffixList() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("jr");
		list.add("jr.");
		list.add("junior");
		list.add("sr");
		list.add("senior");
		list.add("iii");
		list.add("ii");
		list.add("iv");
		list.add("esq");
		list.add("esq.");
		list.add("esql");
		list.add("esqs");
		list.add("esqas");
		list.add("srl");
		list.add("pro per");
		list.add("pro se");
		list.add("prose");
		list.add("pro-se");
		list.add("etal");
		list.add("et al");
		list.add("et. al");
		list.add("etal");
		list.add("et. al.");
		list.add("et.al.");
		list.add("et.al");
		list.add("aag");
		list.add("lg");
		list.add("d.d.s");
		list.add("dds");
		list.add("et ano");
		list.add("exrel ny");
		list.add("md");
		list.add("m.d");
		list.add("dvm");
		list.add("d.v.m");
		list.add("jd");
		list.add("j.d");
		list.add("cpn");
		list.add("c.p.n");
		list.add("cpa");
		list.add("c.p.a");
		list.add("dc");
		list.add("d.c");
		list.add("dd");
		list.add("d.d");
		list.add("edd");
		list.add("ed.d");
		list.add("dmin");
		list.add("d.min");
		list.add("dmeta");
		list.add("d.meta");
		list.add("phd");
		list.add("ph.d");
		list.add("thd");
		list.add("th.d");
		list.add("dma");
		list.add("d.m.a");
		list.add("dmus");
		list.add("d.mus");
		list.add("od");
		list.add("o.d");
		list.add("pharmd");
		list.add("pharm.d");
		list.add("psyd");
		list.add("psy.d");
		list.add("dpa");
		list.add("d.p.a");
		list.add("drph");
		list.add("dr.ph");
		list.add("dsc");
		list.add("d.sc");
		list.add("ei");
		list.add("e.i");
		list.add("eit");
		list.add("e.i.t");
		list.add("emt");
		list.add("e.m.t");
		list.add("lcsw");
		list.add("l.c.s.w");
		list.add("lls");
		list.add("l.l.s");
		list.add("lpn");
		list.add("l.p.n");
		list.add("lmsw");
		list.add("l.m.s.w");
		list.add("macc");
		list.add("m.acc");
		list.add("ma");
		list.add("m.a");
		list.add("mfa");
		list.add("m.f.a");
		list.add("mba");
		list.add("m.b.a");
		list.add("med");
		list.add("m.ed");
		list.add("mpa");
		list.add("m.p.a");
		list.add("mph");
		list.add("m.p.h");
		list.add("ms");
		list.add("m.s");
		list.add("pe");
		list.add("p.e");
		list.add("pmp");
		list.add("p.m.p");
		list.add("ra");
		list.add("r.a");
		list.add("rda");
		list.add("r.d.a");
		list.add("rla");
		list.add("r.l.a");
		list.add("rls");
		list.add("r.l.s");
		list.add("rn");
		list.add("r.n");
		list.add("rp");
		list.add("r.p");
		list.add("rrt");
		list.add("r.r.t");
		list.add("se");
		list.add("s.e");
		list.add("llm");
		list.add("ll.m");
		list.add("magistrate");
		list.add("esquire");
		list.add("pc");
		list.add("aka");
		list.add("a/k/a");
		list.add("phv");
		list.add("govt");
		list.add("ss");
		list.add("d.c");
		list.add("p.a");
		return list;
	}

	private static final List<String> prefixList = createPrefixList();

	private static List<String> createPrefixList() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("part 20");
		list.add("dr.");
		list.add("Hon.");
		return list;
	}

	/**
	 * @param name
	 *            in format: FAMIANO, ALEXANDER A. II SRL
	 * @return name should be: ALEXANDER A. FAMIANO II SRL
	 */
	public static String normalizePartyName(String name) {
		return normalizePartyName(name, true);
	}

	/**
	 * @param name
	 *            with out comma before suffix
	 * @return name with comma before suffix
	 */

	private static String addComma(String name) {
		String addComma = name;
		if (StringUtils.isNotBlank(name) && (!name.contains(","))) {
			String suffix = findSuffix(name);
			if (StringUtils.isNotBlank(suffix) && name.endsWith(suffix)) {
				int number = name.lastIndexOf(suffix);
				String foundSuffix = name.substring(number);
				addComma = addComma.replace(foundSuffix, "");
				addComma = addComma.trim() + ", " + foundSuffix.trim();
			} else {
				addComma = addComma.trim();
			}
		}
		return addComma;
	}

	/**
	 * @param name
	 *            in potential format of LAST, FIRST, MIDDLE, SUFFIX ie: CONNOR, MARTIN, E, JR.
	 * @return
	 */
	public static String lastMiddleFirstSuffix(String name) {
		String formattedName = name;
		if (StringUtils.isEmpty(name))
			return name;
		// Clean up of * and ?
		formattedName = formattedName.replaceAll("[?*]", " ");
		// Clean up extra spaces.
		formattedName = formattedName.replaceAll("(\\s)+", " ").trim();

		// name contains specific words like ATTY that means we just need to copy it as is.
		if (isSkip(formattedName) || isFirm(formattedName))
			return formattedName;

		if (formattedName.contains(",")) {
			String[] nameParts = formattedName.split(",");
			if (nameParts.length == 4) {
				String last = nameParts[0];
				String first = nameParts[1];
				String middle = nameParts[2];
				String suffix = nameParts[3];
				return first + " " + middle + " " + last + " " + suffix;
			} else {
				return normalizePartyName(name, true);
			}

		}
		return formattedName;

	}

	/**
	 * @param name
	 *            in format: FAMIANO, ALEXANDER A. II SRL
	 * @return name should be: ALEXANDER A. FAMIANO II SRL
	 */
	public static String normalizePartyName(String name, boolean removeNumber) {
		return normalizePartyName( name,  removeNumber, true);
	}
	public static String normalizePartyName(String name, boolean removeNumber, boolean switchIfComma) {
		if (StringUtils.isEmpty(name))
			return name;

		// If name is a URL, do not normalize
		if (nameIsURL(name)) {
			return name;
		}
		String normalizedPartyName = name;
		// Clean up extra spaces.
		normalizedPartyName = normalizedPartyName.replaceAll("(\\s)+", " ").trim();
		// Clean up special characters
//		normalizedPartyName = removeNonASCIICharacters(normalizedPartyName);
		
		// Do not normalize if "$3,688 IN U S CURRENCY" (Dollar followed by number and comma and number)
		if (Pattern.compile("\\$+\\d+,\\s*\\d+").matcher(normalizedPartyName).find()) {
			return normalizedPartyName;
		}

		// name contains specific words like ATTY that means we just need to copy it as is.
		if (isSkip(normalizedPartyName) || isFirm(normalizedPartyName))
			return normalizedPartyName;

		// Clean up of * and ?
		normalizedPartyName = normalizedPartyName.replaceAll("[?*]", " ");
		
		if(!normalizedPartyName.matches("^.*?RE:.*$"))
			normalizedPartyName = normalizedPartyName.replaceAll(":", " ");
		
		if (removeNumber) {
			// Remove Number from name if number is only content.
			normalizedPartyName = removeNumberFromName(normalizedPartyName);
			if (StringUtils.isEmpty(normalizedPartyName))
				return normalizedPartyName;
		}

		// for name followed by comma then number space and value, copy as is.  ex. Donte Curry, # 963666
		if (normalizedPartyName.matches(".*?,.*# \\d.*"))
		{
			return normalizedPartyName;
		}
		
		normalizedPartyName = filtername(normalizedPartyName); // Strip extra text in the name. Bug 121227, 120756

		// for name that does not contain a comma we just need to copy it as is.
		if (normalizedPartyName.contains(",") && switchIfComma) {
			
			normalizedPartyName = removeExcessCommasBeforeSuffixes(normalizedPartyName);

			String[] nameParts = normalizedPartyName.split(",");
			
			//for the name that contains more than 3 commas then we just need to copy as it is.			
			boolean lastPartIsSuffix;
			
			if(nameParts.length > 0){
				lastPartIsSuffix = isSuffix(nameParts[nameParts.length - 1], nameParts.length);
				
			}
			lastPartIsSuffix = containsSuffix(name);
			if((nameParts.length > 3) || (nameParts.length == 3 && !lastPartIsSuffix)){
				return normalizedPartyName;
			}


			if (nameParts.length > 0 && StringUtils.isNotBlank(nameParts[0])) {
				String suffixes = "";
				String firstNameWithSpace = "";
				String lastNameWithSpace = "";
				String firstName = "";
				String lastName = "";
				int nameCnt = 0;
				int partCnt = nameParts.length;
				
				// loop after splitting by comma
				for (int i = nameParts.length - 1; i >= 0; i--) // decrementing because we assume data comes in as last, first mi.
				{
					if (!isSuffix(nameParts[i].trim(), partCnt)) {
						if (firstNameWithSpace.isEmpty()) {
							firstNameWithSpace = nameParts[i].trim();
						}else if(lastNameWithSpace.isEmpty()) {
							lastNameWithSpace = nameParts[i].trim();
						}
						else {
							lastNameWithSpace = nameParts[i].trim() + " " +lastNameWithSpace;
						}
						nameCnt++;
					} else {
						suffixes = " " + checkAndRemoveParen(nameParts[i].trim()) + checkAndRemoveParen(suffixes);
					}

					// Check if list of names like a firm and just need to copy it as is.
					if (nameCnt > 2 && (isFirm(nameParts[i]) || nameParts[i].matches(".*[0-9].*"))) {
						return normalizedPartyName;
					}


				}

				String[] lastNameAndMore = lastNameWithSpace.split("\\s");
				String[] firstNameAndMore = firstNameWithSpace.split("\\s");
				partCnt = lastNameAndMore.length + firstNameAndMore.length;
				// loop through likely lastName splitting by space

				for (int i = lastNameAndMore.length - 1; i >= 0; i--) {
					if (isSuffix(lastNameAndMore[i].trim(), partCnt) || lastNameAndMore[i].trim().startsWith("#")) {
						suffixes = " " + checkAndRemoveParen(lastNameAndMore[i].trim()) + checkAndRemoveParen(suffixes);
					} else {
						if (!lastNameAndMore[i].isEmpty()) {
							lastName = " " + lastNameAndMore[i].trim() + lastName;
						}
					}
				}

				// loop through likely firstName splitting by space
				for (int i = firstNameAndMore.length - 1; i >= 0; i--) {
					if (isSuffix(firstNameAndMore[i].trim(), partCnt) || firstNameAndMore[i].trim().startsWith("#")) {
						suffixes = " " + checkAndRemoveParen(firstNameAndMore[i].trim()) + checkAndRemoveParen(suffixes);
					} else {
						if (!firstNameAndMore[i].isEmpty()) {
							firstName = " " + firstNameAndMore[i].trim() + firstName;
						}
					}
				}
				normalizedPartyName = "";
				if (!firstName.isEmpty() && !firstName.trim().startsWith("#")) {
					normalizedPartyName = firstName.trim();
				}
				if (!lastName.isEmpty()) {
					normalizedPartyName = normalizedPartyName + " " + lastName.trim();
				}
				if (!suffixes.isEmpty()) {
					//code is added to handle more than one suffixes to be seperated by a comma
					if (suffixes.matches("(?i)\\s*(i{1,3}|iv)")) {
						suffixes = suffixes.toUpperCase();
					}
					if (suffixes.contains("MR")) {
						if(suffixes.contains(",")){
							suffixes = suffixes.replaceAll(",", "");
						}
						normalizedPartyName = checkAndRemoveParen(suffixes) + " " + normalizedPartyName;
					} else {
						String commaorSpace = suffixes.startsWith(",") ? "" : ",";
						normalizedPartyName = normalizedPartyName + commaorSpace + checkAndRemoveParen(suffixes);
						if (!firstName.isEmpty() && firstName.startsWith("#"))
							normalizedPartyName = normalizedPartyName + " " + firstName.trim();
					}
				}
				if (!firstName.isEmpty() && firstName.trim().startsWith("#"))
					normalizedPartyName = normalizedPartyName + "," + firstName;
			}
		}
		
		// If there isn't a space after a period, add one, unless ph.d is present
		if (!name.toLowerCase().contains("ph.d") && !name.contains("$")) {
			normalizedPartyName = normalizedPartyName.replaceAll("\\.(?!\\s)(?!,)(?![A-Za-z]{1}\\.)(?![A-Za-z]{1}$)", ". ");
		}

		// Bug138650
		normalizedPartyName = validatePartyNameDin(normalizedPartyName);

		normalizedPartyName = normalizedPartyName.trim();
		normalizedPartyName = fixPrefix(normalizedPartyName);
		//To remove the comma at the end of the suffix if any.
		if(!normalizedPartyName.isEmpty() && normalizedPartyName.endsWith(",")){
			normalizedPartyName = normalizedPartyName.replaceAll(",$", "");
		}
		//Add comma if not present in name.
		normalizedPartyName  = addComma(normalizedPartyName);
		return normalizedPartyName;
	}
	
	private static String removeExcessCommasBeforeSuffixes(String name) {
		if(StringUtils.isEmpty(name))
			return name;
		if (name.split(",").length < 3 ) return name;
		String removeExcessComma = name;
		String[] nameParts = removeExcessComma.split(",");
		int partCnt = nameParts.length;
		for (String str : nameParts[partCnt - 1].split(" ")) {
			if (isSuffix(str, 1)) {
				removeExcessComma = "";
				for (int i = 0; i < partCnt - 2; i++)
					removeExcessComma += nameParts[i] + ",";
				removeExcessComma += nameParts[partCnt - 2] + nameParts[partCnt - 1];
				
				nameParts = removeExcessComma.split(",");
				partCnt = nameParts.length;
				break;
			}
		}
		return removeExcessComma;
	}

	/**
	 * Returns true if name is a URL.
	 * 
	 * @param name
	 * @return
	 */
	private static boolean nameIsURL(String name) {
		if (name.contains(".com") || name.contains(".net") || name.contains(".gov") || name.contains(".org") || name.contains(".biz")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Converts important non-ASCII characters into ASCII characters
	 * @author <a href="mailto:tyler.studanski@thomsonreuters.com">Tyler Studanski</a> <b>u0162605</b>
	 * @param name Name of Party
	 * @return Returns the name with all important characters converted
	 */
	public static String convertToASCIICharacters(String name, Collection<Entry<String, String>> mappingList) {
		String convertedName = name;
		for (Entry<String, String> mapping : mappingList) {
			convertedName = convertedName.replaceAll(mapping.getKey(), mapping.getValue());
		}
		return convertedName;
	}

	/**
	 * Removes any special characters that might be present in the name.
	 * 
	 * @param name Name of Party.
	 * @return
	 */
	public static String removeNonASCIICharacters(String name) {
		String removedNonAscii= name.replaceAll("[^\\x00-\\x7F]", "");
		return removedNonAscii;
	}

	/**
	 * Normalization rules: 1. Party should be returned as FIRST MI LAST, SUFFIX 2. Currently Names sent in as LAST, FIRST MI and FIRST MI LAST with or without suffixes are handled. If Names are sent in as LAST FIRST MI with no comma they will not be
	 * fixed. If Names are sent in as FIRST, LAST MI they will be set to LAST MI FIRST which is incorrect. 3. There is a known list of suffixes like JR which will be put at the end and preceded by a comma 4. An acronym is greater than 2 characters in a
	 * list of more than 2 nameWords that ends in a period and will be considered a suffix. 5. There is a list of words like P.L.L.C. related to FIRMS that causes any normalization to be skipped. 6. There is a list of words like ATTY GENERAL that causes
	 * any normalization to be skipped. 7. Phone Numbers are replaced with an empty string if the name is only digits. 8. Part and it's corresponding number is stripped from Party Names. This was mostly found for judges. 9. SRL - is replace with SRL, to
	 * avoid the hanging dash from Party Names. 10. ESQ - is replace with ESQ, to avoid the hanging dash from Party Names. 11. Anything between parenthesis are stripped. This was implemented for judges. 12. Space is added after a period that is followed by
	 * a word of more than 1 digit. 13. Names are trimmed of preceding and following space. 14. Acronym ph.d. is handled specially. 15. DIN # will moved to after the name. For example PHILLIP DIN#07B1202 YATES to PHILLIP YATES DIN#07B1202. 16. Slashes are
	 * removed 17. For words Referee and Executor the following dashes are removed. 18. OUT is removed 19. * and ? are removed. 20. If a name starts with a # then it is considered a suffix.
	 * 
	 * @param String
	 *            name of Party to be normalized in format: FAMIANO, ALEXANDER A. II SRL
	 * @return String normalized Party Name should be: ALEXANDER A. FAMIANO II SRL
	 */
	public static String normalizeNameWithTitle(String name, boolean removeNumber) {
		if (StringUtils.isEmpty(name)) {
			return name;
		}
		String normalizedPartyName = normalizePartyName(name, removeNumber);

		return normalizedPartyName.trim();
	}
	public static String normalizeNameWithTitle(String name, boolean removeNumber,boolean switchComma) {
		if (StringUtils.isEmpty(name)) {
			return name;
		}
		String normalizedPartyName = normalizePartyName(name, removeNumber, switchComma);

		return normalizedPartyName.trim();
	}
	public static String normalizeNameWithTitle(String name) {
		return normalizeNameWithTitle(name, true);
	}

	public static String normalizeJudgeName(String name, boolean removeNumber) {
		if (name == null || name.isEmpty() || name.startsWith("SMALL CLAIMS")) {
			return name;
		}

		if (name.equalsIgnoreCase("NO  ASSIGNED")) {
			return "No Judge Assigned";
		}
		String normalizedJudgeName = normalizeNameWithTitle(name, removeNumber);
		normalizedJudgeName = normalizedJudgeName.trim();

		return normalizedJudgeName;
	}

	/**
	 * @param name
	 * @return name with suffix content at end of string, assuming suffix comes inside parentheses.<br/> <br/> If the name sent to this method contains a group of parentheses that contains a suffix, said suffix will be extracted and put at the end of the
	 *         name (removing all instances of that parentheses group). <br/> <br/> If the name does not contain parentheses, or the name contains a group of parentheses that does not contain a suffix, then the value returned will be equal to the value
	 *         sent in.
	 */
	public static String shuffleSuffix(String name) {
		String newName = name;
		String regex = "\\((.*?)\\)";

		Pattern parenContains = Pattern.compile(regex);
		Matcher findParens = parenContains.matcher(name);

		if (findParens.find()) {

			String possibleSuffix = findParens.group(1);
			if (isSuffix(possibleSuffix, possibleSuffix.length())) {
				newName = name.replaceAll(possibleSuffix, "").replaceAll("\\(\\)", "").replaceAll("\\s+", " ") + " " + possibleSuffix;
			}
		}

		return newName;
	}

	public static boolean isFirm(String name) {
		if (name == null)
			return false;

		String upperCaseName = name.toUpperCase();
	
		String regex = new String("( LAW |FIRM|LEGAL|LAW OFFICE|INJURY LAWYER|" + "LLP|&| AND | OF |LLC|ASSOC|L\\.L\\.P|P\\.L\\.C|P\\.L\\.L\\.C|L\\.L\\.C|L\\.P\\.|\\$#AMP;|" + "P\\.A|P\\.C|PROFESSIONAL ASSOCIATION|PRO PER|IN PRO PER|"
				+ "[ ,]CO\\.*[ ,]|[ ,]CO\\.*$|[ ,]INC\\.*|[ ,]INC$|I\\sNC$|BAIL BONDS|PTRSHP| STATE\\b|" + "[ ,]COMPANY|[ ,]*CORP(\\W|$)|[ ,]*CORPORATION|[ ,]INCORPORATED|INVESTMENT|" + "[ ,]DEPT|[ ,]DEPARTMENT|[ ,]ET\\.* AL\\.*|[ ,]ETAL\\.*|PUBLIC DEFENDER|CONFLICT|"				
				+ "[ ,]OFFICE\\b|[ ,]LTD|[ ,]LIMITED|[ ,]PARTNERS|[ ,]COMPANIES|[ ,]FUND|" + "[ ,]ADJUSTMENT|[ ,]*ASSOCIATES|[ ,]LIABILITY|[ ,]INS\\.*|[ ,]INS$|[ ,]OFC\\.*|"
				+ "[ ,]OFC$ TOWN |INSURANCE|[ ,]GROUP|[ ,]MANAGEMENT|[ ,]SERVICE|[ ,]DEPARTMENT|[ ,]PC[.\\s]*$|~PC[,]|PC\\s-|\\bASSO\\b|\\bAPC\\b|\\bASSOCIATION\\b|\\bCHARTER\\b|\\bCHARTERED\\b|\\bLTD\\b|\\bPLC\\b|\\bPLLC\\b|[ ,]STATE |\\bBANK\\b|CITIBANK|\\bS\\.A\\.?\\b|\\bS\\.E\\.?\\b|\\bSA(?!')\\b|\\bSE\\b|\\bL\\.?P\\.?\\b|\\bLAW$ |\\bCOMMISSION\\b|^\\D*?\\bBUSINESS\\b.*?$|\\bORGANIZATION\\b)");

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(upperCaseName);

		boolean firmFound = m.find();
		if (firmFound && upperCaseName.contains("CO") && upperCaseName.contains("ATTY")) {
			// CO. (not followed by ATTY)
			// CO (not followed by ATTY)

			String regexFollowedBy = new String("CO[., ]ATTY");
			Pattern p2 = Pattern.compile(regexFollowedBy);
			Matcher m2 = p2.matcher(upperCaseName);
			boolean isCountyAttorney = m2.find();

			if (isCountyAttorney){ // if it is followed by ATTY then is not a firm but a county attorney
				firmFound = false;
			}
		}else if (firmFound && upperCaseName.contains("STATE") && !upperCaseName.contains("FOODS")) {
			//state followed by street/prison was being tagged as a firm and shouldn't be
			String regexStreet = new String("STATE\\s+(STREET|ROAD|AVE|AVENUE)");
			Pattern p3 = Pattern.compile(regexStreet);
			Matcher m3 = p3.matcher(upperCaseName);
			boolean isStreet = m3.find();
			if (isStreet){
				firmFound = false;
			}
		}else if(firmFound && upperCaseName.contains("POST OFFICE")){
			firmFound = false;
		}else if(firmFound && (upperCaseName.contains("PRO SE") || upperCaseName.matches(".*?\\bPROSE\\b.*?") || upperCaseName.contains("PRO-SE"))){
			firmFound = false;
		}
		
		// try to detect if it is part of a human name
		// look for a lastName, firstName middleInitial format
		Pattern lfm = Pattern.compile("\\w+,\\s*\\w+\\s+\\w");
		Matcher nameMatcher = lfm.matcher(upperCaseName);
		if (firmFound && nameMatcher.matches()) {
			firmFound = false;
		}

		return firmFound;
	}

	/**
	 * @param name name to test
	 * @return Returns false if a phone number is found in <code>name</code>, otherwise true
	 */
	public static boolean isValidName(String name) {
		if (name == null) {
			return false;
		}

		String phnRegex = "\\d{3}.\\d{3}.\\d{4}";

		Pattern phn = Pattern.compile(phnRegex);
		Matcher pm = phn.matcher(name);
		if (pm.find()) {
			return false;
		} else if (name.trim().matches("\\d+")) {
			return false;
		}
		return true;

	}

	public static String removeNumberFromName(String name) {
		if (name == null) {
			return null;
		}
		
		String phnRegex = "\\d*\\s*[-\\(]*\\d{3}.\\d{3}.\\d{4}";
		String removedNumberFromName = name;
		Pattern phn = Pattern.compile(phnRegex);
		Matcher pm = phn.matcher(name);
		if (pm.find()) {
			// Need to escape regex special characters if we're going to use the result as regex
			String groupText = pm.group().replaceAll("([\\(\\)\\.\\?\\*\\\\\\+\\{\\}\\[\\]\\^\\$])", "\\\\$1");
			return removedNumberFromName.replaceAll(groupText, "");
		} else if (removedNumberFromName.replaceAll("\\s", "").matches("\\d+")) {
			return removedNumberFromName.replaceAll("\\d+", "");
		}
		return removedNumberFromName;

	}

	public static String stripComment(String address) {
		if (StringUtils.isNotEmpty(address)) {
			String formattedAddress = address;
			if (formattedAddress.toUpperCase().contains("SEE COMMENT SCREEN FOR NAME") || formattedAddress.toUpperCase().contains("ADDRESS & PHONE") || formattedAddress.toUpperCase().contains("ADDRESS &AMP; PHONE") || formattedAddress.toUpperCase().contains("ADDRESS $#AMP; PHONE")) {
				formattedAddress = "";
			} else {
				formattedAddress = formattedAddress.replaceAll("\\s+", " ");
			}

			if (formattedAddress.matches("\\W*")) {
				formattedAddress = "";
			}

			return formattedAddress.trim();
		} else {
			return address;
		}
	}

	/**
	 * @param String
	 *            with Party names appear with DIN # in the middle, between first and last, ie: PHILLIP DIN#07B1202 YATES
	 * @return String with first and last name and follow by DIN #, ie: PHILLIP YATES DIN#07B1202 Other examples: REUBEN-DIN #07A0885 MCDOWELL and JERRY DIN #06A0730 RIVERA
	 */
	public static String validatePartyNameDin(String name) {
		int index1 = 0;
		int index2 = 0;
		int index3 = 0;
		int lastIndex = 0;
		int lastSpace = 0;
		String newValidNameDin = null;
		String dinNum = null;
		String lastName = null;

		lastIndex = name.length();
		index1 = name.indexOf("DIN#", index1);
		index2 = name.indexOf("-DIN #", index1);
		index3 = name.indexOf("DIN #", index1);

		if (index1 == -1 && index2 == -1 && index3 == -1) {
			newValidNameDin = name;
		} else {
			if (index1 != -1) {
				String firstName = name.substring(0, index1);
				lastSpace = name.lastIndexOf(" ");
				if (lastSpace == -1 || lastSpace < index1) {
					dinNum = name.substring(index1, lastIndex);
					newValidNameDin = (firstName.trim() + " " + dinNum);
				} else {
					dinNum = name.substring(index1, lastSpace);
					if (lastSpace < lastIndex) {
						lastName = name.substring(lastSpace + 1, lastIndex);
						newValidNameDin = (firstName.trim() + " " + lastName + " " + dinNum);
					} else {
						// do nothing
					}
				}
			} else if (index2 != -1) {
				String firstName = name.substring(0, index2);
				lastSpace = name.lastIndexOf(" ");
				if (lastSpace < lastIndex) {
					dinNum = name.substring(index2 + 1, lastSpace);
					lastName = name.substring(lastSpace + 1, lastIndex);
					newValidNameDin = (firstName + " " + lastName + " " + dinNum);
				} else {
					// do nothing
				}
			} else if (index3 >= 0) {
				String firstName = name.substring(0, (index3 == 0) ? index3 : index3 - 1);
				lastSpace = name.lastIndexOf(" ");
				if (lastSpace < lastIndex) {
					dinNum = name.substring(index3, lastSpace);
					lastName = name.substring(lastSpace + 1, lastIndex);
					newValidNameDin = (firstName + " " + lastName + " " + dinNum);
				} else {
					// do nothing
				}
			}
		}

		return newValidNameDin;
	}

	public static boolean isSuffix(String possibleSuffix, int numOfWords) {
		if (possibleSuffix == null)
			return false;
		possibleSuffix = checkAndRemoveParen(possibleSuffix);

		if (possibleSuffix.endsWith(".")) {
			possibleSuffix = possibleSuffix.substring(0, possibleSuffix.lastIndexOf("."));
			if (possibleSuffix.trim().length() == 1) {
				return false;
			}
			if (suffixList.contains(possibleSuffix.toLowerCase()) || checkWithoutParen(possibleSuffix.toLowerCase())) {
				return true;
			}
			// If possible suffix ends with a period assume it is a legitimate acronym as a suffix.
			if (possibleSuffix.length() > 1 && !possibleSuffix.contains(" ") && numOfWords > 2
					&& !possibleSuffix.equalsIgnoreCase("ST")
					&& !possibleSuffix.equalsIgnoreCase("B.T")) {
				return true;
			}
		} else {
			if (possibleSuffix.trim().equals("Ma")) {
				return false;
			}
			if (suffixList.contains(possibleSuffix.toLowerCase()) || checkWithoutParen(possibleSuffix.toLowerCase())) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Checks if the passed in possible suffix is valid for the passed in party name
	 * @param possibleSuffix - The suffix to check whether or not is valid
	 * @param partyName - The full name of the party to check if the suffix is valid for
	 * @return boolean - Whether or not possibleSuffix is a valid suffix for partyName
	 */
	public static boolean isSuffix(String possibleSuffix, String partyName) {
		return isSuffix(possibleSuffix.trim(), partyName.trim().split(",").length);
	}

	/**
	 * Remove parenthesis surrounding suffix if present.
	 * 
	 * @param possibleParenSuffix
	 * @return
	 */
	public static String checkAndRemoveParen(String possibleParenSuffix) {
		String returnString = possibleParenSuffix;
		if (StringUtils.isNotEmpty(possibleParenSuffix)) {
			if (possibleParenSuffix.contains("(") || possibleParenSuffix.contains(")")) {
				possibleParenSuffix = removeParentheses(possibleParenSuffix);
				if (suffixList.contains(possibleParenSuffix.toLowerCase())) {
					returnString = possibleParenSuffix;
				}
			}
		}
		return returnString;
	}

	/**
	 * We have seen examples where suffix comes surrounded with parentheses. remove parentheses before comparing.
	 * 
	 * @param possibleSuffix
	 * @return
	 */
	private static boolean checkWithoutParen(String possibleSuffix) {
		boolean returnFlag = false;

		if (StringUtils.isNotEmpty(possibleSuffix)) {
			if (possibleSuffix.contains("(") || possibleSuffix.contains(")")) {
				possibleSuffix = removeParentheses(possibleSuffix);
				if (suffixList.contains(possibleSuffix.toLowerCase())) {
					returnFlag = true;
				}
			}
		}
		return returnFlag;
	}

	/**
	 * Remove all the parentheses from string. Normalization utility does not normalize names with parenthesis for example. ROBERT A (JR) MCALLISTER should be normalize to ROBER A MCALLISTER, JR
	 * 
	 * @param possibleParenString
	 * @return
	 */
	private static String removeParentheses(String possibleParenString) {
		String removeParens = possibleParenString;
		if (StringUtils.isNotEmpty(possibleParenString)) {
			removeParens = removeParens.replace("(", "");
			removeParens = removeParens.replace(")", "");
		}
		return removeParens;
	}

	/**
	 * @param String
	 *            with unwanted parentheses, ie: Jackson, Samuel (BAMF)
	 * @return String with unwanted parentheses removed, ie: Jackson, Samuel
	 */
	public static String removeUnwantedParens(String name) {
		if (name == null)
			return null;
		
		String removeParens  = name.replaceAll("\\(.*?\\)", "");
		removeParens = removeParens.replaceAll("\\)", "");
		removeParens = removeParens.replaceAll("\\(", "");
		return removeParens.trim();
	}

	/**
	 * Do not normalize if these words are found.
	 * 
	 * @param Name
	 *            ie: ROBERT ABRAMS, ATTY. GENERAL
	 * @return True if you should not normalize
	 */
	public static boolean isSkip(String name) {
		if (name == null)
			return false;

		if(StringUtils.containsIgnoreCase(name, "AS ASSIGNEE OF")){
			return true;
		}
		
		String regex = new String("( ATTY| GENERAL| AKA|A/K/A|PRO SE|BAIL-BONDS|\\$[^#]|ORG\\.)");
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(name.toUpperCase());

		boolean temp = m.find();
		//Bug 196957
		if (temp)  //Event though the above is true, double check whether there is a actual name -- eg : DOUGLAS, COUNTY DISTRICT ATTY
		{
			Pattern patt1 = Pattern.compile("[a-z0-9]+,", Pattern.CASE_INSENSITIVE); //Check whether the name has text or number followed by comma
			Matcher m1 = patt1.matcher(name);
			if (m1.find())
			{
				temp = false;
			}
		}
		return temp;
	}

	private static String filtername(String fullname) {
		if (StringUtils.isEmpty(fullname))
			return fullname;
		String formattedFullName = fullname;
		if (fullname.startsWith("ESQ")) {
			formattedFullName = formattedFullName.replace("ESQ. ", "ESQ., ");
			formattedFullName = formattedFullName.replace("ESQAS ", "ESQAS,");
			formattedFullName = formattedFullName.replace("ESQL ", "ESQL, ");
			formattedFullName = formattedFullName.replace("ESQ ", "ESQ, ");
			formattedFullName = formattedFullName.replace("ESQ. -", "ESQ., ");
		}
		formattedFullName = formattedFullName.replace(" /", "");
		// removing the MR. sometimes leaves a trailing comma character. This removes that comma.
		if (fullname.endsWith(","))
			formattedFullName = StringUtils.removeEnd(fullname, ",");

		if (fullname.matches("^.*\\bAAG\\b.*$")) {
			formattedFullName = formattedFullName.replaceAll("[-\\s]+AAG", " AAG,");
			formattedFullName = formattedFullName.replaceAll("^AAG", "AAG,"); // starts with
		}
		formattedFullName = formattedFullName.replaceAll("LG$", ", LG");

		formattedFullName = formattedFullName.replaceAll("[-\\s]+OUT\\b", "");

		formattedFullName = formattedFullName.replaceAll("[-\\s]+EXECUTOR", " EXECUTOR");
		formattedFullName = formattedFullName.replaceAll("[-\\s]+REFEREE", " REFEREE");

		return formattedFullName;
	}

	public static String filterComment(String comment) {
		if (StringUtils.isEmpty(comment))
			return comment;
		String filteredComment = comment;
		String[] filterWords = { "^\\s*X\\s*$", "^\\s*x\\s*$" };

		for (String c : filterWords) {
			Pattern pattern = Pattern.compile(c);
			Matcher matcher = pattern.matcher(filteredComment);
			filteredComment = matcher.replaceAll("");
		}
		return filteredComment;
	}

	public static String SkipAttyInName(String name) {
		String nameSubString = name;
		if (!name.toUpperCase().contains("PROSECUTING ATTY") && (name.trim().toUpperCase().endsWith(" ATTY") || name.trim().toUpperCase().endsWith(" ATTY."))) {
			nameSubString = name.substring(0, name.length() - 5).trim();
		}
		return nameSubString;

	}

	public static boolean containsSuffix(String partyName){
		for (String suffix : suffixList) {
			if(partyName.matches("(?i)^.*?\\b" + Pattern.quote(suffix) + "(\\b|,).*?$")){
				return true;
			}
		}
		if(partyName.matches("(?i)^.*?\\bmr\\.?(\\b|,).*?$")) return true;
		return false;
	}
	
	public static String findSuffix(String partyName) {
		String name = "";
		for (String party : suffixList) {
			party = " " + party;
			if (StringUtils.endsWithIgnoreCase(partyName, party)) {
				int index = partyName.toLowerCase().indexOf(party);
				name = partyName.substring(index, index + party.length());
				if(isSuffix(name.trim(), partyName))
					return name;
				else 
					name = "";
			}
		}
		return name;
	}
	
	public static String fixPrefix(String partyName){
		String fixedPartyName = partyName;
		for (String prefix : prefixList) {
			if(!StringUtils.startsWithIgnoreCase(fixedPartyName, prefix) && StringUtils.containsIgnoreCase(fixedPartyName, prefix)){ 
				String prefixCaseSensitive = getPrefixFromName(prefix, fixedPartyName); //Protects the case of the name input
				String prefixEscaped = Pattern.quote(prefix);
				fixedPartyName = prefixCaseSensitive + " " + fixedPartyName.replaceAll("(?i)\\s" + prefixEscaped + "(\\s|$|.)", " ");
			}
		}
		return fixedPartyName.trim().replaceAll("(\\s){2,}", "$1");
	}
	
	private static String getPrefixFromName(String prefix, String name) {
		int startIndex = name.toUpperCase().indexOf(prefix.toUpperCase());
		int endIndex = startIndex + prefix.length();
		if(name.length()-1 < endIndex && startIndex > 0) {
			return name.substring(startIndex, endIndex);
		} else {
			return prefix;
		}
	}

	public static String findPrefix(String partyName) {
		String name = "";
		for (String prefix : prefixList) {
			if (StringUtils.startsWithIgnoreCase(partyName, prefix) || StringUtils.containsIgnoreCase(partyName, prefix)) {
				int index = partyName.toLowerCase().indexOf(prefix);
				name = partyName.substring(index, index + prefix.length());
			}
		}
		return name;
	}

	public static String checkMinorInPartyRole(String partyRoleName, String partyName) {
		if (StringUtils.isNotEmpty(partyRoleName) && StringUtils.isNotEmpty(partyName)) {
			if (StringUtils.equalsIgnoreCase(partyName, "MINORS COUNSEL") || StringUtils.equalsIgnoreCase(partyName, "MINOR'S COUNSEL")) {
				return partyName;
			} else if (StringUtils.containsIgnoreCase(partyRoleName, "MINOR")) {
				return "MINOR";
			} else if (StringUtils.contains(partyName, "(minor)")) {
				return "MINOR";
			}
		}
		return partyName;
	}

}

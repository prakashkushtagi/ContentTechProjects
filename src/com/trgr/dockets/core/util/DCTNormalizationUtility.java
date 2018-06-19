/**
 Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class DCTNormalizationUtility {

	//regex patterns
	public static final Pattern redactNameStatusREGEX = Pattern.compile(".*?(legal\\srepresentative(?:s)?\\s|as\\s+(?:natural\\s+)?mother\\s+of\\s|.*as\\s+(?:natural\\s+)?guardian\\s*(?:ad\\s+Litem\\s*)?|.*(?:a/n/f|next\\s?-?friend|\\bEstate|o/b/o|on\\sbehalf(?:\\sof)?|next\\sof\\skin\\sto)\\s+)?(.*for\\s|of\\s)?.*?(\\b(an?)?\\s*(Infant|Minor|Child)\\b(\\s?Child)?)(.*?\\b(?:by|age|deceased)\\b.*)?.*", Pattern.CASE_INSENSITIVE);
	
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
		list.add("esquire");
		list.add("pc");
		list.add("aka");
		list.add("a/k/a");
		list.add("phv");
		list.add("govt");
		list.add("ss");
		list.add("ausa");
		return list;
	}

	private static final List<String> prefixList = createPrefixList();

	private static List<String> createPrefixList() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("part 20");
		list.add("Dr.");
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
	 * If name has a suffix at the end and no comma between the name and the 
	 * suffix, then this adds a comma there.
	 * 
	 * @param name with out comma before suffix
	 * @return name with comma before suffix
	 */
	private static String addComma(String name) {
		String nameWithComma = name;
		if (StringUtils.isNotBlank(name) && (!name.contains(","))) {
			String suffix = findSuffix(name);
			if (StringUtils.isNotBlank(suffix) && name.endsWith(suffix)) {
				int number = name.lastIndexOf(suffix);
				String foundSuffix = name.substring(number);
				nameWithComma = nameWithComma.replace(foundSuffix, "");
				nameWithComma = nameWithComma.trim() + ", " + foundSuffix.trim();
			} else {
				nameWithComma = nameWithComma.trim();
			}
		}
		return nameWithComma;
	}

	/**
	 * @param name
	 *            in potential format of LAST, FIRST, MIDDLE, SUFFIX ie: CONNOR, MARTIN, E, JR.
	 * @return
	 */
	public static String lastMiddleFirstSuffix(String name) {
		if (StringUtils.isEmpty(name))
			return name;
		String formattedName = name;
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
				return normalizePartyName(formattedName, true);
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
	
	/**
	 * Prefixes that belong to a judge and should not be separated from the judge
	 */
	private static Strings judgePrefixes = new Strings(
		"Magistrate",
		"Senior",
		"Sr",
		"Sr.",
		"Mag",
		"Mag.",
		"U.S."
	);
	
	/**
	 * 
	 * @param name Name to normalize
	 * @param removeNumber Remove Number from name if number is only content.
	 * @param switchIfComma
	 * @return
	 */
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
		normalizedPartyName = removeNonASCIICharacters(normalizedPartyName);
		
		//Do not normalize if "$3,688 IN U S CURRENCY" (Dollar followed by number and comma and number)
		if (Pattern.compile("^\\$\\d+,\\s*\\d+").matcher(normalizedPartyName).find()) {
			return normalizedPartyName;
		}

		// name contains specific words like ATTY that means we just need to copy it as is.
		if (isSkip(name) || isFirm(name))
			return name;

		// Clean up of * and ?
		normalizedPartyName = normalizedPartyName.replaceAll("[?*:]", " ");
		
		if (removeNumber) {
			// Remove Number from name if number is only content.
			normalizedPartyName = removeNumberFromName(normalizedPartyName);
			if (StringUtils.isEmpty(normalizedPartyName))
				return normalizedPartyName;
		}

		normalizedPartyName = filtername(normalizedPartyName); // Strip extra text in the name. Bug 121227, 120756
		
		// do some other stuff for judges, they can have titles such as magistrate judge or senior judge
		if (StringUtils.containsIgnoreCase(name, "judge") && !StringUtils.containsIgnoreCase(normalizedPartyName, "US Magistrate Judge")) {
			String[] judgeParts = normalizedPartyName.split("(?i)judge");
			if (judgeParts.length == 2) { // if the judge has prefixes and a name
				String possibleJudgePrefixes = judgeParts[0].trim();
				String judgeName = judgeParts[1].trim();
				for (String possiblePrefix : possibleJudgePrefixes.split(" ")) {
					if (!judgePrefixes.contains(possiblePrefix)) {
						possibleJudgePrefixes = possibleJudgePrefixes.replaceFirst(Pattern.quote(possiblePrefix), "").trim(); // remove from possible prefixes
						judgeName = possiblePrefix + " " + judgeName; // restore the prefix to the judge name
					}
				}
				if (!StringUtils.isEmpty(possibleJudgePrefixes)) {
					return possibleJudgePrefixes + " Judge " + normalizePartyName(judgeName, removeNumber, switchIfComma);
				}
			}
		}
		

		// for name that does not contain a comma we just need to copy it as is.
		if (normalizedPartyName.contains(",") && switchIfComma) {

			String[] nameParts = normalizedPartyName.split(",");
			
			//for the name that contains more than 3 commas then we just need to copy as it is.			
			if(nameParts.length > 3){
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
					if(suffixes.trim().split("\\s+").length >=2){
						suffixes = suffixes.replaceAll("\\s+", ", ");
					} else if (suffixes.matches("(?i)\\s*(i{1,3}|iv)")) {
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

		
		// Check for a suffix at the beginning of the name
		normalizedPartyName = fixSuffix(normalizedPartyName);
		
		// If there isn't a space after a period, add one, unless ph.d is present
		if (!normalizedPartyName.toLowerCase().contains("ph.d") && !name.contains("$")) {
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
		for (Entry<String, String> mapping : mappingList) {
			name = name.replaceAll(mapping.getKey(), mapping.getValue());
		}
		return name;
	}

	/**
	 * Removes any special characters that might be present in the name.
	 * 
	 * @param name Name of Party.
	 * @return
	 */
	public static String removeNonASCIICharacters(String name) {
		if(StringUtils.isEmpty(name))
			return name;
		String removeNonAsciiFromString = name.replaceAll("[^\\x00-\\x7F]", "");
		return removeNonAsciiFromString;
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

		String normalizePartyName = normalizePartyName(name, removeNumber);

		return normalizePartyName.trim();
	}
	public static String normalizeNameWithTitle(String name, boolean removeNumber,boolean switchComma) {
		if (name == null || name.isEmpty()) {
			return name;
		}

		String normalizePartyName = normalizePartyName(name, removeNumber, switchComma);

		return normalizePartyName.trim();
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

		String normalizeJudgeName = normalizeNameWithTitle(name, removeNumber);
		normalizeJudgeName = normalizeJudgeName.trim();

		return normalizeJudgeName;
	}

	/**
	 * If the name sent to this method contains a group of parentheses that contains a suffix, said suffix will be extracted and put at the end of the
	 *         name (removing all instances of that parentheses group). <br/> <br/> If the name does not contain parentheses, or the name contains a group of parentheses that does not contain a suffix, then the value returned will be equal to the value
	 *         sent in.
	 * @param name Name that may have a suffix
	 * @return name with suffix content at end of string, assuming suffix comes inside parentheses.
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

		name = name.toUpperCase();
	
		String regex = new String("( LAW |FIRM|LEGAL|LAW OFFICE|INJURY LAWYER|" + "LLP|&| AND | OF |LLC|ASSOC|L\\.L\\.P|P\\.L\\.C|P\\.L\\.L\\.C|L\\.L\\.C|L\\.P\\.|\\$#AMP;|" + "P\\.A|P\\.C|PROFESSIONAL ASSOCIATION|PRO PER|IN PRO PER|[ ,]"
				+ "[ ,]CO\\.*[ ,]|[ ,]CO\\.*$|[ ,]INC\\.*|[ ,]INC$| STATE\\b|" + "[ ,]COMPANY|[ ,]*CORP(\\W|$)|[ ,]*CORPORATION|[ ,]INCORPORATED|INVESTMENT|" + "[ ,]DEPT|[ ,]DEPARTMENT|[ ,]ET\\.* AL\\.*|[ ,]ETAL\\.*|PUBLIC DEFENDER|CONFLICT|"				
				+ "[ ,]OFFICE|[ ,]LTD|[ ,]LIMITED|[ ,]PARTNERS|[ ,]COMPANIES|[ ,]FUND|" + "[ ,]ADJUSTMENT|[ ,]*ASSOCIATES|[ ,]LIABILITY|[ ,]INS\\.*|[ ,]INS$|[ ,]OFC\\.*|[ ,]OFC$"
				+ " TOWN |INSURANCE|[ ,]GROUP|[ ,]MANAGEMENT|[ ,]SERVICE|[ ,]DEPARTMENT|[ ,]PC\\.*$|~PC[,]|PC\\s-|\\bASSO\\b|\\bAPC\\b|\\bASSOCIATION\\b|\\bCHARTER\\b|\\bCHARTERED\\b|\\bLTD\\b|\\bPLC\\b|\\bPLLC\\b|[ ,]STATE |\\bBANK\\b|CITIBANK|\\bS\\.A\\.?\\b|\\bS\\.E\\.?\\b|\\bSA\\b|\\bSE\\b|\\bL\\.?P\\.?\\b)");
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(name);

		boolean firmFound = m.find();
		if (firmFound && name.contains("CO") && name.contains("ATTY")) {
			// CO. (not followed by ATTY)
			// CO (not followed by ATTY)

			String regexFollowedBy = new String("CO[., ]ATTY");
			Pattern p2 = Pattern.compile(regexFollowedBy);
			Matcher m2 = p2.matcher(name);
			boolean isCountyAttorney = m2.find();

			if (isCountyAttorney){ // if it is followed by ATTY then is not a firm but a county attorney
				firmFound = false;
			}
		}else if (firmFound && name.contains("STATE")) {
			//state followed by street/prison was being tagged as a firm and shouldn't be
			String regexStreet = new String("STATE\\s+(STREET|ROAD|AVE|AVENUE)");
			Pattern p3 = Pattern.compile(regexStreet);
			Matcher m3 = p3.matcher(name);
			boolean isStreet = m3.find();
			if (isStreet){
				firmFound = false;
			}
		}else if(firmFound && name.contains("POST OFFICE")){
			firmFound = false;
		}else if(firmFound && (name.contains("PRO SE") || name.matches(".*?\\bPROSE\\b.*?") || name.contains("PRO-SE"))){
			firmFound = false;
		}
		
		// try to detect if it is part of a human name
		// look for a lastName, firstName middleInitial format
		Pattern lfm = Pattern.compile("\\w+,\\s*\\w+\\s+\\w");
		Matcher nameMatcher = lfm.matcher(name);
		if (firmFound && nameMatcher.matches()) {
			firmFound = false;
		}

		return firmFound;
	}

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
		String removedNumFromName = name;
		String phnRegex = "\\d*\\s*[-\\(]*\\d{3}.\\d{3}.\\d{4}";

		Pattern phn = Pattern.compile(phnRegex);
		Matcher pm = phn.matcher(removedNumFromName);
		if (pm.find()) {
			// Need to escape regex special characters if we're going to use the result as regex
			String groupText = pm.group().replaceAll("([\\(\\)\\.\\?\\*\\\\\\+\\{\\}\\[\\]\\^\\$])", "\\\\$1");
			return removedNumFromName.replaceAll(groupText, "");
		} else if (removedNumFromName.replaceAll("\\s", "").matches("\\d+")) {
			return removedNumFromName.replaceAll("\\d+", "");
		}
		return removedNumFromName;

	}

	public static String stripComment(String address) {
		if (StringUtils.isNotEmpty(address)) {
			String formatedAddress = address;
			if (formatedAddress.toUpperCase().contains("SEE COMMENT SCREEN FOR NAME") || formatedAddress.toUpperCase().contains("ADDRESS & PHONE") || formatedAddress.toUpperCase().contains("ADDRESS &AMP; PHONE") || formatedAddress.toUpperCase().contains("ADDRESS $#AMP; PHONE")) {
				formatedAddress = "";
			} else {
				formatedAddress = formatedAddress.replaceAll("\\s+", " ");
			}

			if (formatedAddress.matches("\\W*")) {
				formatedAddress = "";
			}

			return formatedAddress.trim();
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
			if (possibleSuffix.length() > 1 && !possibleSuffix.contains(" ") && numOfWords > 2 && !possibleSuffix.equalsIgnoreCase("ST") && !possibleSuffix.equalsIgnoreCase("B.T")) {
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
		
		String removeParens = name.replaceAll("\\(.*?\\)", "");
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

		String regex = new String("( ATTY| GENERAL| AKA|A/K/A|PRO SE|BAIL-BONDS|\\$|ORG\\.)");
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

	private static String filtername(String fullName) {
		if (StringUtils.isEmpty(fullName))
			return fullName;
		String filteredFullName = fullName;
		if (filteredFullName.startsWith("ESQ")) {
			filteredFullName = filteredFullName.replace("ESQ. ", "ESQ., ");
			filteredFullName = filteredFullName.replace("ESQAS ", "ESQAS,");
			filteredFullName = filteredFullName.replace("ESQL ", "ESQL, ");
			filteredFullName = filteredFullName.replace("ESQ ", "ESQ, ");
			filteredFullName = filteredFullName.replace("ESQ. -", "ESQ., ");
		}
		filteredFullName = filteredFullName.replace(" /", "");
		// removing the MR. sometimes leaves a trailing comma character. This removes that comma.
		if (filteredFullName.endsWith(","))
			filteredFullName = StringUtils.removeEnd(filteredFullName, ",");

		if (filteredFullName.matches("^.*\\bAAG\\b.*$")) {
			filteredFullName = filteredFullName.replaceAll("[-\\s]+AAG", " AAG,");
			filteredFullName = filteredFullName.replaceAll("^AAG", "AAG,"); // starts with
		}
		filteredFullName = filteredFullName.replaceAll("LG$", ", LG");

		filteredFullName = filteredFullName.replaceAll("[-\\s]+OUT\\b", "");

		filteredFullName = filteredFullName.replaceAll("[-\\s]+EXECUTOR", " EXECUTOR");
		filteredFullName = filteredFullName.replaceAll("[-\\s]+REFEREE", " REFEREE");

		return filteredFullName;
	}

	public static String filterComment(String comment) {
		String filteredComment = comment;
		if (StringUtils.isEmpty(comment))
			return comment;

		String[] filterWords = { "^\\s*X\\s*$", "^\\s*x\\s*$" };

		for (String c : filterWords) {
			Pattern pattern = Pattern.compile(c);
			Matcher matcher = pattern.matcher(filteredComment);
			filteredComment = matcher.replaceAll("");
		}
		return filteredComment;
	}

	public static String SkipAttyInName(String name) {
		String  formattedName = name;
		if (!name.toUpperCase().contains("PROSECUTING ATTY") && (name.trim().toUpperCase().endsWith(" ATTY") || name.trim().toUpperCase().endsWith(" ATTY."))) {
			formattedName = formattedName.substring(0, name.length() - 5).trim();
		}
		return formattedName;

	}

	/**
	 * Returns the suffix from the end of the name if one is present
	 * @param name Name with possible suffix
	 * @return Returns the suffix that was found or an empty String
	 */
	public static String findSuffix(String partyName) {
		String name = "";
		for (String party : suffixList) {
			party = " " + party;
			if (StringUtils.endsWithIgnoreCase(partyName, party)) {
				int index = partyName.toLowerCase().indexOf(party);
				name = partyName.substring(index, index + party.length());
				return name;
			}
		}
		return name;
	}
	
	/**
	 * Moves a suffix from the beginning of the name to the end of the name
	 * @author <a href="mailto:tyler.studanski@thomsonreuters.com">Tyler Studanski</a> <b>u0162605</b>
	 * @param name Name to fix
	 * @return Returns the name with the suffix at the end if the name starts with a suffix, otherwise just returns the name.
	 */
	public static String fixSuffix(String name) {
		String fixedSuffix = name;
		for (String suffix : suffixList) {
			String regexVersion = Pattern.quote(suffix);
			Pattern p = Pattern.compile("^(" + regexVersion + ")(?:[^\\w])(.+)", Pattern.CASE_INSENSITIVE);
			Matcher result = p.matcher(fixedSuffix);
			if(result.find()){
				return result.group(2).trim() + " " + result.group(1);
			}
		}
		return fixedSuffix;
	}
	
	public static String fixPrefix(String partyName){
		String fixedPartyName = partyName;
		for (String prefix : prefixList) {
			if(!StringUtils.startsWithIgnoreCase(partyName, prefix) && StringUtils.containsIgnoreCase(partyName, prefix)){
				String prefixEscaped = Pattern.quote(prefix);
				fixedPartyName = prefix + " " + fixedPartyName.replaceAll("\\s" + prefixEscaped + "(\\s|$|.)", " ");
			}
		}
		return fixedPartyName.trim().replaceAll("(\\s){2,}", "$1");
	}
	
	/**
	 * If a prefix is in the partyName then it will return that prefix
	 * @param partyName Party name with possible prefix
	 * @return Returns just the prefix that was found otherwise an empty String.
	 */
	public static String findPrefix(String partyName) {
		String name = "";
		for (String prefix : prefixList) {
			if (StringUtils.startsWithIgnoreCase(partyName, prefix) || StringUtils.containsIgnoreCase(partyName, prefix)) {
				int index = partyName.toLowerCase().indexOf(prefix.toLowerCase());
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
	
	//normalization step for names that come in as statuses and statuses
	//in name: [name] as Guardian ad Litem for [name], a minor and [name], a minor >>--> [name] as Guardian ad Litem for a minor
	//in status: as natural mother of a minor child >>--> as natural mother of a minor child
	//in status: as natural mother of [name] a minor child >>--> as natural mother of a minor child
	public static String redactNameStatus(String namestatus){
		return (redactNameStatusREGEX.matcher(namestatus).replaceAll("$1$2$3$7")).replaceAll("\\s+", " ").trim();
	}

}

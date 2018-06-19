/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.Platform;
import com.trgr.dockets.core.exception.ContentParserException;
import com.ximpleware.AutoPilot;
import com.ximpleware.FastLongBuffer;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

public class SourceContentVtdParser 
{

	private static Logger logger = Logger.getLogger(SourceContentVtdParser.class);
	private static Pattern scrapeDatePattern = Pattern.compile("<scrape.date>(.*?)<\\/scrape.date>");
	private static Pattern legacyIdPattern = Pattern.compile("<legacy.id>(.*?)<\\/legacy.id>");
	private static Pattern courtFromJurisAbbrevPattern = Pattern.compile("<md.jurisabbrev>(.*?)<\\/md.jurisabbrev>");
	
	public String prepareDocketNumber(String unFormattedDocketNumber) 
	{
		String docketNumber = null;
		if(unFormattedDocketNumber.contains(":"))
		{
			String str[]=unFormattedDocketNumber.split("\\:");
			int year=Integer.parseInt(str[1].substring(0,2));
			if (year>50)
			{
				year= year +1900;
			}
			else
			{
				year =year+2000;
			}		

			docketNumber = str[0].replaceAll("[A-Z]+-[A-Z]+","")+":"+year+str[1].replaceAll("^\\d{2}", "");
			
		}
		else{
			String str[]=unFormattedDocketNumber.split("[Bb][Kk][Rr]");
			int year=Integer.parseInt(str[1].substring(0,2));
			if (year>50)
			{
				year= year +1900;
			}
			else
			{
				year =year+2000;
			}
			docketNumber =year+str[1].replaceAll("^\\d{2}", "");
		}

		return docketNumber.toLowerCase();
	}
	
	/**
	 * This method returns the file name, given the file path.
	 * 
	 * @param filePath
	 * @return fileName
	 * @throws KCCMLFSException 
	 * @throws KCCMLFSException
	 */
	public void process( String filePath, String destinationFilePath,String novusDocumentSeparator) throws ContentParserException
	{
		VTDGen vg = null;
		VTDNav vn = null;
		AutoPilot ap = null;
		byte[] totalXMLContent = null;
		FastLongBuffer flb = null;

		int invalidDocCount = 0;


		List<String> keyList = new ArrayList<String>();
		FileOutputStream fos = null;

		vg = new VTDGen();
		if (vg.parseFile(filePath, false))
		{
			logger.debug("Successfully parsed the content file. ");
			vn = vg.getNav();
			ap = new AutoPilot(vn);

			try
			{
				ap.selectXPath("//" + novusDocumentSeparator);

				totalXMLContent = vn.getXML().getBytes();
				flb = new FastLongBuffer(4);

				while (ap.evalXPath() != -1)
				{
					logger.debug("xpath (" + novusDocumentSeparator + ") matched");
					if (vn.getAttrCount() > 0)
					{

						long l = vn.getElementFragment();
						int len = (int) (l >> 32);
						int offset = (int) l;
						String nestedXmlFragment = new String(totalXMLContent, offset, len);
						Matcher docketNumMatcher = legacyIdPattern.matcher(nestedXmlFragment);
						Matcher scrapeDateMatcher = scrapeDatePattern.matcher(nestedXmlFragment);
						Matcher courtFromJurisAbbrevMatcher = courtFromJurisAbbrevPattern.matcher(nestedXmlFragment);

						String docketNumberUnformatted = null;
						String scrapeDate = null;
						String courtFromJurisAbbrev = null;
						if (docketNumMatcher.find() && scrapeDateMatcher.find() && courtFromJurisAbbrevMatcher.find())//nestedXmlFragment.contains("<md.citator>"))
						{
							docketNumberUnformatted = docketNumMatcher.group(1);
							scrapeDate = scrapeDateMatcher.group(1);
							courtFromJurisAbbrev = courtFromJurisAbbrevMatcher.group(1);
							
							
							keyList.add(courtFromJurisAbbrev+ "_" + prepareDocketNumber(docketNumberUnformatted) +"_" + scrapeDate);
							flb.append(vn.getElementFragment());
						}
						else
						{
							invalidDocCount = invalidDocCount + 1;
							logger.error("Error Processing Document moving to the next document in the input file " + filePath
							        + "- Either docketNumberUnformatted is null or scrapeDate is null  or  courtFromJurisAbbrev serialNumber:: " + docketNumberUnformatted + " scrapeDate:: " + scrapeDate+ " courtFromJurisAbbrev:: " + courtFromJurisAbbrev);
						}
					}
					else
					{
						invalidDocCount = invalidDocCount + 1;
						logger.error("Error Processing Document moving to the next document in the input file - " + filePath);
					}

				}//while   

				for (int k = 0; k < flb.size(); k++)
				{
					String fileName = ""+(k+1)+"_"+keyList.get(k) + "_"+ Platform.NOVUSXML.toString() +"_update.scl.split.xml";
					String[] fs = keyList.get(k).split("_");
					String dNum = fs[1];

						String documentFilePath = null;
						try
						{
							String fileLocation = destinationFilePath;//getFileLocation(filePath);
							String backslash = System.getProperty("file.separator");
							if(backslash.equals("\\"))
							{
								fileLocation = fileLocation.replace(backslash, "/");
								fileName = fileName.replace(":", "%3A");
							}
							
							if (!StringUtils.isEmpty(fileLocation))
							{
								documentFilePath = fileLocation + fileName;
							}
							else
							{
								logger.error(this.getClass().getName() + " fileLocation or docId is null : ");
								continue;
							}
							File fo = new File(documentFilePath);
							fos = new FileOutputStream(fo);
							fos.write(totalXMLContent, flb.lower32At(k), flb.upper32At(k));
							fos.flush();

						}
						catch (IOException ioe1)
						{
							logger.error(" Exception occurred while processing the document with docketnum : " + dNum, ioe1);
							continue;
						}
						finally
						{
							try
							{
								if (fos != null)
								{
									fos.close();
								}
							}
							catch (IOException e)
							{
								logger.error(" Exception occurred while closing the FileOutputStream", e);
								continue;
							}
						}

				}
			}
			catch (Exception e)
			{
				logger.error("Error parsing " + filePath);
				throw new ContentParserException("Exception encountered while executing splitDcouments ", e);
			}

		}
		return ;
	}

}

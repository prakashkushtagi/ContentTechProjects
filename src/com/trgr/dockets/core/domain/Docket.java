/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:mahendra.survase@thomsonreuters.com">Mahendra Survase</a> u0105927 
 *
 */
@XmlRootElement
public class Docket {

	private String number;
	private String court;
	private String county;
	private String platform;
	private String scrapeDate;
	private String acquireDate;
	private String statePostal;
	private String encoding;
	private List<Page> pageList;
	private String errorMessage;
	
	public Docket() {
		super();
	}
	public String getNumber() {
		return number;
	}
	@XmlAttribute(name="number",required=true)
	public void setNumber(String number) {
		this.number = number;
	}


	public String getCourt() {
		return court;
	}
	@XmlAttribute(name="court",required=true)
	public void setCourt(String court) {
		this.court = court;
	}
	public String getPlatform() {
		return platform;
	}
	
	public String getCounty() {
		return county;
	}
	
	@XmlAttribute(name="county")
	public void setCounty(String county) {
		this.county = county;
	}

	@XmlAttribute(name="platform",required=true)
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getScrapeDate() {
		return scrapeDate;
	}

	@XmlAttribute(name="scrape.date",required=true)
	public void setScrapeDate(String scrapeDate) {
		this.scrapeDate = scrapeDate;
	}
	public String getAcquireDate() {
		return acquireDate;
	}

	@XmlAttribute(name="acquire.date")
	public void setAcquireDate(String acquireDate) {
		this.acquireDate = acquireDate;
	}
	public String getStatePostal() {
		return statePostal;
	}

	@XmlAttribute(name="state.postal",required=true)
	public void setStatePostal(String statePostal) {
		this.statePostal = statePostal;
	}

	public String getEncoding() {
		return encoding;
	}
	
	@XmlAttribute
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	public List<Page> getPageList() {
		return pageList;
	}
	
	@XmlElement(name="page",required = true)
	public void setPageList(List<Page> pageList) {
		this.pageList = pageList;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((acquireDate == null) ? 0 : acquireDate.hashCode());
		result = prime * result + ((county == null) ? 0 : county.hashCode());
		result = prime * result + ((court == null) ? 0 : court.hashCode());
		result = prime * result
				+ ((encoding == null) ? 0 : encoding.hashCode());
		result = prime * result
				+ ((errorMessage == null) ? 0 : errorMessage.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result
				+ ((pageList == null) ? 0 : pageList.hashCode());
		result = prime * result
				+ ((platform == null) ? 0 : platform.hashCode());
		result = prime * result
				+ ((scrapeDate == null) ? 0 : scrapeDate.hashCode());
		result = prime * result
				+ ((statePostal == null) ? 0 : statePostal.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Docket other = (Docket) obj;
		if (acquireDate == null) {
			if (other.acquireDate != null)
				return false;
		} else if (!acquireDate.equals(other.acquireDate))
			return false;
		if (county == null) {
			if (other.county != null)
				return false;
		} else if (!county.equals(other.county))
			return false;
		if (court == null) {
			if (other.court != null)
				return false;
		} else if (!court.equals(other.court))
			return false;
		if (encoding == null) {
			if (other.encoding != null)
				return false;
		} else if (!encoding.equals(other.encoding))
			return false;
		if (errorMessage == null) {
			if (other.errorMessage != null)
				return false;
		} else if (!errorMessage.equals(other.errorMessage))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (pageList == null) {
			if (other.pageList != null)
				return false;
		} else if (!pageList.equals(other.pageList))
			return false;
		if (platform == null) {
			if (other.platform != null)
				return false;
		} else if (!platform.equals(other.platform))
			return false;
		if (scrapeDate == null) {
			if (other.scrapeDate != null)
				return false;
		} else if (!scrapeDate.equals(other.scrapeDate))
			return false;
		if (statePostal == null) {
			if (other.statePostal != null)
				return false;
		} else if (!statePostal.equals(other.statePostal))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Docket [number=" + number + ", court=" + court + ", platform="
				+ platform + ", scrapeDate=" + scrapeDate + ", acquireDate="
				+ acquireDate + ", statePostal=" + statePostal + ", encoding="
				+ encoding + ", pageList=" + pageList + ", errorMessage="
				+ errorMessage + ", county=" + county + "]";
	}
	public String formatToXml() {
		StringBuilder docketXml = new StringBuilder("<docket");
		
		//Add attributes
		docketXml.append(addAttribute("number", number));
		docketXml.append(addAttribute("court", court));
		docketXml.append(addAttribute("county", county));		
		docketXml.append(addAttribute("platform", platform));
		docketXml.append(addAttribute("scrape.date", scrapeDate));
		docketXml.append(addAttribute("acquire.date", acquireDate));
		docketXml.append(addAttribute("state.postal", statePostal));
		docketXml.append(addAttribute("encoding", encoding));	
		docketXml.append(">");
		
		//Add pages.
		//for NY courts, we need to escape few characters before we insert them to the db.
		if(court.equalsIgnoreCase("N_DNYDOWNSTATE") || court.equalsIgnoreCase("N_DNYUPSTATE") 
				|| court.equalsIgnoreCase("N_DNYCNTYCLERK"))
		{
			for (Page page : pageList) {
				String escapedDocketContent = xmlEscape(page.getDocketContent());
				docketXml.append("\n");
				docketXml.append("<page name=\"").append(page.getName()).append("\">");
				docketXml.append(escapedDocketContent);
				docketXml.append("</page>");
				docketXml.append("\n");
			}
		}
		else
		{
			for (Page page : pageList) {
				docketXml.append("\n");
				docketXml.append("<page name=\"").append(page.getName()).append("\">");
				docketXml.append(page.getDocketContent());
				docketXml.append("</page>");
				docketXml.append("\n");
			}
		}	
		docketXml.append("</docket>");
		return docketXml.toString();
	}
	private String addAttribute(String name, String value) {
		if (value == null || value.trim().equals("")) {
			return "";
		}
		return " " + name + "=\"" + value + "\"";
	}
	
	/**
	 * escape out the 5 xml escape characters.  When we upgrade commons-lang to 3.0 we can use String
	 * @param ch
	 * @return
	 */
    public String xmlEscape(String str) 
    {
        if (str != null) 
        {
               str = str.replace("&", "&amp;");
               str = str.replace("<", "&lt;");
               str = str.replace("\"", "&quot;");
               str = str.replace("'", "&apos;");
               str = str.replace(">", "&gt;");
        }
        
        return str;
    }
	

	
	
	
		
}

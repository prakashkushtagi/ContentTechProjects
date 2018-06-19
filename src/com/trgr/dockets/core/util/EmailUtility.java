/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.util;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * @author C047166
 *
 */
public class EmailUtility
{
	public static void sendEmail(String hostName, String fromAddr, String toAddr, String subject, String body) throws EmailException{
		Email email = new SimpleEmail();
		email.setHostName(hostName);
		email.setFrom(fromAddr);
		email.setSubject(subject);
		email.setMsg(body);
		email.addTo(toAddr);
		email.send();
	}
	
	public static void sendEmail(String hostName, String fromAddr, String toAddr[], String subject, String body) throws EmailException{
		Email email = new SimpleEmail();
		email.setHostName(hostName);
		email.setFrom(fromAddr);
		email.setSubject(subject);
		email.setMsg(body);
		email.addTo(toAddr);
		email.send();
	}
	
public static boolean isValidEmailAddress(String email) {
	   boolean result = true;
	   try {
	      InternetAddress emailAddr = new InternetAddress(email);
	      emailAddr.validate();
	   } catch (AddressException ex) {
	      result = false;
	   }
	   return result;
	}
}

/*
 * Copyright 2013: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.trgr.dockets.core.domain.NotificationInfo;
import com.trgr.dockets.core.entity.NotificationRequest;
import com.trgr.dockets.core.entity.NotificationTypeEvent;
import com.trgr.dockets.core.entity.PublishingRequest;


public class NotificationServiceUtil {
	
	public static NotificationRequest createNotificationForNotificationTypes(NotificationTypeEvent notificationTypeEvent, 
			String notificationGroupString, PublishingRequest pubReq, String batchId, String errorCode, String errorDesc, String env)	
	{
		/* Variables (Search and Replace) come from 
		1)	Operational Data (Job/Step Execution variables
		2)	Request Records
		3)	Product/Court Records
		4)	Other as requirements expand
		*/
			String bodyTemplate = notificationTypeEvent.getBody();
			String subjectTemplate = notificationTypeEvent.getSubject();
			String notificationGroupList = notificationGroupString.replaceAll("\\[\\{GROUP_EMAIL=", "").replaceAll("\\}\\]", "").replaceAll("\\}", "").replaceAll("\\{", "")
																			.replaceAll("GROUP_EMAIL=", "");
			
			Map<String,String> tokens = new HashMap<String,String>();
			tokens.put("PRODUCT", pubReq.getProduct().getDisplayName());
			tokens.put("REQUEST_NAME", pubReq.getRequestName());
			tokens.put("BATCH_ID", batchId);
			tokens.put("REQUEST_OWNER", pubReq.getRequestOwner());
			tokens.put("ERROR_CODE", errorCode);
			tokens.put("ERROR_DESCRIPTION", errorDesc);
			tokens.put("ENV", env.toUpperCase());
			tokens.put("TIME", new Date().toString());
			
			// Create pattern of the format "%(REQUEST_NAME|ERROR_CODE|ERROR_DESCRIPTION|etc)%"
			String patternString = "%(" + StringUtils.join(tokens.keySet(), "|") + ")%";
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(subjectTemplate);
			Matcher matcher1 = pattern.matcher(bodyTemplate);

			StringBuffer subject = new StringBuffer();
			while(matcher.find()) {
			    matcher.appendReplacement(subject, tokens.get(matcher.group(1)));
			}
			matcher.appendTail(subject);

			StringBuffer body = new StringBuffer();
			while(matcher1.find()) {
				matcher1.appendReplacement(body, tokens.get(matcher1.group(1)));
			}
			matcher1.appendTail(body);
			
			NotificationRequest notificationRequest = new NotificationRequest();
			notificationRequest.setNotificationGroup(notificationGroupList);
			notificationRequest.setSubject(subject.toString());
			notificationRequest.setBody(body.toString());
			notificationRequest.setCreatedTime(new Date());
			return notificationRequest;
			
	}
	public static NotificationRequest createNotificationForNotificationTypes(NotificationInfo notificationInfo)	
	{
		/* Variables (Search and Replace) come from 
		1)	Operational Data (Job/Step Execution variables
		2)	Request Records
		3)	Product/Court Records
		4)	Other as requirements expand
		*/
			String bodyTemplate = notificationInfo.getNotificationTypeEvent().getBody();
			String subjectTemplate = notificationInfo.getNotificationTypeEvent().getSubject();
			String notificationGroupList = notificationInfo.getNotificationGroupString().replaceAll("\\[\\{GROUP_EMAIL=", "").replaceAll("\\}\\]", "").replaceAll("\\}", "").replaceAll("\\{", "")
																			.replaceAll("GROUP_EMAIL=", "");
			
			List<String> batchWithProcessErrs = notificationInfo.getBatchWithProcessErr();
		
			
			Map<String,String> tokens = new HashMap<String,String>();
			if (notificationInfo.getPubReq().getProduct().getDisplayName() != null) {
				tokens.put("PRODUCT", notificationInfo.getPubReq().getProduct().getDisplayName());
			}
			if (notificationInfo.getPubReq().getCourt() != null && notificationInfo.getPubReq().getCourt().getCourtCluster() != null) {
				tokens.put("COURT", notificationInfo.getPubReq().getCourt().getCourtCluster());
			}else{
				tokens.put("COURT", "DCT");
			}
			if (notificationInfo.getMergeFileName() != null) {
				tokens.put("MERGE_FILE_NAME", notificationInfo.getMergeFileName());
			}
			if (notificationInfo.getReceiptId() != null) {
				tokens.put("RECEIPT_ID", notificationInfo.getReceiptId());
			}
			
			if (notificationInfo.getPubReq().getRequestName() != null) {
				tokens.put("REQUEST_NAME", notificationInfo.getPubReq().getRequestName());
			}
			if (notificationInfo.getBatchId() != null) {
				tokens.put("BATCH_ID", notificationInfo.getBatchId());
			}
			if (notificationInfo.getPubReq().getRequestOwner() != null) {
				tokens.put("REQUEST_OWNER", notificationInfo.getPubReq().getRequestOwner());
			}
			if (notificationInfo.getErrorCode() != null ) {
				tokens.put("ERROR_CODE", notificationInfo.getErrorCode());
			}
			if (notificationInfo.getErrorDesc() != null) {
				tokens.put("ERROR_DESCRIPTION", notificationInfo.getErrorDesc());
			}
			if(batchWithProcessErrs!= null && !batchWithProcessErrs.isEmpty())
			{				
				StringBuilder finalBatchErrors = new StringBuilder();
				for(String err : batchWithProcessErrs)
				{
					finalBatchErrors.append(err);
					finalBatchErrors.append("\n");			
				}	
				tokens.put("BATCH_ERRORS", finalBatchErrors.toString());
			}
			if (notificationInfo.getEnv() != null) {
				tokens.put("ENV", notificationInfo.getEnv().toUpperCase());
			}
			if (notificationInfo.getFTPFile() != null)
			{
				tokens.put("FTP_FILE", notificationInfo.getFTPFile());
			}
			if (notificationInfo.getStatusName() != null)
			{
				tokens.put("STATUS", notificationInfo.getStatusName().toString());
			}
			if(notificationInfo.getCollectionName() != null)
			{
				tokens.put("COLLECTION_NAME", notificationInfo.getCollectionName());
				
			}if(notificationInfo.getPubId() != null)
			{
				tokens.put("PUBLICATION_ID", notificationInfo.getPubId());
			}if(notificationInfo.getMetaDocInputFile() != null)
			{
				tokens.put("METADOC_INPUT_FILE", notificationInfo.getMetaDocInputFile());
			}
			tokens.put("TIME", new Date().toString());

			
			// Create pattern of the format "%(REQUEST_NAME|ERROR_CODE|ERROR_DESCRIPTION|etc)%"
			String patternString = "%(" + StringUtils.join(tokens.keySet(), "|") + ")%";
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(subjectTemplate);
			Matcher matcher1 = pattern.matcher(bodyTemplate);

			StringBuffer subject = new StringBuffer();
			while(matcher.find()) {
			    matcher.appendReplacement(subject, tokens.get(matcher.group(1)));
			}
			matcher.appendTail(subject);

			StringBuffer body = new StringBuffer();
			while(matcher1.find()) {
				matcher1.appendReplacement(body, tokens.get(matcher1.group(1)));
			}
			matcher1.appendTail(body);
			
			NotificationRequest notificationRequest = new NotificationRequest();
			notificationRequest.setNotificationGroup(notificationGroupList);
			notificationRequest.setSubject(subject.toString());
			notificationRequest.setBody(body.toString());
			notificationRequest.setCreatedTime(new Date());
			return notificationRequest;
			
	}
	
	public static NotificationRequest createNotificationForNotificationDcTypes(NotificationInfo notificationInfo)	
	{
		/* Variables (Search and Replace) come from 
		1)	Operational Data (Job/Step Execution variables
		2)	Request Records
		3)	Product/Court Records
		4)	Other as requirements expand
		*/
			String bodyTemplate = notificationInfo.getNotificationTypeEvent().getBody();
			String subjectTemplate = notificationInfo.getNotificationTypeEvent().getSubject();
			String notificationGroupList = notificationInfo.getNotificationGroupString().replaceAll("\\[\\{GROUP_EMAIL=", "").replaceAll("\\}\\]", "").replaceAll("\\}", "").replaceAll("\\{", "")
																			.replaceAll("GROUP_EMAIL=", "");
			
			List<String> batchWithProcessErrs = notificationInfo.getBatchWithProcessErr();
		
			
			Map<String,String> tokens = new HashMap<String,String>();
			if (notificationInfo.getDcReq().getRequestName() != null) {
				tokens.put("REQUEST_NAME", notificationInfo.getDcReq().getRequestName());
			}
			if (notificationInfo.getDcReq().getCourtName() != null) {
				tokens.put("COURT", notificationInfo.getDcReq().getCourtName());
			}
			if (notificationInfo.getBatchId() != null) {
				tokens.put("BATCH_ID", notificationInfo.getBatchId());
			}
			if (notificationInfo.getDcReq().getDcProvider() != null) {
				tokens.put("REQUEST_OWNER", notificationInfo.getDcReq().getDcProvider());
			}
			if (notificationInfo.getErrorCode() != null ) {
				tokens.put("ERROR_CODE", notificationInfo.getErrorCode());
			}
			if (notificationInfo.getErrorDesc() != null) {
				tokens.put("ERROR_DESCRIPTION", notificationInfo.getErrorDesc());
			}
			if(batchWithProcessErrs!= null && !batchWithProcessErrs.isEmpty())
			{				
				StringBuilder finalBatchErrors = new StringBuilder();
				for(String err : batchWithProcessErrs)
				{
					finalBatchErrors.append(err);
					finalBatchErrors.append("\n");			
				}	
				tokens.put("BATCH_ERRORS", finalBatchErrors.toString());
			}
			if (notificationInfo.getEnv() != null) {
				tokens.put("ENV", notificationInfo.getEnv().toUpperCase());
			}
			if (notificationInfo.getFTPFile() != null)
			{
				tokens.put("FTP_FILE", notificationInfo.getFTPFile());
			}
			if (notificationInfo.getStatusName() != null)
			{
				tokens.put("STATUS", notificationInfo.getStatusName().toString());
			}
			tokens.put("TIME", new Date().toString());

			
			// Create pattern of the format "%(REQUEST_NAME|ERROR_CODE|ERROR_DESCRIPTION|etc)%"
			String patternString = "%(" + StringUtils.join(tokens.keySet(), "|") + ")%";
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(subjectTemplate);
			Matcher matcher1 = pattern.matcher(bodyTemplate);

			StringBuffer subject = new StringBuffer();
			while(matcher.find()) {
			    matcher.appendReplacement(subject, tokens.get(matcher.group(1)));
			}
			matcher.appendTail(subject);

			StringBuffer body = new StringBuffer();
			while(matcher1.find()) {
				matcher1.appendReplacement(body, tokens.get(matcher1.group(1)));
			}
			matcher1.appendTail(body);
			
			NotificationRequest notificationRequest = new NotificationRequest();
			notificationRequest.setNotificationGroup(notificationGroupList);
			notificationRequest.setSubject(subject.toString());
			notificationRequest.setBody(body.toString());
			notificationRequest.setCreatedTime(new Date());
			return notificationRequest;
	}
}



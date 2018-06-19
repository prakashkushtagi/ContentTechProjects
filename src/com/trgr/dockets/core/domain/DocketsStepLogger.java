/**
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved. Proprietary and Confidential information of TRGR. Disclosure, Use or Reproduction without the written authorization of TRGR is prohibited.
 * ********************************************************************
 *  PROPERTY OF WEST, A THOMSON BUSINESS
 *  
 *  COMPANY CONFIDENTIAL 
 *  
 *  "UNPUBLISHED--ALL RIGHTS RESERVED UNDER THE COPYRIGHT LAWS."
 *  "THE INFORMATION CONTAINED HEREIN IS PROPRIETARY, TRADE SECRET
 *  INFORMATION. ACCESS TO SAID INFORMATION IS STRICTLY LIMITED 
 *  TO SPECIFICALLY AUTHORIZED PERSONNEL. SAID INFORMATION 
 *  SHALL BE KEPT STRICTLY CONFIDENTIAL AND SECRET."
 *  
 *  Name        : ConversionLogger.java
 *  @authors    : Tom Manion (C144403)
 *  @Copyright 2012 West, A Thomson Business
 *  Purpose     : Create an object that handles the majority of the background logic for the business logging process.
 *  Notes       : Created 8/7/2012
 *  
 ***********************************************************************  
 */
package com.trgr.dockets.core.domain;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class DocketsStepLogger {
	Logger LOG = Logger.getLogger(getClass());

	private File errorFile;
	private File logFile;
	private String logFileName;
	private String errorFileName;
	private File logDir;
	private File errorDir;


	/**
	 * Constructs a step logger with the provided log file names.
	 * This constructor is used when JobDirParams is available.
	 * 
	 * @param jobDirParams
	 * @param logFileName
	 * @param errorFileName
	 * @throws IOException
	 */
	public DocketsStepLogger(JobDirParams jobDirParams, String logFileName, String errorFileName){
		this.logDir = jobDirParams.getLogDir();
		this.errorDir = jobDirParams.getErrorDir();
		this.logFileName = logFileName;
		this.errorFileName = errorFileName;
	}
	
	/**
	 * This constructor is used if the dockets step logger is instantiated outside the 
	 * spring batch.
	 * 
	 * @param logDir
	 * @param errorDir
	 * @param logFileName
	 * @param errorFileName
	 */
	public DocketsStepLogger(File logDir, File errorDir, String logFileName, String errorFileName){
		this.logDir = logDir;
		this.errorDir = errorDir;
		this.logFileName = logFileName;
		this.errorFileName = errorFileName;
	}
	

	public void log(String val)
	{
		writeStepLog(logDir, logFile, logFileName, val);
		LOG.debug(val);
	}
	
	public void log(String val, Throwable t)
	{
		writeStepLog(logDir, logFile, logFileName, val);
		LOG.debug(val, t);
	}

	public void error(String val)
	{
		writeStepLog(errorDir, errorFile, errorFileName, val);
		LOG.error(val);
	}
	
	public void error(String val, Throwable t)
	{
		writeStepLog(errorDir, errorFile, errorFileName, val);
		LOG.error(val, t);
	}
	
	private void writeStepLog(File dir, File file, String fileName, String val){
		if (dir != null)
		{
			if (!dir.exists())
			{
				dir.mkdirs();
			}
			try
			{
    			if (file == null)
    			{
    				file = new File(dir, fileName);
    				if(!file.exists())
    				{
    					file.createNewFile();
    				}
    			}
    			StackTraceElement[] stack = Thread.currentThread().getStackTrace();
    			String invokingMethod = stack[2].getMethodName();
    			String logStr = (new Date()).toString() + " " + invokingMethod + ": " + val + '\n';
    			FileUtils.writeStringToFile(file, logStr, true);
			}catch(IOException io){
				 //do nothing	
			}
		}
	}

	public File getLogDir() {
		return logDir;
	}

	public File getErrorDir() {
		return errorDir;
	}
	
	public String getLogFileName() {
		return logFileName;
	}
	
	public String getErrorFileName() {
		return errorFileName;
	}

}

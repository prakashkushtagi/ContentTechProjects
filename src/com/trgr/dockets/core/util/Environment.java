/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 Proprietary and Confidential information of Thomson Reuters. 
 Disclosure, Use or Reproduction without the written 
 authorization of Thomson Reuters is prohibited.
 */

package com.trgr.dockets.core.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.trgr.dockets.core.CoreConstants;

public class Environment
{
	
	public static final String WORKSTATION = "workstation";

	private String env;
	private String jrecsEnv;
	private String cornerstoneConfig;
	private String ltcCheckLsvUrl;
	private String ltcCheckTime;
	private String ltcLsvUsername;
	private String ltcLsvPassword;
	
	private static Environment environment = new Environment();

	private Environment()
	{

	}

	public static Environment getInstance()
	{
		return environment;
	}

	/**
	 * This method lookup the JNDI local environment variable set on the server.
	 * If the initialcontext lookup gets an NamingException this indicates
	 * either there is no JNDI defined or there is no container.
	 * 
	 * @return
	 */
	public String getEnv()
	{
		Context context;
		if (env == null)
		{
			try
			{
				context = (Context) new InitialContext().lookup(CoreConstants.JNDI_LOCAL_LOOKUP_PREFIX);
				env = (String) context.lookup(CoreConstants.SERVER_ENVIRONMENT_ATTTRIBUTE);
			}
			catch (NamingException e)
			{
				env = WORKSTATION;
			}
		}
		return env;
	}
	
	/**
	 * This method lookup the JNDI local environment variable set on the server.
	 * If the initialcontext lookup gets an NamingException this indicates
	 * either there is no JNDI defined or there is no container.
	 * 
	 * @return
	 */
	public String getCornerstoneConfig()
	{
		Context context;
		if (cornerstoneConfig == null)
		{
			try
			{
				context = (Context) new InitialContext().lookup(CoreConstants.JNDI_LOCAL_LOOKUP_PREFIX);
				cornerstoneConfig = (String) context.lookup(CoreConstants.CORNERSTONE_CONFIG);
			}
			catch (NamingException e)
			{

				cornerstoneConfig = CoreConstants.CORNERSTONE_CONFIG_LOCATION;
			}
		}
		return cornerstoneConfig;
	}
	
	/**
	 * This method lookup the JNDI local environment variable set on the server.
	 * If the initialcontext lookup gets an NamingException this indicates
	 * either there is no JNDI defined or there is no container.
	 * 
	 * @return
	 */
	public String getJrecsEnv()
	{
		Context context;
		if (jrecsEnv == null)
		{
			try
			{
				context = (Context) new InitialContext().lookup(CoreConstants.JNDI_LOCAL_LOOKUP_PREFIX);
				jrecsEnv = (String) context.lookup(CoreConstants.JRECS_ENVIRONMENT_LEVEL);
			}
			catch (NamingException e)
			{

				jrecsEnv = CoreConstants.JRECS_STANDALONE_ENVIRONMENT;
			}
		}
		return jrecsEnv;
	}

	/**
	 * This method lookup the JNDI local environment variable set on the server.
	 * If the initialcontext lookup gets an NamingException this indicates
	 * either there is no JNDI defined or there is no container.
	 * 
	 * @return
	 */
	public String getLtcCheckLsvUrl()
	{
		Context context;
		if (ltcCheckLsvUrl == null)
		{
			try
			{
				context = (Context) new InitialContext().lookup(CoreConstants.JNDI_LOCAL_LOOKUP_PREFIX);
				ltcCheckLsvUrl = (String) context.lookup(CoreConstants.LTC_LOAD_STATUS_VIEWER_URL);
			}
			catch (NamingException e)
			{

				ltcCheckLsvUrl = CoreConstants.LTC_LOAD_STATUS_VIEWER_QA_URL;
			}
		}
		return ltcCheckLsvUrl;
	}
	
	
	/**
	 * This method lookup the JNDI local environment variable set on the server.
	 * If the initialcontext lookup gets an NamingException this indicates
	 * either there is no JNDI defined or there is no container.
	 * 
	 * @return
	 */
	public String getLtcCheckTime()
	{
		Context context;
		if (ltcCheckTime == null)
		{
			try
			{
				context = (Context) new InitialContext().lookup(CoreConstants.JNDI_LOCAL_LOOKUP_PREFIX);
				ltcCheckTime = (String) context.lookup(CoreConstants.LTC_LOAD_STATUS_VIEWER_TIME);
			}
			catch (NamingException e)
			{

				ltcCheckTime = CoreConstants.LTC_LOAD_STATUS_VIEWER_DEFAULT_TIME;
			}
		}
		return ltcCheckTime;
	}
	
	/**
	 * This method lookup the JNDI local environment variable set on the server.
	 * If the initialcontext lookup gets an NamingException this indicates
	 * either there is no JNDI defined or there is no container.
	 * 
	 * @return
	 */
	public String getLtcLsvUsername()
	{
		Context context;
		if (ltcLsvUsername == null)
		{
			try
			{
				context = (Context) new InitialContext().lookup(CoreConstants.JNDI_LOCAL_LOOKUP_PREFIX);
				ltcLsvUsername = (String) context.lookup(CoreConstants.LTC_LOAD_STATUS_VIEWER_USERNAME);
			}
			catch (NamingException e)
			{

				ltcLsvUsername = CoreConstants.LTC_LOAD_STATUS_VIEWER_USERNAME_DEFAULT;
			}
		}
		return ltcLsvUsername;
	}
	
	
	/**
	 * This method lookup the JNDI local environment variable set on the server.
	 * If the initialcontext lookup gets an NamingException this indicates
	 * either there is no JNDI defined or there is no container.
	 * 
	 * @return
	 */
	public String getLtcLsvPassword()
	{
		Context context;
		if (ltcLsvPassword == null)
		{
			try
			{
				context = (Context) new InitialContext().lookup(CoreConstants.JNDI_LOCAL_LOOKUP_PREFIX);
				ltcLsvPassword = (String) context.lookup(CoreConstants.LTC_LOAD_STATUS_VIEWER_PASSWORD);
			}
			catch (NamingException e)
			{

				ltcLsvPassword = CoreConstants.LTC_LOAD_STATUS_VIEWER_PASSWORD_DEFAULT;
			}
		}
		return ltcLsvPassword;
	}
	
	public static boolean environmentIsWorkstation(final String env) {
		return WORKSTATION.equals(env);
	}
	
}

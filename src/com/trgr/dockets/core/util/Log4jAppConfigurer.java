package com.trgr.dockets.core.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Log4jConfigurer;

/**
 * A simple Spring Bean that allows log configuration to be managed in the Application Context
 * 
 */
public class Log4jAppConfigurer implements InitializingBean
{
    //private String location;
	/** This is the interval time to check the log4j config file for any changes. */
    private long refreshInterval;
 
    /**
     * It will load the environment corresponding log4j configuration file.
     * This method will be called by the Spring dynamically
     */
    public void afterPropertiesSet() throws Exception
    {
    	String environment = Environment.getInstance().getEnv();
    	
    	if(environment == null)
    	{
    		return;
    	}
    	//construct the system property (combination of context and host name) and set it
    	//to the system to use later in the log4j configuration files    	
    	String hostName = java.net.InetAddress.getLocalHost().getHostName();
    	if (hostName.indexOf("-XP") > -1 || hostName.toUpperCase().indexOf("-W7A") > -1)
        {
           hostName = "LOCAL";
        }
    	
    	String location = "classpath:log4j_"+environment.toLowerCase()+".xml";
 
        if (refreshInterval == 0)
        {
            Log4jConfigurer.initLogging(location);
        }
        else
        {
            Log4jConfigurer.initLogging( location, refreshInterval);
        }
    }
 
    /*// Attribute injectors
    public void setLocation(String location)
    {
        this.location = location;
    }*/
    
    /**
     * It sets the value by Spring DI.
     */
    public void setRefreshInterval(long refreshInterval){
        this.refreshInterval = refreshInterval;
    }
    
}

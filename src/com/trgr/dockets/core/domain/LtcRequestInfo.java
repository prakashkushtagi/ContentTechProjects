/**
 * 
 */
package com.trgr.dockets.core.domain;


/**
 * Domain objects holds the necessary info to build a LTC request.
 * 
 * @author C047166
 *
 */
public class LtcRequestInfo
{
	public static final String EVENT_PUBLISHER = "LTC";
	public static final String EVENT_TYPE = "SYSTEM.EVENT";
	public static final String EVENT_OWNER = "LTC";
	public static final String REQUEST_TYPE = "Load";
	public static final String REQUEST_SOURCE = "Dockets";
	public static final String LTC_NOVUS_LOAD_VARIANT = "Ordered Load"; //???
	public static final String LTC_METADOC_LOAD_VARIANT = "Metadoc Ordered Load"; //???
	public static final String DATASET_RESOURCE_NAME = "DocketsTest"; //???
	
	private String dataTypeName;
	private String dataResourceName;
	private String subscriptionEvents;
	private String queueHost;
	private String queueName;
	private String queueChannel;
	private String subscriptionEmailRecipients;
	private String requestUrl;
	
	
	public String getDataTypeName()
	{
		return dataTypeName;
	}
	public void setDataTypeName(String dataTypeName)
	{
		this.dataTypeName = dataTypeName;
	}
	public String getDataResourceName()
	{
		return dataResourceName;
	}
	public void setDataResourceName(String dataResourceName)
	{
		this.dataResourceName = dataResourceName;
	}
	
	public String getQueueHost()
	{
		return queueHost;
	}
	public void setQueueHost(String queueHost)
	{
		this.queueHost = queueHost;
	}
	public String getQueueName()
	{
		return queueName;
	}
	public void setQueueName(String queueName)
	{
		this.queueName = queueName;
	}
	public String getQueueChannel()
	{
		return queueChannel;
	}
	public void setQueueChannel(String queueChannel)
	{
		this.queueChannel = queueChannel;
	}
	public String getSubscriptionEvents()
	{
		return subscriptionEvents;
	}
	public void setSubscriptionEvents(String subscriptionEvents)
	{
		this.subscriptionEvents = subscriptionEvents;
	}
	public String getSubscriptionEmailRecipients()
	{
		return subscriptionEmailRecipients;
	}
	public void setSubscriptionEmailRecipients(String subscriptionEmailRecipients)
	{
		this.subscriptionEmailRecipients = subscriptionEmailRecipients;
	}
	public String getRequestUrl()
	{
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl)
	{
		this.requestUrl = requestUrl;
	}
}

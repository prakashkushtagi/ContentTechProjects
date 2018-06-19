/*Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.environment.overrides;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class EnvironmentalRuleProcessor {
	private static Logger LOG = Logger.getLogger(EnvironmentalRuleProcessor.class);
	
	private static final String CONFIG_LOCATION = "EnvironmentalRules.xml";	
	private static Map<String, EnvironmentalRule> environmentalRules;
	

	public static boolean overrideFilterChain(Long vendorId) throws Exception{
		return doesRuleApplyForEnv(getEnvironmentalRuleForVendor(vendorId));
	}
	
	public static String getCustomFilterChain(Long vendorId) throws Exception{
		EnvironmentalRule tempRule = getEnvironmentalRuleForVendor(vendorId);
		if (doesRuleApplyForEnv(tempRule)){
			return tempRule.getFilterChainName();
		}		
		return "";
	}
	
	public static boolean splitByVendor(Long vendorId) throws Exception{
		return doesRuleApplyForEnv(getEnvironmentalRuleForVendor(vendorId));
	}
	
	private static boolean doesRuleApplyForEnv(EnvironmentalRule rule) throws Exception{
		if (rule != null){
			String CURRENT_ENV = System.getProperty("environment");
			return rule.applyRuleForThisEnv(CURRENT_ENV);
		} else {
			return false;
		}		
	}
	
	private static EnvironmentalRule getEnvironmentalRuleForVendor(Long vendorId) throws Exception{
		loadEnvironmentalRulesIfEmpty();
		return environmentalRules.get(vendorId.toString());		
	}
		
	private static void loadEnvironmentalRulesIfEmpty() throws Exception{
		if (environmentalRules == null || environmentalRules.size() == 0){
			loadEnvironmentalRules();
		}
	}
	
	private static void loadEnvironmentalRules() throws Exception{
		environmentalRules = new HashMap<String, EnvironmentalRule>();
		Properties envRulesProperties = new Properties();
		InputStream config = null;
		try {
			config = EnvironmentalRuleProcessor.class.getResourceAsStream(CONFIG_LOCATION);
			envRulesProperties.loadFromXML(config);
		} catch (Exception e){
			LOG.warn("Failed to load environmental properties to determine environment-specific court logic!");
			throw e;
		}	
		
		for (Map.Entry<Object, Object> prop : envRulesProperties.entrySet()){
			String key = (String) prop.getKey();
			String value = (String) prop.getValue();			
			environmentalRules.put(key,  createRuleFromProperty(key, value));			
		}
	}
	
	private static EnvironmentalRule createRuleFromProperty (String key, String value) throws Exception{
		//We intentionally do not check string array length because if the property field is ill-formed
		//we want to fail and not continue processing
		String[] propertyValues = value.split("\\|");
		String filterChainName = propertyValues[0];
		String[] environments = propertyValues[1].split(";", -1);
		return new EnvironmentalRule(key,filterChainName, environments);
	}
	
	 
}

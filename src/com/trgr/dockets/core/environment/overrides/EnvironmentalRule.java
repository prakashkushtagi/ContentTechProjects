/*Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.environment.overrides;

import java.util.HashSet;
import java.util.Set;

public class EnvironmentalRule {
	
	private Set<String> environmentsToApply;
	private String filterChainName;
	private String vendorId;


	public EnvironmentalRule(String vendorId, String filterChainName, String[] environmentsToApply){
		this.setFilterChainName(filterChainName);
		this.environmentsToApply = new HashSet<String>();
		for (String environment : environmentsToApply){
			this.environmentsToApply.add(environment);
		}
	}
	
	public boolean applyRuleForThisEnv(String environment){
		return environmentsToApply.contains(environment);
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getFilterChainName() {
		return filterChainName;
	}

	public void setFilterChainName(String filterChainName) {
		this.filterChainName = filterChainName;
	}

	@Override
	public String toString() {
		return "EnvironmentalRule [filterChainName=" + filterChainName + ",  environmentsToApply=" + environmentsToApply + "]";
	}
	
	
	
	
}

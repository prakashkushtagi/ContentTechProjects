package com.trgr.dockets.core.domain;

import java.util.Collection;

import org.springframework.util.Assert;

/**
 * Container for collections of Dockets that were successfully processed (good) and
 * those that failed to process (bad).
 */
public class AllAndBadDockets {
	
	private Collection<Docket> allDockets;
	private Collection<Docket> badDockets;
	
	public AllAndBadDockets(Collection<Docket> all, Collection<Docket> bad) {
		Assert.notNull(all, "The List containing the population of dockets may not be null");
		Assert.notNull(bad, "The List containing the dockets in error may not be null");
		this.allDockets = all;
		this.badDockets = bad;
	}
	
	public Collection<Docket> getAllDockets() {
		return allDockets;
	}
	public Collection<Docket> getBadDockets() {
		return badDockets;
	}

}

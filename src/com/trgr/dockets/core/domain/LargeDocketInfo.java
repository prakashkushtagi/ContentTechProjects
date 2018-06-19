/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */

package com.trgr.dockets.core.domain;

public class LargeDocketInfo {
	private int nbrOfDocketEntries;
	private int nbrOfParties;
	private boolean isLargeDocket;

	public int getNbrOfDocketEntries() {
		return nbrOfDocketEntries;
	}

	public void setNbrOfDocketEntries(int nbrOfDocketEntries) {
		this.nbrOfDocketEntries = nbrOfDocketEntries;
	}

	public int getNbrOfParties() {
		return nbrOfParties;
	}

	public void setNbrOfParties(int nbrOfParties) {
		this.nbrOfParties = nbrOfParties;
	}

	public boolean isLargeDocket() {
		return isLargeDocket;
	}

	public void setLargeDocket(boolean isLargeDocket) {
		this.isLargeDocket = isLargeDocket;
	}

	public char getLargeDocket() {
		if (isLargeDocket) {
			return 'Y';
		} else {
			return 'N';
		}
	}
}

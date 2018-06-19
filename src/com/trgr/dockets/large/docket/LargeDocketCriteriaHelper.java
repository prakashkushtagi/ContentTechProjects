/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.large.docket;

import java.util.List;

import com.trgr.dockets.core.entity.LargeDocketCriteria;

public class LargeDocketCriteriaHelper {

	public static LargeDocketCriteria selectLargeDocketCriteria(long desiredProductId, List<LargeDocketCriteria> largeDocketCriteriaList) {
		for (LargeDocketCriteria largeDocketCriteria : largeDocketCriteriaList) {
			long productId = largeDocketCriteria.getProductId();
			if(productId == desiredProductId){
				return largeDocketCriteria;
			}
		}
		
		return new LargeDocketCriteria(-1,0,0);
	}
}

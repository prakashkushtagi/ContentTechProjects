/**Copyright 2015: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.dao;

import com.trgr.dockets.core.entity.DictionaryNormalized;
import com.trgr.dockets.core.entity.DictionaryNormalizedKey;

public interface DictionaryNormalizedDao {

	public DictionaryNormalized findNormalizedNamesByPrimaryKey(DictionaryNormalizedKey primaryKey);

	public void persist(DictionaryNormalized dictNorm);
}

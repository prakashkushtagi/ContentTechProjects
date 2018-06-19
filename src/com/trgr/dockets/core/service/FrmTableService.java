/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.service;

import com.trgr.dockets.core.entity.FrmTable;


/**
 * Spring service that handles CRUD requests for FrmTable entities
 * 
 */
public interface FrmTableService {

	/**
	 * Delete an existing FrmTable entity
	 * 
	 */
	public void deleteFrmTable(FrmTable frmtable);

	/**
	 */
	public FrmTable findFrmTableByPrimaryKey(String countyCode, String attorneyCode);

	/**
	 * Save an existing FrmTable entity
	 * 
	 */
	public void saveFrmTable(FrmTable frmtable_1);
}
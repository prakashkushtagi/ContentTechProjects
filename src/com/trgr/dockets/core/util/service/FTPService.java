/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.util.service;

import java.io.IOException;

import com.trgr.dockets.core.exception.FTPException;
import com.trgr.dockets.core.util.FTPFile;

/**
 * A generic FTP service interface to load/retrieve/cleanup files from NAS.
 * 
 * @author C047166
 *
 */
public interface FTPService
{
	/**
	 * FTP a file to NAS folder.
	 * 
	 * @param ftpFile
	 * @param nasLandingPath
	 * @param nasLandingPathFolder
	 * @throws FTPException
	 */
	public void ftpFileToLandingStrip(FTPFile ftpFile, String nasLandingPath, String nasLandingPathFolder) throws FTPException;
	
	
	/**
     * Method to retrieve the processed FTP file from the landing strip sub folder.
     *  
     * @param	
	 * @throws FTPException 
     */
    public void retrieveFileFromLandingStrip(FTPFile ftpFile, String nasLandingPath, String nasLandingPathFolder) throws FTPException;
    
    /**
	 * Delete a folder completely including the files inside it.
	 * 
	 * @param nasPath
	 * @param nasFolder
	 * @param files
	 * @throws IOException
	 */
	public void cleanupNasFolder(String nasPath, String nasFolderName) throws IOException;
	
}

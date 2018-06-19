/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.util.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.trgr.dockets.core.exception.FTPException;
import com.trgr.dockets.core.util.DocketFTPClient;
import com.trgr.dockets.core.util.FTPFile;

/**
 * A generic FTP service class to load/retrieve/cleanup files from NAS.
 * 
 * @author C047166
 *
 */
public class FTPServiceImpl implements FTPService
{
	private static final Logger log = Logger.getLogger(FTPServiceImpl.class);
	
	private DocketFTPClient docketFtpClient;
	
	@Override
	public synchronized void ftpFileToLandingStrip(FTPFile ftpFile, String nasLandingPath, String nasLandingPathFolder) throws FTPException
	{
		log.info("Attempting to FTP the src file with path " + ftpFile.getFileName() + " to " + nasLandingPath + "/" + nasLandingPathFolder);
		DocketFTPClient ftpClient = new DocketFTPClient(docketFtpClient);
		InputStream inputStream = null;
		try
		{
			if (!ftpClient.isConnected()) 
			{
			ftpClient.connect();
			log.info("Connected to " + ftpClient.getServerName());
			
			}

			if(null != nasLandingPathFolder)
			{
				ftpClient.makeDirectory(nasLandingPath, nasLandingPathFolder);
			}
			if (ftpFile.getInputStream() != null)
			{
				inputStream = ftpFile.getInputStream();
			}
			else
			{
				if (!ftpFile.getFileName().equals(""))
				{
					inputStream = new BufferedInputStream(new FileInputStream(new File(ftpFile.getFileName())));
				}
			}
			String filename = ftpFile.getFileName();
			filename = FilenameUtils.getName(filename);

			if (!ftpClient.isConnected())
			{
				ftpClient.connect();
				log.info("Re-Connected to " + ftpClient.getServerName());
			}
			if(null != nasLandingPathFolder)
			{
				ftpClient
					.put(nasLandingPath + "/" + nasLandingPathFolder, filename, inputStream);
				log.info(" file were FTP at  nasLandingPath + nasLandingPathFolder =" + nasLandingPath + "/" + nasLandingPathFolder);
			}
			else
			{
				ftpClient.put(nasLandingPath, filename, inputStream);
				log.info(" file were FTP at nasLandingPath =" + nasLandingPath);
			}
			
			log.info("Completed FTPing the src file with path " + ftpFile.getFileName() + " to " + nasLandingPath + "/" + nasLandingPathFolder + " on " +ftpClient.getServerName() );

		}
		catch (IOException io)
		{
			log.info("IO Exception while FTP file :"+io.getMessage());
			
			String errorMessage = "Error ocurred while FTPing the file " + ftpFile.getFileName();
			if (ftpClient != null && ftpClient.getServerName() != null )
			{ 
				errorMessage = "Error ocurred while FTPing the file " + ftpFile.getFileName() + " to " + ftpClient.getServerName();
			}
			log.error(errorMessage, io);
			throw new FTPException("Error ocurred while FTPing the file " + ftpFile.getFileName(), io);
		}
		finally
		{
			try {
				if (inputStream != null)
				{
					inputStream.close();
				}
			} catch (IOException e) {
				throw new FTPException("Error closing inputStream for ftpFileToLandingStrip. ", e);
			}
			if (ftpClient.isConnected()) ftpClient.disconnect();
		}
	}
	
	@Override
	public synchronized void retrieveFileFromLandingStrip(FTPFile ftpFile, String nasLandingPath, String nasLandingPathFolder) throws FTPException
	{
    	log.info("Attempting to retrieve NAS file " + ftpFile.getFileName() + " from " + nasLandingPath + "/" + nasLandingPathFolder);
    	try    			
		{
    		DocketFTPClient ftpClient = new DocketFTPClient(docketFtpClient);
    		//TODO: Unsure why this is 'needed'
			if (ftpClient.isConnected())
			{
				ftpClient.disconnect();
			}
			ftpClient.connect();
			InputStream inputStream = ftpClient.get(
					nasLandingPath + "/" + nasLandingPathFolder, ftpFile.getFileName());
			ftpFile.setInputStream(inputStream);
			log.info("Retrieved NAS file " + ftpFile.getFileName() + " from " + nasLandingPath + "/" + nasLandingPathFolder);
		}
		catch (IOException io)
		{
			log.error("Error ocurred while retrieving the file " + ftpFile.getFileName() + " from NAS path " + nasLandingPath + "/" + nasLandingPathFolder, io);
			throw new FTPException("Error ocurred while retrieving the file " + ftpFile.getFileName() + " from NAS path " + nasLandingPath + "/" + nasLandingPathFolder, io);
		}
	}
    
    /**
     * Delete files in a remote directory.
     *  
     * @param files - list of files 
     * @throws IOException
     */
	private void deleteFilesInNasFolder(String remoteDirectory, List<String> files) throws IOException{
		Iterator<String> iterator = files.iterator();
		DocketFTPClient ftpClient = new DocketFTPClient(docketFtpClient);
		while (iterator.hasNext()) {
	   		String fileName = iterator.next();
	   		ftpClient.deleteFile(remoteDirectory, fileName);
	   	}
	}
	
	/**
	 * Delete a remote folder.
	 * 
	 * @param nasPath
	 * @param deleteDirectoryName
	 * @throws IOException
	 */
	private void deleteNasFolder(String nasPath, String deleteDirectoryName) throws IOException{
		DocketFTPClient ftpClient = new DocketFTPClient(docketFtpClient);
		ftpClient.removeDirectory(nasPath, deleteDirectoryName);
	}
	
	@Override
	public void cleanupNasFolder(String nasPath, String nasFolderName) throws IOException{
		DocketFTPClient ftpClient = new DocketFTPClient(docketFtpClient);
		String nasFolderFullPath = nasPath + "/" + nasFolderName;
		String[] nasFolderFiles = ftpClient.listNasFolderFiles(nasFolderFullPath);
		if(null != nasFolderFiles && nasFolderFiles.length > 0){
			deleteFilesInNasFolder(nasPath + "/" + nasFolderName, Arrays.asList(nasFolderFiles));
		}
		deleteNasFolder(nasPath, nasFolderName);
	}
	
	public DocketFTPClient getDocketFtpClient()
	{
		return docketFtpClient;
	}

	public void setDocketFtpClient(DocketFTPClient docketFtpClient)
	{
		this.docketFtpClient = docketFtpClient;
	}
}

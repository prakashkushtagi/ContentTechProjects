/**
 * Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and 
 * Confidential information of Thomson Reuters. Disclosure, Use or Reproduction 
 * without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;

import com.thomson.judicial.dockets.bankruptcycontentservice.BankruptcyContentServiceConstants;
import com.thomson.judicial.dockets.bankruptcycontentservice.exception.BankruptcyContentServiceException;
import com.trgr.dockets.core.CoreConstants;
import com.trgr.dockets.core.domain.NovusDocumentMetadata;
import com.trgr.dockets.core.exception.DocketsCoreUtilException;
import com.trgr.dockets.core.parser.IDocumentParser;

/**
 * @author C047166
 *
 */
public class DocketFileUtils
{
	private static final Logger log = Logger.getLogger(DocketFileUtils.class);
	/**
	 * Converts the inputFile into gzip format.
	 *  
	 * @param inputFile
	 * @param gzipOutputFile
	 * @throws IOException
	 */
	public static void convertFileToGZIPFormat(File inputFile, File gzipOutputFile) throws IOException
	{
		byte buffer[] = new byte[10240];
		BufferedInputStream bis = null;
		BufferedOutputStream bfos = null;

		InputStream inputFileStream = null;
		try
		{
			inputFileStream = FileUtils.openInputStream(inputFile);
		}
		catch (IOException io)
		{
			throw new IOException("Error opening an inputstream to the src file " + inputFile.getPath(), io);
		}

		bis = new BufferedInputStream(inputFileStream, 10240);
		int length;
		try
		{
			bfos = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(gzipOutputFile)), 10240);
			while ((length = bis.read(buffer)) != -1)
				bfos.write(buffer, 0, length);
			bfos.close();
		}
		catch (Exception e)
		{
			throw new IOException("Error occured creating gzip file for inputfile " + inputFile.getPath(), e);
		}
		finally
		{
			try
			{
				if (inputFileStream != null) {
					inputFileStream.close();
				}
				if (bis != null) {
					bis.close();
				}
				if (bfos != null) {
					bfos.close();
				}
			}
			catch (IOException e)
			{
				
			}
		}
	}
	
	
	public static void convertInputStreamToGZIPFormat(InputStream inputFileStream, File gzipOutputFile) throws IOException
	{
		byte buffer[] = new byte[10240];
		BufferedInputStream bis = null;
		BufferedOutputStream bfos = null;

		bis = new BufferedInputStream(inputFileStream, 10240);
		int length;
		try
		{
			bfos = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(gzipOutputFile)), 10240);
			while ((length = bis.read(buffer)) != -1)
				bfos.write(buffer, 0, length);
			bfos.close();
		}
		catch (Exception e)
		{
			throw new IOException("Error occured creating gzip file for inputfile from inputStream" , e);
		}
		finally
		{
			try
			{
				if (inputFileStream != null) {
					inputFileStream.close();
				}
				if (bis != null) {
					bis.close();
				}
				if (bfos != null) {
					bfos.close();
				}
			}
			catch (IOException e)
			{
				
			}
		}
	}
	
	/**
	 * Writes the given gzip input stream to a file.
	 * 
	 * @param inputStream
	 * @param file
	 * @throws IOException
	 */
	public static void writeGzipInputStreamToFile(InputStream inputStream, File file) throws IOException{

		InputStream gzipInputStream = null;
		try
		{
			if (inputStream != null)
			{
				gzipInputStream = new GZIPInputStream(inputStream);
				OutputStream fileOutputStream = new FileOutputStream(file);
				IOUtils.copy(gzipInputStream, fileOutputStream);
				IOUtils.closeQuietly(gzipInputStream);
				IOUtils.closeQuietly(fileOutputStream);
			}
		}
		catch (IOException io)
		{
			throw new IOException("Error occurred while copying the contents fron NAS file stream to destination file " + file.getPath(),
					io);
		}
	
	}
	
	/**
	 * Writes the given input stream to a file.
	 * 
	 * @param inputStream
	 * @param file
	 * @throws IOException
	 */
	public static void writeInputStreamToFile(InputStream inputStream, File file) throws IOException{

		try
		{
			if (inputStream != null)
			{
				OutputStream fileOutputStream = new FileOutputStream(file);
				IOUtils.copy(inputStream, fileOutputStream);
				IOUtils.closeQuietly(inputStream);
				IOUtils.closeQuietly(fileOutputStream);
			}
		}
		catch (IOException io)
		{
			throw new IOException("Error occurred while copying the contents fron NAS file stream to destination file " + file.getPath(),
					io);
		}
	
	}
	
	/**
	 * Zip the sourceFile to targetZipFile
	 * 
	 * @param sourceFile
	 * @param targetZipFile
	 * @param zipEntryFileName
	 * @throws Exception
	 */
	public static void zipFile(File sourceFile, File targetZipFile, String zipEntryFileName) throws Exception{
		ZipOutputStream zipOutputStream = null;
		if(sourceFile.exists()){
			InputStream srcFileInputStream = null;
			try
			{
				srcFileInputStream = FileUtils.openInputStream(sourceFile);
				zipOutputStream = new ZipOutputStream(new FileOutputStream(targetZipFile.getPath()));
				ZipEntry zipEntry = new ZipEntry(zipEntryFileName);
				zipOutputStream.putNextEntry(zipEntry);
				IOUtils.copy(srcFileInputStream, zipOutputStream);
			}catch(Exception e){
				throw new IOException("Error occurred while zipping the source file " + sourceFile.getPath(),
						e);
			}finally{
				IOUtils.closeQuietly(srcFileInputStream);
				IOUtils.closeQuietly(zipOutputStream);
			}	
		}else{
			
		}
	}
	
	/**
	 * Zip the sourceFile to targetZipFile
	 * 
	 * @param sourceFile
	 * @param targetZipFile
	 * @throws Exception
	 */
	public static void zipFile(File sourceFile, File targetZipFile) throws Exception{
		BufferedInputStream bufferedInputStream = null;
		int bufferSize = 2048;
		byte data[] = new byte[bufferSize];
		ZipOutputStream zipOutputStream = null;
		if(sourceFile.exists()){
			try
			{
				FileInputStream fileInputStream = new FileInputStream(sourceFile);
				bufferedInputStream = new  BufferedInputStream(fileInputStream, bufferSize);
				zipOutputStream = new ZipOutputStream(new FileOutputStream(targetZipFile.getPath()));
				ZipEntry zipEntry = new ZipEntry(sourceFile.getName());
				zipOutputStream.putNextEntry(zipEntry);
				int count;
		        while((count = bufferedInputStream.read(data, 0, bufferSize)) != -1) {
		        	zipOutputStream.write(data, 0, count);
		        }
		    }catch(Exception e){
				throw new IOException("Error occurred while zipping the source file " + sourceFile.getPath(),
						e);
			}finally{
				IOUtils.closeQuietly(bufferedInputStream);
				IOUtils.closeQuietly(zipOutputStream);
			}	
		}else{
			
		}
	}
	
	/**
	 * Zips a list of sourceFiles to targetZipFile
	 * 
	 * @param sourceFile
	 * @param targetZipFile
	 * @throws IOException 
	 * @throws Exception
	 */
	public static void zipFiles(List<File> sourceFiles, File targetZipFile){
		log.info("About to zip " + sourceFiles.size() + " files.");
		BufferedInputStream bufferedInputStream = null;
		int bufferSize = 2048;
		byte data[] = new byte[bufferSize];
		ZipOutputStream zipOutputStream = null;
		try{
			zipOutputStream = new ZipOutputStream(new FileOutputStream(targetZipFile.getPath()));
    		for(File file : sourceFiles){
    			if(null != file && file.exists()){
        			log.info("Attempting to zip file " + file.getName());
    				FileInputStream fileInputStream = new FileInputStream(file);
    				bufferedInputStream = new  BufferedInputStream(fileInputStream, bufferSize);
    				ZipEntry zipEntry = new ZipEntry(file.getName());
    				zipOutputStream.putNextEntry(zipEntry);
    				int count;
    			    while((count = bufferedInputStream.read(data, 0, bufferSize)) != -1) {
    			       	zipOutputStream.write(data, 0, count);
    			    }
    			    IOUtils.closeQuietly(bufferedInputStream);	
    			}
    		}
		}catch(Exception e){
			log.error("Error occurred while zipping the source file " + targetZipFile, e);
		}finally{
			IOUtils.closeQuietly(zipOutputStream);
			log.info("Successfully zipped file.");
		}
	}
	
	/**
	 * Given a map of sourceFiles to its uncompressed file size, generates a list of zip files 
	 * (with the input zipFilenamePrefix and fileExtension as their prefix and suffix, respectively) 
	 * in the target directory if the single zip file that would have initially been created exceeds the maxLoadFileSize. 
	 * 
	 * @param sourceFiles
	 * @param zipEntryFileNamePrefix
	 * @param fileExtension
	 * @param maxLoadFileSize
	 * @throws IOException 
	 * @throws Exception
	 */
	public static List<File> zipFiles(Map<File, Long> sourceFiles, String targetDir, String zipFilenamePrefix, String fileExtension, long maxLoadFileSize){
		List<File> loadFiles = new ArrayList<File>();

		try {
			int fileNumber = 1;
			File outputZip = null;
			ZipOutputStream zipOutputStream = null;
			
			long outputSize = 0;
			List<File> filesToAdd = new ArrayList<File>();
			boolean exceedsThreshold = false;
			
    		//Parse files that under the maxLoadFileSize threshold
    		Iterator<File> fileIterator = sourceFiles.keySet().iterator();
    		while(fileIterator.hasNext()) {
    			File sourceFile = fileIterator.next();
    			if(sourceFile.exists()){
    				Long uncompressedFileSize = sourceFiles.get(sourceFile);
	    			if (uncompressedFileSize < maxLoadFileSize) {
	    				outputSize += uncompressedFileSize;
	    				if (outputSize < maxLoadFileSize) {
		    				filesToAdd.add(sourceFile);
		    			} else {
		    				exceedsThreshold = true;
		    			}
		
	    				if (exceedsThreshold) {
		    				outputZip = new File(targetDir, String.format("%s.part%02d.%s", zipFilenamePrefix, fileNumber, fileExtension));
		    				zipOutputStream = new ZipOutputStream(new FileOutputStream(outputZip));
		    				for (File fileToAdd : filesToAdd) {
		    					writeZipEntry(zipOutputStream, fileToAdd);
		    				}
		    				IOUtils.closeQuietly(zipOutputStream);
		    				loadFiles.add(outputZip);
		    				fileNumber++;

		    				outputSize = sourceFiles.get(sourceFile);
		    				exceedsThreshold = false;
		    				filesToAdd.clear();
		    				filesToAdd.add(sourceFile);		    				
		    			}
	    			}
    			}
    		}
    		
    		//Output any remaining files into a single zip file
    		if (!filesToAdd.isEmpty()) {
    			outputZip = new File(targetDir, String.format("%s.part%02d.%s", zipFilenamePrefix, fileNumber, fileExtension));
				zipOutputStream = new ZipOutputStream(new FileOutputStream(outputZip));
				for (File fileToAdd : filesToAdd) {
					writeZipEntry(zipOutputStream, fileToAdd);
				}
				IOUtils.closeQuietly(zipOutputStream);
				loadFiles.add(outputZip);
				fileNumber++;
    		}
    		
    		//Parse files that are equal to or over the maxLoadFileSize threshold
    		fileIterator = sourceFiles.keySet().iterator();
    		while(fileIterator.hasNext()) {
    			File sourceFile = fileIterator.next();
    			if(sourceFile.exists()) {
    				Long uncompressedFileSize = sourceFiles.get(sourceFile);
	    			if (uncompressedFileSize >= maxLoadFileSize) {
	    				//Generate a zip file for each file that exceeds the maxLoadFileSize threshold
	    				log.warn(String.format("Input file %s exceeds the maxLoadFileSize of %d bytes - size of file is %d bytes", sourceFile.getPath(), maxLoadFileSize, uncompressedFileSize));
	    				outputZip = new File(targetDir, String.format("%s.part%02d.%s", zipFilenamePrefix, fileNumber, fileExtension));
	    				zipOutputStream = new ZipOutputStream(new FileOutputStream(outputZip));
	    				writeZipEntry(zipOutputStream, sourceFile);
	    				IOUtils.closeQuietly(zipOutputStream);
	    				loadFiles.add(outputZip);
	    				fileNumber++;
	    			}
    			}
    		}
    	
    		//If only one load file is returned, rename to the original filename (minus the 'part' string)
    		if (loadFiles.size() == 1) {
    			File oldFile = loadFiles.get(0);
    			File newFile = new File (targetDir, String.format("%s.%s", zipFilenamePrefix, fileExtension));
    			oldFile.renameTo(newFile);
    			loadFiles.clear();
    			loadFiles.add(newFile);
    		}
    		
		} catch(Exception e) {
			log.error("Error occurred while zipping the source files", e);
		}
		return loadFiles;
	}
	
	private static void writeZipEntry(ZipOutputStream zipOutputStream, File file) {
		BufferedInputStream bufferedInputStream = null;
		int bufferSize = 2048;
		byte data[] = new byte[bufferSize];
		
		ZipEntry zipEntry = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			bufferedInputStream = new BufferedInputStream(fileInputStream, bufferSize);
			zipEntry = new ZipEntry(file.getName());
			zipOutputStream.putNextEntry(zipEntry);
			int count;
		    while((count = bufferedInputStream.read(data, 0, bufferSize)) != -1) {
		       	zipOutputStream.write(data, 0, count);
		    }
		    zipOutputStream.closeEntry();
		    IOUtils.closeQuietly(bufferedInputStream);
		} catch (Exception e) {
			String message = "Error occured while writing zip entry";
			if (zipEntry != null) {
				message += " for file " + zipEntry.getName();
			}
			log.error(message, e);
		}
	}
	
	/**
	 * Writes a input stream into the target zip file with given zip entry file name.
	 * 
	 * @param inputStream
	 * @param targetZipFile
	 * @param zipEntryFileName
	 * @throws Exception
	 */
	public static void writeStreamToZipFile(InputStream inputStream, File targetZipFile, String zipEntryFileName) throws Exception{
		ZipOutputStream zipOutputStream = null;
		if(inputStream != null){
			try
			{
				zipOutputStream = new ZipOutputStream(new FileOutputStream(targetZipFile.getPath()));
				ZipEntry zipEntry = new ZipEntry(zipEntryFileName);
				zipOutputStream.putNextEntry(zipEntry);
				IOUtils.copy(inputStream, zipOutputStream);
			}catch(Exception e){
				throw new IOException("Error occurred while creating zip file " + targetZipFile.getPath(),
						e);
			}finally{
				IOUtils.closeQuietly(inputStream);
				IOUtils.closeQuietly(zipOutputStream);
			}	
		}else{
			
		}
	}
	
	public static List<NovusDocumentMetadata> handleLoadFileWithRetries(String fileLocation, IDocumentParser parser) throws Exception
	{		
		
		int numberOfRetries = 3;
		while(checkFileLength(fileLocation)<0 && numberOfRetries-- > 0)
		{
			try
			{
				log.info("Waiting 20 seconds ....");
				Thread.sleep(20000);
			}
			catch(Exception e)
			{
				
			}
		}

		File file  = new File(fileLocation);
		if(file.length()<0)
		{
			file = null;
			throw new DocketsCoreUtilException("File length at " + fileLocation +  " is still zero" );
		}
		file = null;		  														
		return parseLoadFile(fileLocation,parser); 						
	}
	
	/**
	 * @param fileLocation
	 * @param file
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws XMLStreamException 
	 * @throws SAXException
	 * @throws SAXParseException
	 * @throws ParserConfigurationException
	 * @throws ZipException
	 * @throws BankruptcyContentServiceException 
	 */
	public static List<NovusDocumentMetadata> parseLoadFile(String fileLocation, IDocumentParser parser)
			throws FileNotFoundException, UnsupportedEncodingException, IOException, XMLStreamException 
	{
		String fileExtension=fileLocation.substring(fileLocation.lastIndexOf(".")+1,fileLocation.length()); 
		FileInputStream fileInputStream =null;
		GZIPInputStream gzipInputStream = null;
		InputStream zipFileInputStream = null;
		List<NovusDocumentMetadata> novusDocketMetadataList = new LinkedList<NovusDocumentMetadata>();
		 
		try 
		{
			fileInputStream = new FileInputStream(fileLocation);
			if (fileExtension.equalsIgnoreCase(CoreConstants.FILE_TYPE_XML)) 
			{
				 novusDocketMetadataList = parser.parse(fileInputStream);	
			}
			else if (fileExtension.equalsIgnoreCase(BankruptcyContentServiceConstants.FILE_TYPE_GZIP))
			{
				gzipInputStream = new GZIPInputStream(fileInputStream);
				novusDocketMetadataList = parser.parse(gzipInputStream);
			}
			else if (fileExtension.equalsIgnoreCase(BankruptcyContentServiceConstants.FILE_TYPE_ZIP))
			{						
				ZipFile zipFile = null;
				File fileOnSystem = new File(fileLocation);
				zipFile = new ZipFile(fileOnSystem);
				Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();		
				while (zipEntries.hasMoreElements())
				{
					ZipEntry ze = zipEntries.nextElement();
					String zipFileExtension=ze.getName().substring(ze.getName().lastIndexOf(".")+1,ze.getName().length());
					
					if (zipFileExtension.equalsIgnoreCase(BankruptcyContentServiceConstants.FILE_TYPE_GZIP)) 
					{
						zipFileInputStream = zipFile.getInputStream(ze);
						gzipInputStream = new GZIPInputStream(zipFileInputStream);
						novusDocketMetadataList.addAll(parser.parse(gzipInputStream));
						gzipInputStream.close();
						gzipInputStream = null;
						zipFileInputStream.close();
						zipFileInputStream = null;
					}
					else if (zipFileExtension.equalsIgnoreCase(BankruptcyContentServiceConstants.FILE_TYPE_XML))
					{
						zipFileInputStream =zipFile.getInputStream(ze);
						novusDocketMetadataList.addAll(parser.parse(zipFileInputStream));
						zipFileInputStream.close();
						zipFileInputStream = null;
					}

				}
				zipFile.close();
				zipFile =null;
			}
		} 
		finally 
		{
			try 
			{
				if(null!=fileInputStream)
				{
					fileInputStream.close();
				}
				if(null!=gzipInputStream)
				{
					gzipInputStream.close();
				}
			} 
			catch (Exception e) 
			{
			}
		}
		return novusDocketMetadataList;
	}
	
	public static long checkFileLength(String fileLocation) 
	{
		long length = 0l;
		
		File file = new File(fileLocation);
		length = file.length();
		file = null;
		return length;
	}
	
	/**
	 * Cleans up a directory with file extension txt and xml. Ignores directories and its not recursive.
	 * 
	 * @param cleanupDir
	 */
	public static void cleanOnlyFilesInDirectory(File cleanupDir){
		if(cleanupDir.exists()){
			Collection<File> fileList = FileUtils.listFiles(cleanupDir, new String[]{"txt", "xml"}, false);
			Iterator<File> fileIterator = fileList.iterator();
			while(fileIterator.hasNext()){
				File file = fileIterator.next();
				if(file.isFile()){
					FileUtils.deleteQuietly(file);
				}
			}
		}
	}
	
	/**
	 *  	
	 * @param file
	 * @return false only if file is null,does not exits or is empty.
	 */
	public static boolean checkfile(File file)
	{
		boolean emptyFlag = false;
		BufferedReader bufferedReader = null;
		try
		{
			if(file != null && file.exists())
			{
				FileReader fileReader = new FileReader(file);
				bufferedReader = new BufferedReader(fileReader);
        		if (bufferedReader.readLine() == null) 
           		{
        			emptyFlag = true;
           		}
        		
			}else{
				emptyFlag = true;
			}
		}catch(Exception e) {			
			emptyFlag = true;
			log.info("Empty or null file = " + file.getAbsolutePath());
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				log.info("IOException attempting to close file: " + file.getAbsolutePath());
				e.printStackTrace();
			}
		}
		return emptyFlag;
	}
}

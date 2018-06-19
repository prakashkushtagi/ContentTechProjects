/*
 * Created on Feb 24, 2006
 *
 * TODO 
 */
package com.trgr.dockets.core.util;

import java.io.InputStream;

public class FTPFile {
	private String fileName = "";
	private InputStream inputStream = null;
	
	
	public FTPFile()
	{
		super();
	}


	public FTPFile(String fileName, InputStream inputStream)
	{
		super();
		this.fileName = fileName;
		this.inputStream = inputStream;
	}

	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
}

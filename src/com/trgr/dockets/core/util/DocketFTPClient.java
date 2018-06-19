package com.trgr.dockets.core.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.west.aqueduct.BaseInputStream;

public class DocketFTPClient
{
	private static final Logger LOG = Logger.getLogger(DocketFTPClient.class);
	private long lastActiveTime = 0;
	private static final long FTP_IDLE_TIMEOUT_TIME = 1000 * 60 * 2;
	
	private String serverName;
	private String userName;
	private String password;
	private FTPClient ftpClient;
	private String homeDirectory = null;
	
	public DocketFTPClient(){}
	
	public DocketFTPClient(DocketFTPClient ftpClientToCopy){
		this.serverName = ftpClientToCopy.getServerName();
		this.userName = ftpClientToCopy.getUserName();
		this.password = ftpClientToCopy.getPassword();
		this.ftpClient = new FTPClient();
	}
	
	public void connect() throws IOException
    {
        try
        {
        	ftpClient.connect(serverName);
        }
        catch (IOException e)
        {
            throw new IOException(serverName + " refused FTP connection", e);
        }
        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
        {
            String reply = ftpClient.getReplyString();
            ftpClient.disconnect();
            throw new IOException(serverName + " refused FTP connection: " + reply);
        }
        if (!ftpClient.login(userName, password))
        {
            String reply = "no additional information available.";
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
            {
                reply = ftpClient.getReplyString();
            }
            ftpClient.disconnect();
            throw new IOException(serverName + " refused FTP login: " + reply);
        }
        // Use passive mode as default because most of us are behind firewalls these days.
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        homeDirectory = ftpClient.printWorkingDirectory();
        lastActiveTime = System.currentTimeMillis();
    }
	
	public boolean makeDirectory(String remoteDirectory, String newSubDirectory) throws IOException
    {
        boolean success = false;
        if (!isConnected())
        {
            connect();
        }
        else
        {
        	ftpClient.changeWorkingDirectory(homeDirectory);
        }
        if (!ftpClient.changeWorkingDirectory(remoteDirectory))
        {
            String reply = "no additional information available.";
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
            {
                reply = ftpClient.getReplyString();
            }
            throw new IOException("could not change directory to \"" + remoteDirectory + "\" on " + serverName + ": " + reply);
        }

        success = ftpClient.changeWorkingDirectory(newSubDirectory);
        if (!success)
        {
            success = ftpClient.makeDirectory(newSubDirectory);
        }

        if (!success)
        {
            String reply = "no additional information available.";
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
            {
                reply = ftpClient.getReplyString();
            }
            throw new IOException("could not make \"" + newSubDirectory + "\" from \"" + remoteDirectory + "\" on " + serverName + ": " + reply);
        }
        return success;
    }
	
	public void put(String remoteDirectory, String targetFileName, InputStream sourceStream) throws IOException
    {
        try
        {
            if (!isConnected())
            {
                connect();
            }
            else
            {
            	ftpClient.changeWorkingDirectory(homeDirectory);
            }
            if (!ftpClient.changeWorkingDirectory(remoteDirectory))
            {
                String reply = "no additional information available.";
                if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
                {
                    reply = ftpClient.getReplyString();
                }
                throw new IOException("could not change directory to \"" + remoteDirectory + "\" on " + serverName + ": " + reply);
            }
            if (!ftpClient.storeFile(targetFileName, sourceStream))
            {
                String reply = "no additional information available.";
                if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
                {
                    reply = ftpClient.getReplyString();
                }
                throw new IOException("could not put \"" + targetFileName + "\" to \"" + remoteDirectory + "\" on " + serverName + ": " + reply);
            }
        }
        finally
        {
            try
            {
                sourceStream.close();
            }
            catch (Exception ignore)
            {
            }
        }
    }
	
	public InputStream get(String remoteDirectory, String sourceFileName) throws IOException
    {
        if (!isConnected())
        {
            connect();
        }
        else
    	{
			disconnect();
			connect();
            ftpClient.changeWorkingDirectory(homeDirectory);
        }

        if (!ftpClient.changeWorkingDirectory(remoteDirectory))
        {
            String reply = "no additional information available.";
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
            {
                reply = ftpClient.getReplyString();
            }
            throw new IOException("could not change directory to \"" + remoteDirectory + "\" on " + serverName + ": " + reply);
        }

        InputStream dataStream = ftpClient.retrieveFileStream(sourceFileName);
        if (null == dataStream)
        {
            String reply = "no additional information available.";
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
            {
                reply = ftpClient.getReplyString();
            }
            throw new IOException("could not get \"" + sourceFileName + "\" from \"" + remoteDirectory + "\" on " + serverName + ": " + reply);
        }

        return new CompletePendingCommandInputStream(dataStream, remoteDirectory, sourceFileName, ftpClient);
    }
	
	public boolean deleteFile(String remoteDirectory, String sourceFileName) throws IOException
    {
        boolean success = false;
        if (!isConnected())
        {
            connect();
        }
        else
        {
            ftpClient.changeWorkingDirectory(homeDirectory);
        }

        if (!ftpClient.changeWorkingDirectory(remoteDirectory))
        {
            String reply = "No additional information available.";
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
            {
                reply = ftpClient.getReplyString();
            }
            throw new IOException("Could not change directory to \"" + remoteDirectory + "\" "+ reply);
        }

        success =  ftpClient.deleteFile(sourceFileName);
        if (!success)
        {
            String reply = "No additional information available.";
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
            {
                reply = ftpClient.getReplyString();
            }
            throw new IOException("Could not delete \"" + sourceFileName + "\" from \"" + remoteDirectory + "\" " + reply);
        }

        return success;
    }
	
	public boolean removeDirectory(String remoteDirectory, String directoryToRemove) throws IOException
    {
        boolean success = false;
        if (!isConnected())
        {
            connect();
        }
        else
        {
        	ftpClient.changeWorkingDirectory(homeDirectory);
        }

        if (!ftpClient.changeWorkingDirectory(remoteDirectory))
        {
            String reply = "no additional information available.";
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
            {
                reply = ftpClient.getReplyString();
            }
            throw new IOException("Could not change directory to \"" + remoteDirectory + "\" " + reply);
        }

        success =  ftpClient.removeDirectory(directoryToRemove);
        if (!success)
        {
            String reply = "no additional information available.";
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
            {
                reply = ftpClient.getReplyString();
            }
            throw new IOException("Could not delete \"" + directoryToRemove + "\" from \"" + remoteDirectory + "\" " + reply);
        }

        return success;
    }
	
	public String[] listNasFolderFiles(String remoteDirectory) throws IOException
    {
       disconnect(); // ftp client has to reconnected to get this to work
       connect();

        if (!ftpClient.changeWorkingDirectory(remoteDirectory))
        {
            String reply = "No additional information available.";
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
            {
                reply = ftpClient.getReplyString();
            }
            throw new IOException("Could not change directory to \"" + remoteDirectory + "\" " + reply);
        }
        return ftpClient.listNames();
    }
	
	public boolean isConnected()
	{
		long currentTime = System.currentTimeMillis();

		if (ftpClient.isConnected() && (currentTime - lastActiveTime >= FTP_IDLE_TIMEOUT_TIME))
		{
			try
			{
				ftpClient.disconnect();
			}
			catch (Exception e)
			{
				LOG.warn("Exception thrown while trying to disconnect FTP client!:" + ExceptionUtils.getFullStackTrace(e));
			}
		}
		else
		{
			lastActiveTime = currentTime;
		}

		return ftpClient.isConnected();
	}
	
	public void disconnect(){
		try
		{
			ftpClient.disconnect();
		}
		catch (IOException e)
		{
			LOG.warn("Exception thrown while trying to disconnect FTP client!:" + ExceptionUtils.getFullStackTrace(e));
		}
	}
	
	class CompletePendingCommandInputStream extends BaseInputStream
    {
        private FTPClient ftpClient;
        private String remoteDirectory;
        private String sourceFileName;
        
		public CompletePendingCommandInputStream(InputStream in, String remoteDirectory, String sourceFileName, FTPClient ftpClient)
        {
            super(in);
            this.ftpClient = ftpClient;
            this.remoteDirectory = remoteDirectory;
            this.sourceFileName = sourceFileName;
        }

        public int read(byte[] buffer, int offset, int length) throws IOException
        {
            int bytesRead = 0;
            try
            {
                bytesRead = in.read(buffer, offset, length);
                if (-1 == bytesRead)
                {
                    in.close();
                    if (!ftpClient.completePendingCommand())
                    {
                        String reply = "no additional information available.";
                        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
                        {
                            reply = ftpClient.getReplyString();
                        }
                        throw new IOException("could not get \"" + sourceFileName + "\" from \"" + remoteDirectory + "\" on " + serverName + ": " + reply);
                    }
                }
                return bytesRead;
            }
            catch (IOException e)
            {
                String reply = e.getMessage();
                if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
                {
                    reply = ftpClient.getReplyString();
                }
                throw new IOException("could not get \"" + sourceFileName + "\" from \"" + remoteDirectory + "\" on " + serverName + ": " + reply);
            }
        }
    }
        
	public long getLastActiveTime()
	{
		return lastActiveTime;
	}
	
	public void setLastActiveTime(long lastActiveTime)
	{
		this.lastActiveTime = lastActiveTime;
	}
	public String getServerName()
	{
		return serverName;
	}
	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public FTPClient getFtpClient()
	{
		return ftpClient;
	}
	public void setFtpClient(FTPClient ftpClient)
	{
		this.ftpClient = ftpClient;
	}

}

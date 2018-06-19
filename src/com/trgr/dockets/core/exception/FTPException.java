/**
 * 
 */
package com.trgr.dockets.core.exception;

/**
 * A custom exception whenever FTP operation fails.
 * 
 * @author C047166
 *
 */
public class FTPException extends Exception
{
	private static final long serialVersionUID = -4649592175583982923L;

	public FTPException()
    {
    }

    public FTPException(String message)
    {
        super(message);
    }

    public FTPException(String message, Throwable throwable)
    {
        super(message, throwable);
    }

    public FTPException(Throwable throwable)
    {
        super(throwable);
    }
}

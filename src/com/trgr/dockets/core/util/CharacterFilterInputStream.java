/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CharacterFilterInputStream extends FilterInputStream
{
    public CharacterFilterInputStream(InputStream in)
    {
        super(new BufferedInputStream(in, 1024));
    }

    public  CharacterFilterInputStream(InputStream in, boolean debug)
    {

        super(new BufferedInputStream(in, 1024));
        this.debug = true;
    }

    protected void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    public int read() throws IOException
    {
        int bytesRead = -1;
        while (0 == (bytesRead = this.read(this.oneByteArray, 0, 1))) ;

        if (-1 == bytesRead)
        {
            return -1;
        }
        else
        {
            return (int) this.oneByteArray[0] & 0xFF;
        }
    }

    public int read(byte b[]) throws IOException
    {
        return this.read(b, 0, b.length);
    }

    public int read(byte buffer[], int offset, int length) throws IOException
    {
        int bytesRead = 0;
        boolean endOfStream = false;
        //byte character = -1;
        int character = -1;


        while (bytesRead < length && !endOfStream)
        {

            character = in.read();

            switch (character)
            {
                case -1:
                    {
                        endOfStream = true;
                        break;
                    }
                default:
                    {
                    	// only omit the unprintable characters.  We still want to process the ones from 127 to 255
                        if ((character >=0 && character <= 31) && (character != 9 && character != 10 && character != 13))
                        {
                            if (debug)
                            {
                                if (!invalidChars.contains(new Integer(character)))
                                    invalidChars.add(new Integer(character));
                            }
                            buffer[offset + bytesRead] = (byte) ' ';
                            bytesRead++;
                        }
                        else
                        {
                            buffer[offset + bytesRead] = (byte) character;
                            bytesRead++;
                        }
                    }
            }

        }

        if (endOfStream && (0 == bytesRead))
        {
            bytesRead = -1;
        }
        return bytesRead;
    }

    protected String[] getInvalidCharacters() throws Exception
    {
        if (debug)
        {
            arrInvalidChars = new String[invalidChars.size()];
            arrInvalidChars = (String[]) invalidChars.toArray(arrInvalidChars);
            return arrInvalidChars;
        }
        else
        {
            throw new Exception("Debug use only:");
        }
    }

    private byte[] oneByteArray = new byte[1];
    boolean debug = false;
    List<Integer> invalidChars = new ArrayList<Integer>();
    //Integer[] arrInvalidChars = null;
    String[] arrInvalidChars = null;
}



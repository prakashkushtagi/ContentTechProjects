/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

import com.west.aqueduct.StreamHelper;
import junit.framework.TestCase;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: U0030947
 * Date: Jun 9, 2004
 * Time: 12:08:41 PM
 * To change this template use Options | File Templates.
 */
public class CharacterFilterInputStreamTest extends TestCase
{
    public CharacterFilterInputStreamTest(String s)
    {
        super(s);
    }

    public void testInputStreamWithControlCharacters() throws Exception
    {
        String input = "<dockets>" +
                "<docket>" +
                "<r r2=\"" + (char) 0x12 + "\">" +
                "<matched.firm.name>NEMECK" + (char) 0x03 + (char) 0x1C + "COLE</matched.firm.name>" +
                "<matched.firm.name>SERVICE: ADDRESS SERVICE TO: 419 BROOKSIDE</matched.firm.name>" +
                "</r>" +
                "</docket>" +
                "</dockets>";

        CharacterFilterInputStream filter = new CharacterFilterInputStream(new ByteArrayInputStream(input.getBytes()), true);

        String actual = StreamHelper.toString(filter);

        //String[] invalidChars = filter.getInvalidCharacters();
        //for (int i=0;i<invalidChars.length;i++)
        //{
        //    System.out.println("Invalid chars: " + invalidChars[i]);
        //}

        String expected = "<dockets><docket><r r2=\" \">" +
                "<matched.firm.name>NEMECK  COLE</matched.firm.name>" +
                "<matched.firm.name>SERVICE: ADDRESS SERVICE TO: 419 BROOKSIDE</matched.firm.name>" +
                "</r></docket></dockets>";
        System.out.println("Expected: " + expected);
        System.out.println("Actual:   " + actual);
        assertEquals(expected, actual);
    }

//    public void testInputStreamForControlCharacters() throws Exception
//    {
//        InputStream input = ClassPathHelper.openStream("testdata", "output54429.xml");
//        CharacterFilterInputStream filter = new CharacterFilterInputStream(input, true);
//        int bytesRead = 0;
//        while ( (bytesRead = filter.read()) != -1);
//        String[] invalidChars = filter.getInvalidCharacters();
//        for (int i=0;i<invalidChars.length;i++)
//        {
//            System.out.println("Invalid chars: " + invalidChars[i]);
//        }
//    }



    public void testInputStreamForASCIIRange127to255() throws Exception
    {
        String expected = "< ????????????????????????????????                                                                                                >";
        String input =  "";

        for (int i=127;i<=255;i++)
        {
            input += (char) i;
        }

        CharacterFilterInputStream filter = new CharacterFilterInputStream(new ByteArrayInputStream(input.getBytes()), true);
        String actual = "<" + StreamHelper.toString(filter) + ">";

System.out.println("Input:    " + "<" + input + ">");
System.out.println("Expected: " + expected);
System.out.println("Actual:   " + actual);
        assertEquals(expected.length(), actual.length());
    }
    /*
    public void testInputStreamWith0xFF() throws Exception
    {
        String input = "<dockets>" +
                "<docket>" +
                "<r>" +
                "<matched.firm.name>NEMECK" + (char) 0xAC + (char) 0xFF + "COLE</matched.firm.name>" +
                "<matched.firm.name>SERVICE: ADDRESS SERVICE TO: 419 BROOKSIDE</matched.firm.name>" +
                "</r>" +
                "</docket>" +
                "</dockets>";

        CharacterFilterInputStream filter = new CharacterFilterInputStream(new ByteArrayInputStream(input.getBytes()), true);

        String actual = StreamHelper.toString(filter);

        Integer[] invalidChars = filter.getInvalidCharacters();

        //assertEquals(5, invalidChars.length);
        String expected = "<dockets><docket><r>" +
                "<matched.firm.name>NEMECK    COLE</matched.firm.name>" +
                "<matched.firm.name>SERVICE: ADDRESS SERVICE TO: 419 BROOKSIDE</matched.firm.name>" +
                "</r></docket></dockets>";
        System.out.println("Expected: " + expected);
        System.out.println("Actual:   " + actual);
        assertEquals(expected, actual);
    }
    */
//    public void testInputFileWithControlCharacters() throws Exception
//    {
//        CharacterFilterInputStream filter = new CharacterFilterInputStream(ClassPathHelper.openStream("testdata", "output54429.xml"), true);
//        String[] invalidChars = filter.getInvalidCharacters();
//
//        for (int i=0;i<invalidChars.length;i++)
//        {
//            System.out.println("Invalid character: " + invalidChars[i]);
//        }
//    }
}
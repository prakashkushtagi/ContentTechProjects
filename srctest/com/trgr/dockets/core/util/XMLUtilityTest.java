/*
 * Copyright 2009 Thomson Global Resources.  All Rights Reserved.  Proprietary
 * and Confidential information of TGR.  Disclosure, Use or Reproduction without
 * the written authorization of TGR is prohibited.
 */
package com.trgr.dockets.core.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * XMLUtilityTest.
 * Test class to test XMLUtility
 * @author u0092513
 */
public class XMLUtilityTest
{
   private String elementName1;
   private String elementName2;
   private String value1;
   private String value2;
   private String expectedreturnedValue1;
   private String empty;
   private StringBuffer strBuf ;
   private String[] stringArray  = {"", ""} ;
   
   @Before
   public void setUp() throws Exception
   {
      elementName1 = "testelement1";
      value1 = "testvalue1";
      elementName2 = "testelement2";
      value2 = "testvalue2";
      expectedreturnedValue1 = "<" + elementName1 + ">" + value1 + "</" + elementName1 + ">";
      empty = "";
   }
   
   @Test
   public void testCreateElementStringString()
   {
      Assert.assertEquals(expectedreturnedValue1, XMLUtility.createElement(elementName1, value1));
      //testing for null
      Assert.assertEquals(empty, XMLUtility.createElement(elementName1, null));
      
   }
   
   @Test
   public void testCreateElementStringStringStringBuffer()
   {
      strBuf = new StringBuffer();
      XMLUtility.createElement(elementName1, value1, strBuf);
      Assert.assertEquals(expectedreturnedValue1, strBuf.toString());
     //testing for null value
      strBuf = new StringBuffer();
      XMLUtility.createElement(elementName1, null, strBuf);
      Assert.assertEquals(empty, strBuf.toString());
      //testing for value with zero length.
      strBuf = new StringBuffer();
      XMLUtility.createElement(elementName1, null, strBuf);
      Assert.assertEquals(empty, strBuf.toString());
    }
   
   @Test
   public void testCreateElementStringStringStringBuffer2()
   {
      strBuf = new StringBuffer();
      XMLUtility.createElement(elementName1, value1, true, strBuf);
      Assert.assertEquals(expectedreturnedValue1, strBuf.toString());
     //testing for null value
      strBuf = new StringBuffer();
      XMLUtility.createElement(elementName1, null, true, strBuf);
      Assert.assertEquals(empty, strBuf.toString());
      //testing for value with zero length.
      strBuf = new StringBuffer();
      XMLUtility.createElement(elementName1, null, false, strBuf);
      Assert.assertEquals(empty, strBuf.toString());
    }
   
   @Test
   public void testCreateElementsStringStringStringArrayBooleanStringBuffer()
   {
      
      strBuf = new StringBuffer();
      stringArray[0] = value1;
      stringArray[1] = value2;
      
      XMLUtility.createElements(elementName1, elementName2, stringArray, true, strBuf);
//    System.out.println(strBuf);
      Assert.assertEquals("<testelement1><testelement2>testvalue1</testelement2><testelement2>testvalue2</testelement2></testelement1>", strBuf.toString());
     //testing for null value
      strBuf = new StringBuffer();
      String[] strArray = new String[0];
      XMLUtility.createElements(elementName1, elementName2, strArray, true, strBuf);
      Assert.assertEquals(empty, strBuf.toString());
      //testing for value with zero length.
      strBuf = new StringBuffer();
      XMLUtility.createElement(elementName1, null, strBuf);
      Assert.assertEquals(empty, strBuf.toString());
   }
   
   @Test
   public void testCreateElementsString()
   {
      strBuf = new StringBuffer();
      String element = XMLUtility.createElements(elementName1);
      Assert.assertEquals(element, "<testelement1></testelement1>");
   }
   
   @Test
   public void testCreateElementsStringStringBuffer()
   {
      String element = XMLUtility.createElement(elementName1, value1, true);
      Assert.assertEquals(element, "<testelement1>testvalue1</testelement1>");
      
      element = XMLUtility.createElement(elementName1, null, false);
      Assert.assertEquals(element, "");
      
      element = XMLUtility.createElement(elementName1, value1, false);
      Assert.assertEquals(element, "<testelement1>testvalue1</testelement1>");
   }
   
   @Test
   public void testCreateElementsStringStringStringArrayBooleanStringBuffer_NullTags()
   {
      stringArray[0] = value1;
      stringArray[1] = value2;
      
      String element = XMLUtility.createElements(elementName1, elementName2, stringArray, true);

      Assert.assertEquals("<testelement1><testelement2>testvalue1</testelement2><testelement2>testvalue2</testelement2></testelement1>", element);
     //testing for null value
      
      String[] strArray = new String[0];
      element = XMLUtility.createElements(elementName1, elementName2, strArray, false);
      Assert.assertEquals(empty, element);
   }
   
   @Test
   public void testCreateElementsStringStringStringArrayBooleanStringBuffer_NullTags2()
   {
      
      strBuf = new StringBuffer();
      stringArray[0] = value1;
      stringArray[1] = value2;
      
      XMLUtility.createElements(elementName1, elementName2, stringArray, strBuf, true);
//    System.out.println(strBuf);
      Assert.assertEquals("<testelement1><testelement2>testvalue1</testelement2><testelement2>testvalue2</testelement2></testelement1>", strBuf.toString());
     //testing for null value
      strBuf = new StringBuffer();
      String[] strArray = new String[0];
      XMLUtility.createElements(elementName1, elementName2, strArray, strBuf, false);
      Assert.assertEquals(empty, strBuf.toString());
   }
   
   @Test
   public void testCreateElementsString_NullTag()
   {      
      String element = XMLUtility.createElements(elementName1, true);
      Assert.assertEquals(element, "<testelement1></testelement1>");
   }
   
   @Test
   public void testCreateElementsString_NullTag2()
   {
      strBuf = new StringBuffer();
      XMLUtility.createElements(elementName1, strBuf, true);
      Assert.assertEquals(strBuf.toString(), "<testelement1></testelement1>");
   }
   
 
   @Test
   public void testCreateElementWithAttributes()
   {
      strBuf = new StringBuffer();
      Map<String,String> attributes = new HashMap<String,String>();
      attributes.put("attribute1", "attributeValue1");
      attributes.put("attribute2", "attributeValue2");
      XMLUtility.createOpenElementWithAttributes(elementName1, attributes, strBuf, true);
      Assert.assertEquals(strBuf.toString(), "<testelement1 attribute1=\"attributeValue1\" attribute2=\"attributeValue2\">");
   }
   
   @Test
   public void testCreateElementWithAttributesAndValue()
   {
      strBuf = new StringBuffer();
      Map<String,String> attributes = new HashMap<String,String>();
      attributes.put("attribute1", "attributeValue1");
      attributes.put("attribute2", "attributeValue2");
      XMLUtility.createElementWithAttributes(elementName1, value1, attributes, strBuf, true);
      Assert.assertEquals(strBuf.toString(), "<testelement1 attribute1=\"attributeValue1\" attribute2=\"attributeValue2\">testvalue1</testelement1>");
   }
   
   @Test
   public void testCreateElementWithAttributes_NullTags()
   {
      strBuf = new StringBuffer();
      Map<String,String> attributes = new HashMap<String,String>();
      attributes.put("attribute1", "attributeValue1");
      attributes.put("attribute2", "attributeValue2");
      XMLUtility.createElementWithAttributes(elementName1, value1, attributes, strBuf, true, true);
      Assert.assertEquals(strBuf.toString(), "<testelement1 attribute1=\"attributeValue1\" attribute2=\"attributeValue2\">testvalue1</testelement1>");
      
      strBuf = new StringBuffer();
      attributes = new HashMap<String,String>();
      attributes.put("attribute1", "attributeValue1");
      attributes.put("attribute2", "attributeValue2");
      XMLUtility.createElementWithAttributes(elementName1, null, attributes, strBuf, true, true);
      Assert.assertEquals(strBuf.toString(), "<testelement1 attribute1=\"attributeValue1\" attribute2=\"attributeValue2\"/>");

      strBuf = new StringBuffer();
      attributes = new HashMap<String,String>();      
      XMLUtility.createElementWithAttributes(elementName1, null, attributes, strBuf, false, true);
      Assert.assertEquals(strBuf.toString(), "");

   }
   
   @Test
   public void testCreateElementWithAttribute()
   {
      strBuf = new StringBuffer();
      
      XMLUtility.createElementWithAttribute(elementName1, value1, "attribute1", "attributeValue1", strBuf, true);
      Assert.assertEquals(strBuf.toString(), "<testelement1 attribute1=\"attributeValue1\">testvalue1</testelement1>");
   }
   
   @Test
   public void testCreateOpenElementWithAttribute()
   {
      strBuf = new StringBuffer();
      
      XMLUtility.createOpenElementWithAttribute(elementName1, "attribute1", "attributeValue1", strBuf, true);
      Assert.assertEquals(strBuf.toString(), "<testelement1 attribute1=\"attributeValue1\">");
   }
   
   /*  
   @Test
   public void testCreateElementStringStringStringBufferBoolean()
   {
      fail("Not yet implemented"); // TODO
   }
   
   @Test
   public void testCreateElementsStringStringStringArrayBoolean()
   {
      fail("Not yet implemented"); // TODO
   }
   
   @Test
   public void testCreateElementsStringStringStringArrayStringBufferBoolean()
   {
      fail("Not yet implemented"); // TODO
   }
   
   @Test
   public void testCreateElementsStringBoolean()
   {
      fail("Not yet implemented"); // TODO
   }
   
   @Test
   public void testCreateElementsStringStringBufferBoolean()
   {
      fail("Not yet implemented"); // TODO
   }
   
   @Test
   public void testCreateOpenElementStringStringBuffer()
   {
      fail("Not yet implemented"); // TODO
   }
   
   @Test
   public void testCreateOpenElementWithAttribute()
   {
      fail("Not yet implemented"); // TODO
   }
   
   @Test
   public void testCreateOpenElementWithAttributes()
   {
      fail("Not yet implemented"); // TODO
   }
   
   @Test
   public void testCreateOpenElementString()
   {
      fail("Not yet implemented"); // TODO
   }
   
   @Test
   public void testCreateCloseElementString()
   {
      fail("Not yet implemented"); // TODO
   }
   
   @Test
   public void testCreateCloseElementStringStringBuffer()
   {
      fail("Not yet implemented"); // TODO
   }
   
   @Test
   public void testCreateElementWithAttributesStringStringMapStringBufferBoolean()
   {
      fail("Not yet implemented"); // TODO
   }
   
   @Test
   public void testCreateElementWithAttributesStringStringMapStringBufferBooleanBoolean()
   {
      fail("Not yet implemented"); // TODO
   }
   
   @Test
   public void testAppendAttributes()
   {
      fail("Not yet implemented"); // TODO
   }
   
   @Test
   public void testCreateElementWithAttribute()
   {
      fail("Not yet implemented"); // TODO
   }
   
   @Test
   public void testCreateDirectory()
   {
      fail("Not yet implemented"); // TODO
   }
   */
}

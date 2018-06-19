/*
Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
/*
 * Created on Jul 28, 2006
 * 
 * Oct 25, 2006 - David Kerman - Overriding methods added which allow the user to set whether or not
 *                               elements with null values should still have tags created.
 */
package com.trgr.dockets.core.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



/**
 * This is separate from Prism's DOM Utils because the Authority Service needed slightly
 * different treatment of null and empty strings.  The Authority Service can't use
 * the DOM Utils in NSA-West because it would create a dependancy cycle.
 * 
 * @author Jason Terhune
 */
public class XMLUtility {
   
   /**
    * "Do not create null tags" indicator.
    */
   public static final boolean CREATE_NULL_TAGS_FALSE = false;

      /**
       * This class can't be instantiated
       */
      private XMLUtility() {
            
      }


      /**
       * Creates an XML element using the specified name and value.  If value is null, this
       * returns an empty string.
       * @param elementName
       * @param value
       * @param escapeValues
       * @return String
       */
      public static String createElement(String elementName, String value) {
            
            if (value == null) {
                  return "";
            }
            
            StringBuffer element = new StringBuffer();
            
            element.append(createOpenElement(elementName));
            
            element.append(value);
            
            
            element.append(createCloseElement(elementName));
            
            return element.toString();
      }
      

      /**
       * Creates an XML element using the specified name and value.  If value is null, this
       * returns an empty string.
       * @param elementName
       * @param value
       * @param escapeValues
       * @param buf
       */
      public static void createElement(String elementName, String value,  StringBuffer buf) {
            
            if (value == null || value.length() == 0) {
                  return;
            }
            createOpenElement(elementName, buf);
            
            buf.append(value);
            
            
            createCloseElement(elementName, buf);
      }

      

      /**
       * Creates an XML element using the specified name and value.  If value is null, this
       * returns an empty string.
       * @param elementName
       * @param value
       * @param escapeValues
       * @param buf
       */
      public static void createElement(String elementName, String value, boolean escapevalues,  StringBuffer buf) {
            
            if (value == null || value.length() == 0) 
            {
                  return;
            }
            createOpenElement(elementName, buf);
            if(escapevalues)
            {
                value = value.replaceAll("&","&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&apos;");
            }
            buf.append(value);
            createCloseElement(elementName, buf);
      }      
      
      /**
       * Creates nested XML that represents a String array.
       * @param outerElementName
       * @param innerElementName
       * @param values
       * @param escapeValues if true, characters such as >, < and & will be translted into entities
       * @param buf
       */
      public static void createElements(String outerElementName, String innerElementName, String[] values, boolean escapeValues, StringBuffer buf) {
            if ((values == null) || (values.length == 0)) {
                  return ;
            }
            
            
            
            createOpenElement(outerElementName, buf);
            
            // Append each element in the array
            for (int i=0; i < values.length; i++) {

                  // Only create an element if it's non-null
                  if (values[i] != null && values[i].length() > 0) {
                        createElement(innerElementName, values[i], buf);
                  }
            }
            
            createCloseElement(outerElementName, buf);
                  }

      /**
       * Creates nested XML that represents an array.  The individual elements are converted
       * to XML using the XMLSerializableBean.toXMLString() method.
       * @param outerElementName
       * @param values
       * @return String
       */
      public static String createElements(String outerElementName) {
            
            
            
            StringBuffer elements = new StringBuffer();
            
            createElements(outerElementName, elements);
            
            return elements.toString();
      }
      /**
       * Creates nested XML that represents an array.  The individual elements are converted
       * to XML using the XMLSerializableBean.toXMLString() method.
       * @param outerElementName
       * @param values
       * @param buf
       */
      public static void createElements(String outerElementName, StringBuffer buf) {
            
            
            
            createOpenElement(outerElementName, buf);
            
            
            
            createCloseElement(outerElementName, buf);

      }
      
      /**
       * Creates an XML element using the specified name and value.  If value is null, this
       * returns an empty string.
       * @param elementName
       * @param value
       * @param escapeValues
       * @param createNullTags
       * @return String
       */
      public static String createElement(String elementName, String value, boolean createNullTags) {
            
            if ((value == null || value.length() == 0) && !createNullTags) {
                  return "";
            }
            
            StringBuffer element = new StringBuffer();
            
            element.append(createOpenElement(elementName));
            if(value != null)
            {
                  
                        element.append(value);
            
            }
            
            element.append(createCloseElement(elementName));
            
            return element.toString();
      }

      /**
       * Creates an XML element using the specified name and value.  If value is null, this
       * returns an empty string.
       * @param elementName
       * @param value
       * @param escapeValues
       * @param buf
       * @param createNullTags
       */
      public static void createElement(String elementName, String value, StringBuffer buf, boolean createNullTags) {
            if(value == null || value.trim().length() == 0){
               if(createNullTags){
                  buf.append('<');
                  buf.append(elementName);
                  buf.append("/>");
               } else {
                  // ignore all tags
               }
               
            } else {
               
               createOpenElement(elementName, buf);
               
               if( value != null)
               {
                 
                     buf.append(value);
                  
               }
               
               createCloseElement(elementName, buf);}
      }

      /**
       * Creates nested XML that represents a String array.
       * @param outerElementName
       * @param innerElementName
       * @param values
       * @param escapeValues if true, characters such as >, < and & will be translted into entities
       * @param createNullTags
       * @return String
       */
      public static String createElements(String outerElementName, String innerElementName, String[] values, boolean createNullTags) {
            if (((values == null) || (values.length == 0))&& !createNullTags) {
                  return "";
            }
            
            StringBuffer elements = new StringBuffer();
            
            elements.append(createOpenElement(outerElementName));
            
            if(values != null)
            {
                  // Append each element in the array
                  for (int i=0; i < values.length; i++) {
                     if(values[i] != null)
                     {
                        elements.append(createElement(innerElementName, values[i], createNullTags));
                     }
                  }
            }
            
            elements.append(createCloseElement(outerElementName));
            
            return elements.toString();
      }

      /**
       * Creates nested XML that represents a String array.
       * @param outerElementName
       * @param innerElementName
       * @param values
       * @param escapeValues if true, characters such as >, < and & will be translted into entities
       * @param buf
       * @param createNullTags
       */
      public static void createElements(String outerElementName, String innerElementName, String[] values, StringBuffer buf, boolean createNullTags) {
            if (((values == null) || (values.length == 0)) && !createNullTags) {
                  return ;
            }
            
            createOpenElement(outerElementName, buf);

            if(values != null)
            {
                  // Append each element in the array
                  for (int i=0; i < values.length; i++) {
      
                        // Only create an element if it's non-null
                        if (values[i] != null) {
                              createElement(innerElementName, values[i], buf, createNullTags);
                        }
                  }
            }
            
            createCloseElement(outerElementName, buf);
      }

      /**
       * Creates nested XML that represents an array.  The individual elements are converted
       * to XML using the XMLSerializableBean.toXMLString() method.
       * @param outerElementName
       * @param values
       * @param createNullTags
       * @return String
       */
      public static String createElements(String outerElementName,  boolean createNullTags) {
            
            
            
            StringBuffer elements = new StringBuffer();
            
            createElements(outerElementName,  elements);
            
            return elements.toString();
      }
      /**
       * Creates nested XML that represents an array.  The individual elements are converted
       * to XML using the XMLSerializableBean.toXMLString() method.
       * @param outerElementName
       * @param values
       * @param buf
       * @param createNullTags
       */
      public static void createElements(String outerElementName,  StringBuffer buf, boolean createNullTags) {
            
            
            
            createOpenElement(outerElementName, buf);
            
            
            createCloseElement(outerElementName, buf);

      }
      
      
      /**
       * Returns an XML starting tag with the specified name.
       * @param name
       * @param buf
       */
      public static void createOpenElement(String name, StringBuffer buf) {
            buf.append("<");
            buf.append(name);
            buf.append(">");
      }
      
      /**
       * Returns an XML starting tag with the specified name.
       * @param name
       * @param attributeName
       * @param attributeValue
       * @param xmlBuffer
       * @param encode
       */
      public static void createOpenElementWithAttribute(String name,
            String attributeName,
            String attributeValue,
            StringBuffer xmlBuffer,
            boolean encode) {
         Map<String, String> attributes = new HashMap<String, String>(1, 1);
         if(attributeValue == null){
            createOpenElement(name, xmlBuffer);
         } else {
               attributes.put(attributeName, attributeValue);
               xmlBuffer.append("<");
               xmlBuffer.append(name);
               appendAttributes(attributes, xmlBuffer, encode);
               xmlBuffer.append(">");
         }
      }

      
      /**
       * Returns an XML starting tag with the specified name.
       * @param name
       * @param attributes
       * @param xmlBuffer
       * @param encode
       */
      public static void createOpenElementWithAttributes(String name,
            Map<String, String> attributes,
            StringBuffer xmlBuffer,
            boolean encode) {
         xmlBuffer.append("<");
         xmlBuffer.append(name);
         appendAttributes(attributes, xmlBuffer, encode);
         xmlBuffer.append(">");
      }

      /**
       * Returns an XML starting tag with the specified name.
       * @param name
       * @return StringBuffer
       */
      public static StringBuffer createOpenElement(String name) {
            StringBuffer tag = new StringBuffer();
            createOpenElement(name, tag);
            
            return tag; 
      }
      /**
       * Returns an XML ending tag with the specified name.
       * @param name
       * @return StringBuffer
       */
      public static StringBuffer createCloseElement(String name) {
            StringBuffer tag = new StringBuffer();
            createCloseElement(name, tag);
            return tag;
      }
      
      /**
       * Returns an XML ending tag with the specified name.
       * @param name
       * @param buf
       */
      public static void createCloseElement(String name, StringBuffer buf) {
            buf.append("</");
            buf.append(name);
            buf.append(">");        
      
      }
      
      /**
       * Creates an element with the specified attributes.
       * @param elementName
       * @param value
       * @param attributes
       * @param xmlBuffer
       * @param encode
       */
      public static void createElementWithAttributes(
            String elementName,
            String value,
            Map<String, String> attributes,
            StringBuffer xmlBuffer,
            boolean encode){
         createElementWithAttributes(
               elementName,
               value,
               attributes,
               xmlBuffer,
               false,
               encode);
      }

   /**
    * Creates an element with the specified attributes.
    * @param elementName
    * @param value
    * @param attributes
    * @param xmlBuffer
    * @param includeNullTags
    * @param encode
    */
   public static void createElementWithAttributes(
         String elementName,
         String value,
         Map<String, String> attributes,
         StringBuffer xmlBuffer,
         boolean includeNullTags,
         boolean encode){
      
      if(!includeNullTags
            && (value == null || value.length() == 0)
            && attributes.isEmpty()){
         // don't append anything because the value and attributes are blank
         return;
      }
      
      // start the opening tag
      xmlBuffer.append('<');
      xmlBuffer.append(elementName);
      appendAttributes(attributes, xmlBuffer, encode);
      // close the element
      if(value == null || value.trim().length() == 0){
         xmlBuffer.append("/>");
      } else {
         xmlBuffer.append('>');
         
            xmlBuffer.append(value);
         
         xmlBuffer.append("</");
         xmlBuffer.append(elementName);
         xmlBuffer.append('>');
      }
   }
   
   /**
    * @param attributes
    * @param xmlBuffer
    * @param encode
    */
   protected static void appendAttributes(Map <String, String> attributes,
         StringBuffer xmlBuffer,
         boolean encode){
      Iterator<String> attributeKeyIter = attributes.keySet().iterator();
      String attributeKey;
      String attributeValue;
      // start the opening tag
     
      // append attributes
      while(attributeKeyIter.hasNext()){
         attributeKey = (String)attributeKeyIter.next();
         attributeValue = (String)attributes.get(attributeKey);
         xmlBuffer.append(' ');
         xmlBuffer.append(attributeKey);
         xmlBuffer.append("=\"");
         
         xmlBuffer.append(attributeValue);
         
         xmlBuffer.append("\"");
      }
   }
      
      /**
       * Creates an element with the specified attribute.  An assumption is made
       * that a tag will ALWAYS be generated:  if there's an attribute, clearly it's
       * worth generating.
       * @param elementName
       * @param value
       * @param attributeName
       * @param attributeValue
       * @param xmlBuffer
       * @param encode
       */
      public static void createElementWithAttribute(String elementName,
            String value, 
            String attributeName, 
            String attributeValue, 
            StringBuffer xmlBuffer,
            boolean encode){
         Map<String, String> attributes = new HashMap<String, String>(4);
         attributes.put(attributeName, attributeValue);
         createElementWithAttributes(
               elementName,
               value,
               attributes,
               xmlBuffer,
               encode);
      }
      
      public static boolean createDirectory(String directoryPath)
      {
          // Create a directory; all non-existent ancestor directories are
          // automatically created
          boolean success = (new File(directoryPath)).mkdirs();
          if (!success) {
              return false;
          }
          return true;

      }
      
 }


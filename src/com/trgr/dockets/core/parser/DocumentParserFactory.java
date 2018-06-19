/* Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.parser;

import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;

/**
 * SourceDocumentFactory.
 * This class is a singleton.  This creates various document parser based on contenttype.
 * 
 */
public class DocumentParserFactory
{
   private static DocumentParserFactory _documentParserFactory = new DocumentParserFactory();
   
   /**
    * private default constructor (singleton) 
    */
   private DocumentParserFactory()
   {
   }
   
   /**
    * Gets singleton instance of factory.
    */
   public static DocumentParserFactory getInstance()
   {
      return _documentParserFactory;
   }
   
   
   /**
    * This method will create an instance of DocumentParser.
    * @return IDocumentParser
    */
   public IDocumentParser createDocumentParser(String productCode, String fileName, Long vendorId)
   {
	   IDocumentParser documentParser = null;
	   if (productCode != null && productCode.equals(ProductEnum.STATE.name()))
	   {
		   documentParser = new StateNovusDocketHandler(fileName, vendorId);//State - NY parser
	   }
	   else if (productCode != null && productCode.equalsIgnoreCase(ProductEnum.JPML.name()) || ProductEnum.FEDERAL.name().equalsIgnoreCase(productCode) 
			   || ProductEnum.DCT.name().equalsIgnoreCase(productCode))
	   {
		   documentParser = new FederalNovusDocketHandler(fileName, vendorId, productCode);//JPML
	   }
	   else if (productCode != null && productCode.equalsIgnoreCase(ProductEnum.FBR.name()))
	   {
		   documentParser = new BKRNovusDocumentParser(fileName);
	   }
	   return documentParser;
   }
}

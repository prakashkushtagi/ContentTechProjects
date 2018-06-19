/**
 * 
 */
package com.trgr.dockets.core.predocket;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.trgr.dockets.core.acquisition.AcquisitionRecord;
import com.trgr.dockets.core.acquisition.NewAcquisitionRecordHandler;

/**
 * @author C047166
 *
 */
public class NewAcquisitionRecordHandlerTest
{
	@Test
	public void testAcqParse_AcquiredDockets() throws ParserConfigurationException, SAXException, IOException{
		String acquisitionRecord = 
				"<new.acquisition.record " +
		        "dcs.receipt.id=\"70219216\" " +
		        "sender.id=\"Data Capture\" " +
		        "merged.file.name=\"n_dlawdct.5:2013cv00143\" " +
		        "merged.file.size=\"3123\" " +
		        "script.start.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
		        "script.end.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
		        "docket.type=\"predocket\" "+
		        "retrieve.type=\"daily\" > "+
		                        "<court westlaw.cluster.name=\"n_dlawdct\" acquisition.status=\"success\" court.type=\"DCT\"> "+
		                                "<acquired.dockets> "+
		                                        "<acquired.docket.status status=\"captured\"> "+
		                                                "<state.docket docket.number=\"5:2013cv00143\" filename=\"5:13cv00143\" case.type=\"cv\" filing.year=\"2013\"                                                                   			sequence.number=\"00143\" subfolder=\"\" filing.location=\"5\" subdivision=\"\"> "+
		                                                        "<docket.entry> "+
		"<pdf.file filename = \"N_DLAWDCT.5:13cv00143.N_DLAWDCT.pdf\" uuid=\"Ibf342410658411e28d0bb787f93a62ef\" /> "+
		                                                        "</docket.entry> "+
		                                                "</state.docket> "+
		                                        "</acquired.docket.status> "+
		                                "</acquired.dockets> "+
		                        "</court> "+
		"</new.acquisition.record> ";
		 NewAcquisitionRecordHandler handler = new NewAcquisitionRecordHandler();
		 SAXParserFactory factory = SAXParserFactory.newInstance();
		 SAXParser parser = factory.newSAXParser();
		 parser.parse(IOUtils.toInputStream(acquisitionRecord), handler);
		 AcquisitionRecord acRecord = handler.getAcquisitionRecord();
		 Assert.assertEquals("70219216", acRecord.getAcquisitionReceiptId());
		 Assert.assertEquals("n_dlawdct.5:2013cv00143", acRecord.getMergedFileName());
		 Assert.assertEquals(3123, acRecord.getMergedFileSize());
		 Assert.assertNotNull(acRecord.getScriptStartDate());
		 Assert.assertNotNull(acRecord.getScriptEndDate());
		 Assert.assertEquals("N_DLAWDCT", acRecord.getWestlawClusterName());
		 Assert.assertEquals("success", acRecord.getAcquisitionStatus());
		 Assert.assertEquals("DCT", acRecord.getCourtType());
		 Assert.assertEquals(1, acRecord.getAcquiredDocketsList().size());
		 Assert.assertEquals(0, acRecord.getDeletedDocketsList().size());
		 Assert.assertEquals("5:2013cv00143", acRecord.getAcquiredDocketsList().get(0).getDocketNumber());
		 Assert.assertEquals("5:13cv00143", acRecord.getAcquiredDocketsList().get(0).getFileName());
		 Assert.assertEquals("cv", acRecord.getAcquiredDocketsList().get(0).getCaseType());
		 Assert.assertEquals("2013", acRecord.getAcquiredDocketsList().get(0).getFilingYear());
		 Assert.assertEquals("00143", acRecord.getAcquiredDocketsList().get(0).getSequenceNumber());
		 Assert.assertEquals("5", acRecord.getAcquiredDocketsList().get(0).getFilingLocation());
	}
	
	@Test
	public void testAcqParse_AcquiredAndDeletedDockets() throws ParserConfigurationException, SAXException, IOException{
		String acquisitionRecord = 
				"<new.acquisition.record " +
		        "dcs.receipt.id=\"70219216\" " +
		        "sender.id=\"Data Capture\" " +
		        "merged.file.name=\"n_dlawdct.5:2013cv00143\" " +
		        "merged.file.size=\"3123\" " +
		        "script.start.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
		        "script.end.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
		        "docket.type=\"predocket\" "+
		        "retrieve.type=\"daily\" > "+
		                        "<court westlaw.cluster.name=\"n_dlawdct\" acquisition.status=\"success\" court.type=\"DCT\"> "+
		                                "<acquired.dockets> "+
		                                        "<acquired.docket.status status=\"captured\"> "+
		                                                "<state.docket docket.number=\"5:2013cv00143\" filename=\"5:13cv00143\" case.type=\"cv\" filing.year=\"2013\"                                                                   			sequence.number=\"00143\" subfolder=\"\" filing.location=\"5\" subdivision=\"\"> "+
		                                                        "<docket.entry> "+
		"<pdf.file filename = \"N_DLAWDCT.5:13cv00143.N_DLAWDCT.pdf\" uuid=\"Ibf342410658411e28d0bb787f93a62ef\" /> "+
		                                                        "</docket.entry> "+
		                                                "</state.docket> "+
		                                        "</acquired.docket.status> "+
		                                "</acquired.dockets> "+
		                                "<deleted.dockets> "+
                                        "<delete.docket.status status=\"captured\"> "+
                                                "<state.docket docket.number=\"5:2013cv00143\" filename=\"5:13cv00143\" case.type=\"cv\" filing.year=\"2013\"                                                                   			sequence.number=\"00143\" subfolder=\"\" filing.location=\"5\" subdivision=\"\"> "+
                                                        "<docket.entry> "+
"<pdf.file filename = \"N_DLAWDCT.5:13cv00143.N_DLAWDCT.pdf\" uuid=\"Ibf342410658411e28d0bb787f93a62ef\" /> "+
                                                        "</docket.entry> "+
                                                "</state.docket> "+
                                        "</delete.docket.status> "+
                                "</deleted.dockets> "+        
		                        "</court> "+
		"</new.acquisition.record> ";
		 NewAcquisitionRecordHandler handler = new NewAcquisitionRecordHandler();
		 SAXParserFactory factory = SAXParserFactory.newInstance();
		 SAXParser parser = factory.newSAXParser();
		 parser.parse(IOUtils.toInputStream(acquisitionRecord), handler);
		 AcquisitionRecord acRecord = handler.getAcquisitionRecord();
		 Assert.assertEquals("70219216", acRecord.getAcquisitionReceiptId());
		 Assert.assertEquals("n_dlawdct.5:2013cv00143", acRecord.getMergedFileName());
		 Assert.assertEquals(3123, acRecord.getMergedFileSize());
		 Assert.assertNotNull(acRecord.getScriptStartDate());
		 Assert.assertNotNull(acRecord.getScriptEndDate());
		 Assert.assertEquals("N_DLAWDCT", acRecord.getWestlawClusterName());
		 Assert.assertEquals("success", acRecord.getAcquisitionStatus());
		 Assert.assertEquals("DCT", acRecord.getCourtType());
		 Assert.assertEquals(1, acRecord.getAcquiredDocketsList().size());
		 Assert.assertEquals(1, acRecord.getDeletedDocketsList().size());
		 Assert.assertEquals("5:2013cv00143", acRecord.getAcquiredDocketsList().get(0).getDocketNumber());
		 Assert.assertEquals("5:13cv00143", acRecord.getAcquiredDocketsList().get(0).getFileName());
		 Assert.assertEquals("cv", acRecord.getAcquiredDocketsList().get(0).getCaseType());
		 Assert.assertEquals("2013", acRecord.getAcquiredDocketsList().get(0).getFilingYear());
		 Assert.assertEquals("00143", acRecord.getAcquiredDocketsList().get(0).getSequenceNumber());
		 Assert.assertEquals("5", acRecord.getAcquiredDocketsList().get(0).getFilingLocation());
		 Assert.assertEquals("5:2013cv00143", acRecord.getDeletedDocketsList().get(0).getDocketNumber());
		 Assert.assertEquals("5:13cv00143", acRecord.getDeletedDocketsList().get(0).getFileName());
		 Assert.assertEquals("cv", acRecord.getDeletedDocketsList().get(0).getCaseType());
		 Assert.assertEquals("2013", acRecord.getDeletedDocketsList().get(0).getFilingYear());
		 Assert.assertEquals("00143", acRecord.getDeletedDocketsList().get(0).getSequenceNumber());
		 Assert.assertEquals("5", acRecord.getDeletedDocketsList().get(0).getFilingLocation());
	}
}

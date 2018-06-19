/**Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package service;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.CoreConstants.AcquisitionMethod;
import com.trgr.dockets.core.CoreConstants.Phase;
import com.trgr.dockets.core.acquisition.service.AcquisitionRecordAndActivitySetService;
import com.trgr.dockets.core.content.service.SourceContentService;
import com.trgr.dockets.core.domain.SourceDocketMetadata;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.Product;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.service.ContentService;
import com.trgr.dockets.core.service.ContentVersionService;
import com.trgr.dockets.core.service.DocketHistoryService;
import com.trgr.dockets.core.service.DocketService;
import com.trgr.dockets.core.util.UUIDGenerator;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "DocketPersistence-context.xml" })
@Transactional
public class AcquisitionLogProcessingTest
{
	@Rule
    public TemporaryFolder folder= new TemporaryFolder();
	
	@Autowired
	private AcquisitionRecordAndActivitySetService acquisitionRecordAndActivitySetService;
	
	@Autowired
	SourceContentService sourceDocketContentServiceImpl;
	
	@Autowired
	private DocketService docketService;
	
	@Autowired
	private DocketHistoryService docketHistoryService;
	
	@Autowired
	private ContentVersionService contentVersionService;
	
	@Autowired
	private ContentService contentService;
	
	private String pathString;
	
	private PublishingRequest publishingRequest;

	
	String acquisitionRecord = 
			"<new.acquisition.record " +
	        "dcs.receipt.id=\"70219216\" " +
	        "sender.id=\"Data Capture\" " +
	        "merged.file.name=\"n_dlawdct.5:2013cv00143\" " +
	        "merged.file.size=\"3123\" " +
	        "script.start.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
	        "script.end.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
	        //"docket.type=\"predocket\" "+
	        "retrieve.type=\"daily\"> "+
	                        "<court westlaw.cluster.name=\"n_dintrial\" acquisition.status=\"success\" court.type=\"State\"> "+
	                        		"<acquired.dockets> "+
			                                "<acquired.docket.status status=\"captured\"> "+
			                                        "<state.docket docket.number=\"0501289/2013\" filename=\"KINGS20130501289\" subfolder=\"\" subdivision=\"\"> "+
			                                                "<docket.entry> "+
			                                                	"<pdf.file filename = \"N_DNYDOWNSTATE.5:13cv00143.N_DLAWDCT.pdf\" uuid=\"Ibf342410658411e28d0bb787f93a62ef\" /> "+
			                                                "</docket.entry> "+
			                                        "</state.docket> "+
			                                "</acquired.docket.status> "+
			                        "</acquired.dockets> "+
	        
	                        		"<skipped.dockets>"+
										"<skipped.dockets.status westget.stage=\"get_all\" status=\"missing\">"+
											"<state.docket docket.number=\"2015-TX-000146\" filename=\"2015-TX-000146\" case.type=\"CIVIL\" filing.year=\"2015\" subfolder=\"\" subdivision=\"Allen\" alt.docket.id=\"\"/>"+
										"</skipped.dockets.status>"+
										"<skipped.dockets.status westget.stage=\"get_all\" status=\"missing\">"+
											"<state.docket docket.number=\"2015-TX-000147\" filename=\"2015-TX-000147\" case.type=\"CIVIL\" filing.year=\"2015\" subfolder=\"\" subdivision=\"Allen\" alt.docket.id=\"\"/>"+
										"</skipped.dockets.status>"+
										"<skipped.dockets.status westget.stage=\"get_all\" status=\"missing\">"+
											"<state.docket docket.number=\"2015-TX-000148\" filename=\"2015-TX-000148\" case.type=\"CIVIL\" filing.year=\"2015\" subfolder=\"\" subdivision=\"Allen\" alt.docket.id=\"\"/>"+
										"</skipped.dockets.status>"+
										"<skipped.dockets.status westget.stage=\"get_all\" status=\"missing\">"+
											"<state.docket docket.number=\"2015-TX-000149\" filename=\"2015-TX-000149\" case.type=\"CIVIL\" filing.year=\"2015\" subfolder=\"\" subdivision=\"Allen\" alt.docket.id=\"\"/>"+
										"</skipped.dockets.status>"+
										"<skipped.dockets.status westget.stage=\"get_all\" status=\"missing\">"+
											"<state.docket docket.number=\"2015-TX-000150\" filename=\"2015-TX-000150\" case.type=\"CIVIL\" filing.year=\"2015\" subfolder=\"\" subdivision=\"Allen\" alt.docket.id=\"\"/>"+
										"</skipped.dockets.status>"+
									"</skipped.dockets>"+
										
									"<website.errors>"+
									    "<web.error.docket reason=\"court_error\" westget.stage=\"get_list\" status=\"failed_in_getlist\" curl.code=\"52\" http.error.code=\"\" court.error.message=\"search_failure\">"+
									        "<state.docket docket.number=\"2015-TX-000151\" filename=\"276450\" case.type=\"C\" filing.year=\"15\" subfolder=\"\" subdivision=\"\" alt.docket.id=\"\"/>"+
									    "</web.error.docket>"+
									    "<web.error.docket reason=\"court_error\" westget.stage=\"get_list\" status=\"failed_rescrape\" curl.code=\"28\" http.error.code=\"\" court.error.message=\"search_failure\">"+
									        "<state.docket docket.number=\"2015-TX-000152\" filename=\"276451\" case.type=\"C\" filing.year=\"15\" subfolder=\"\" subdivision=\"\" alt.docket.id=\"\"/>"+
									    "</web.error.docket>"+
										"<web.error.status reason=\"court_error\" westget.stage=\"get_list\" status=\"failed_in_getlist\" curl.code=\"9999\" http.error.code=\"100\" court.error.message=\"webget_search_error\"/>"+										    
									"</website.errors>"+
	                        "</court>"+
	"</new.acquisition.record> ";
	
	
	
	
	@Before
	public void setUp()
	{
		URL path = getClass().getResource("AcquisitionRecordAndActivitySetServiceTest.class");
		pathString = path.getPath();
		pathString = pathString.substring(0,pathString.indexOf("service"));
		pathString = pathString + "/resources/" + "00000951891.xml";
		
		
		Court court = new Court(108l);
		court.setCourtNorm("NY-SCT-DN");
		this.publishingRequest = new PublishingRequest(UUIDGenerator.createUuid());
		publishingRequest.setCourt(court);
		publishingRequest.setRequestName("thePubRequestName");
		publishingRequest.setDeleteOverride(true);
		publishingRequest.setProduct(new Product(ProductEnum.STATE));
	}

	@Test
	public void testProcessAcquisitionRecordFailedDocketHappyPath() throws Exception
	{
		acquisitionRecordAndActivitySetService.processFailedDocketRecord(acquisitionRecord);
		
		
		DocketEntity docketEntity_1 = docketService.findDocketByPrimaryKey("INTRIAL2015-TX-000146");
		Assert.assertEquals("N", docketEntity_1.getAcquired());
		Assert.assertEquals("N", docketEntity_1.getPublishFlag());
		Court court_1 = docketService.findCourtByPrimaryKey(108l);
		DocketVersion docketVersion_1  = docketService.findLatestDocketVersionForProductCourtLegacyIdPhase("INTRIAL2015-TX-000146", court_1, 3, Phase.ATTEMPTED);
		
		DocketEntity docketEntity_2 = docketService.findDocketByPrimaryKey("INTRIAL2015-TX-000147");
		Assert.assertEquals("N", docketEntity_2.getAcquired());
		Assert.assertEquals("N", docketEntity_2.getPublishFlag());
		Court court_2 = docketService.findCourtByPrimaryKey(108l);
		DocketVersion docketVersion_2  = docketService.findLatestDocketVersionForProductCourtLegacyIdPhase("INTRIAL2015-TX-000147", court_2, 3, Phase.ATTEMPTED);

		DocketEntity docketEntity_3 = docketService.findDocketByPrimaryKey("INTRIAL2015-TX-000148");
		Assert.assertEquals("N", docketEntity_3.getAcquired());
		Assert.assertEquals("N", docketEntity_3.getPublishFlag());
		Court court_3 = docketService.findCourtByPrimaryKey(108l);
		DocketVersion docketVersion_3  = docketService.findLatestDocketVersionForProductCourtLegacyIdPhase("INTRIAL2015-TX-000148", court_3, 3, Phase.ATTEMPTED);

		DocketEntity docketEntity_4 = docketService.findDocketByPrimaryKey("INTRIAL2015-TX-000149");
		Assert.assertEquals("N", docketEntity_4.getAcquired());
		Assert.assertEquals("N", docketEntity_4.getPublishFlag());
		Court court_4 = docketService.findCourtByPrimaryKey(108l);
		DocketVersion docketVersion_4  = docketService.findLatestDocketVersionForProductCourtLegacyIdPhase("INTRIAL2015-TX-000149", court_4, 3, Phase.ATTEMPTED);

		DocketEntity docketEntity_5 = docketService.findDocketByPrimaryKey("INTRIAL2015-TX-000150");
		Assert.assertEquals("N", docketEntity_5.getAcquired());
		Assert.assertEquals("N", docketEntity_5.getPublishFlag());
		Court court_5 = docketService.findCourtByPrimaryKey(108l);
		DocketVersion docketVersion_5  = docketService.findLatestDocketVersionForProductCourtLegacyIdPhase("INTRIAL2015-TX-000150", court_5, 3, Phase.ATTEMPTED);
		
		
		DocketEntity docketEntity_6 = docketService.findDocketByPrimaryKey("INTRIAL2015-TX-000151");
		Assert.assertEquals("N", docketEntity_6.getAcquired());
		Assert.assertEquals("N", docketEntity_6.getPublishFlag());
		Court court_6 = docketService.findCourtByPrimaryKey(108l);
		DocketVersion docketVersion_6  = docketService.findLatestDocketVersionForProductCourtLegacyIdPhase("INTRIAL2015-TX-000151", court_6, 3, Phase.ATTEMPTED);
		Assert.assertEquals("7", docketVersion_6.getAcquisitionStatus().toString());
		
		
		DocketEntity docketEntity_7 = docketService.findDocketByPrimaryKey("INTRIAL2015-TX-000152");
		Assert.assertEquals("N", docketEntity_7.getAcquired());
		Assert.assertEquals("N", docketEntity_7.getPublishFlag());
		Court court_7 = docketService.findCourtByPrimaryKey(108l);
		DocketVersion docketVersion_7  = docketService.findLatestDocketVersionForProductCourtLegacyIdPhase("INTRIAL2015-TX-000152", court_7, 3, Phase.ATTEMPTED);
		Assert.assertEquals("6", docketVersion_7.getAcquisitionStatus().toString());

		Assert.assertNotNull(docketVersion_1);		
		Assert.assertNotNull(docketVersion_2);
		Assert.assertNotNull(docketVersion_3);
		Assert.assertNotNull(docketVersion_4);
		Assert.assertNotNull(docketVersion_5);

		Assert.assertEquals("361", docketEntity_1.getCountyId().toString());
		Assert.assertEquals("2", docketVersion_1.getAcquisitionStatus().toString());
		Assert.assertEquals("1", docketVersion_1.getVersion().toString());

	}
	
	@Test
	public void testMissingThenFound() throws Exception{
		acquisitionRecordAndActivitySetService.processFailedDocketRecord(acquisitionRecord);
		
		DocketEntity docketEntity1 = docketService.findDocketByPrimaryKey("INTRIAL2015-TX-000146");
		Assert.assertEquals("N", docketEntity1.getAcquired());
		Assert.assertEquals("N", docketEntity1.getPublishFlag());
		
		DocketEntity docketEntity2 = docketService.findDocketByPrimaryKey("INTRIAL2015-TX-000147");
		Assert.assertEquals("N", docketEntity2.getAcquired());
		Assert.assertEquals("N", docketEntity2.getPublishFlag());

		DocketEntity docketEntity3 = docketService.findDocketByPrimaryKey("INTRIAL2015-TX-000148");
		Assert.assertEquals("N", docketEntity3.getAcquired());
		Assert.assertEquals("N", docketEntity3.getPublishFlag());

		List<SourceDocketMetadata> foundDocketList = new ArrayList<SourceDocketMetadata>();
		foundDocketList.add(createMockInMetadata("INTRIAL2015-TX-000146", "2015-TX-000146"));
		foundDocketList.add(createMockInMetadata("INTRIAL2015-TX-000148", "2015-TX-000148"));
		this.sourceDocketContentServiceImpl.loadDockets(foundDocketList);
		
		
		docketEntity1 = docketService.findDocketByPrimaryKey("INTRIAL2015-TX-000146");
		Assert.assertEquals("Y", docketEntity1.getAcquired());
		Assert.assertEquals("Y", docketEntity1.getPublishFlag());
		
		docketEntity2 = docketService.findDocketByPrimaryKey("INTRIAL2015-TX-000147");
		Assert.assertEquals("N", docketEntity2.getAcquired());
		Assert.assertEquals("N", docketEntity2.getPublishFlag());
		
		docketEntity3 = docketService.findDocketByPrimaryKey("INTRIAL2015-TX-000148");
		Assert.assertEquals("Y", docketEntity1.getAcquired());
		Assert.assertEquals("Y", docketEntity1.getPublishFlag());
						
	}
	
	@Test
	public void testProcessAcquisitionLogSkippedDCTErrors() throws Exception
	{
		String acquisitionRecord =
			"<new.acquisition.record " +
	        "dcs.receipt.id=\"120560908\" " +
	        "sender.id=\"Data Capture\" " +
	        "merged.file.name=\"PRFEDDCT.DAILY.NEW.9C.050317.100424.CIVIL.PROD.merged.1.xml.gz\" " +
	        "merged.file.size=\"130608\" " +
	        "script.start.date.time=\"Wed May  3 10:04:27 CDT 2017\" " +
	        "script.end.date.time=\"Wed May  3 11:42:27 CDT 2017\" " +
	        "retrieve.type=\"daily\" > " +
            "    <court westlaw.cluster.name=\"n_dilsdct\" acquisition.status=\"success\" court.type=\"DCT\"> " +
            "        <acquired.dockets> " +
            "            <acquired.docket.status status=\"captured\"> " +
            "                <dct.docket docket.number=\"3:17cv00459\" filename=\"3:17cv00459\" case.type=\"cv\" filing.year=\"2017\" sequence.number=\"00459\" subfolder=\"\" filing.location=\"3\" subdivision=\"\"/> " +
            "            </acquired.docket.status> " +
            "            <acquired.docket.status status=\"captured\"> " +
            "              	<dct.docket docket.number=\"3:17cv00460\" filename=\"3:17cv00460\" case.type=\"cv\" filing.year=\"2017\" sequence.number=\"00460\" subfolder=\"\" filing.location=\"3\" subdivision=\"\"/> " +
            "            </acquired.docket.status> " +
            "        </acquired.dockets> " +
            "    </court> " +
            "    <court westlaw.cluster.name=\"n_dohsdct\" acquisition.status=\"success\" court.type=\"DCT\"> " +
            "         <skipped.dockets> " +
            "            <skipped.dockets.status westget.stage=\"get_file\" status=\"missing\"> " +
            "                <dct.docket docket.number=\"2:17cv99999\" filename=\"n_dohsdct.2:17cv99999\" case.type=\"cv\" filing.year=\"2017\" sequence.number=\"00227\" subfolder=\"\" filing.location=\"2\" subdivision=\"\"/> " +
            "            </skipped.dockets.status> " +
            "            <skipped.dockets.status westget.stage=\"get_file\" status=\"missing\"> " +
            "                <dct.docket docket.number=\"2:17cv19999\" filename=\"n_dohsdct.2:17cv19999\" case.type=\"cv\" filing.year=\"2017\" sequence.number=\"00275\" subfolder=\"\" filing.location=\"2\" subdivision=\"\"/> " +
            "            </skipped.dockets.status> " +
            "        </skipped.dockets> " +
            "    </court> " +
            "    <court westlaw.cluster.name=\"n_dprdct\" acquisition.status=\"success\" court.type=\"DCT\"> " +
            "        <acquired.dockets> " +
            "            <acquired.docket.status status=\"captured\"> " +
            "                <dct.docket docket.number=\"3:17cv01578\" filename=\"3:17cv01578\" case.type=\"cv\" filing.year=\"2017\" sequence.number=\"01578\" subfolder=\"\" filing.location=\"3\" subdivision=\"\"/> " +
            "            </acquired.docket.status> " +
            "        </acquired.dockets> " +
            "    </court> " +
            "    <court westlaw.cluster.name=\"n_dwvsdct\" acquisition.status=\"success\" court.type=\"DCT\"> " +
            "        <acquired.dockets> " +
            "            <acquired.docket.status status=\"captured\"> " +
            "                <dct.docket docket.number=\"2:17cv02687\" filename=\"2:17cv02687\" case.type=\"cv\" filing.year=\"2017\" sequence.number=\"02687\" subfolder=\"\" filing.location=\"2\" subdivision=\"\"/> " +
            "            </acquired.docket.status> " +
            "        </acquired.dockets> " +
            "        <skipped.dockets> " +
            "            <skipped.dockets.status westget.stage=\"get_file\" status=\"missing\"> " +
            "                <dct.docket docket.number=\"2:17cv29999\" filename=\"n_dwvsdct.2:17cv29999\" case.type=\"cv\" filing.year=\"2017\" sequence.number=\"02203\" subfolder=\"\" filing.location=\"2\" subdivision=\"\"/> " +
            "            </skipped.dockets.status> " +
            "        </skipped.dockets> " +
            "    </court> " +
            "</new.acquisition.record>";

		acquisitionRecordAndActivitySetService.processFailedDocketRecord(acquisitionRecord);	
		
		Assert.assertTrue(true);
		//first docket
		DocketEntity docket = docketService.findDocketByPrimaryKey("OH-SDCT2:17CV99999");
		Assert.assertEquals("N", docket.getAcquired());
		Assert.assertEquals("N", docket.getPublishFlag());
		Assert.assertEquals("2:17-CV-99999", docket.getDocketNumber());
		Court court = docketService.findCourtByPrimaryKey(195l);
		DocketVersion docketVersion  = docketService.findLatestDocketVersionForProductCourtLegacyIdPhase("OH-SDCT2:17CV99999", court, 5, Phase.ATTEMPTED);
		Assert.assertNotNull(docketVersion);		
		Assert.assertEquals("2", docketVersion.getAcquisitionStatus().toString());
		Assert.assertEquals("1", docketVersion.getVersion().toString());
		//last docket
		docket = docketService.findDocketByPrimaryKey("WV-SDCT2:17CV29999");
		Assert.assertEquals("N", docket.getAcquired());
		Assert.assertEquals("N", docket.getPublishFlag());
		court = docketService.findCourtByPrimaryKey(224l);
		docketVersion  = docketService.findLatestDocketVersionForProductCourtLegacyIdPhase("WV-SDCT2:17CV29999", court, 5, Phase.ATTEMPTED);
		Assert.assertNotNull(docketVersion);		
		Assert.assertEquals("2", docketVersion.getAcquisitionStatus().toString());
		Assert.assertEquals("1", docketVersion.getVersion().toString());
	}
	
	private SourceDocketMetadata createMockInMetadata(String legacyId, String docketNumber){
		SourceDocketMetadata sdm = new SourceDocketMetadata(docketNumber);
		sdm.setPublishingRequest(publishingRequest);
		sdm.setLegacyId(legacyId);
		sdm.setVendorId(22); 
		sdm.setAcquisitionTimestamp(new Date());
		sdm.setDeleteOperation(false);
		sdm.setCaseTypeId(0L);
		sdm.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		sdm.setSourceFile(new File("TEST_SOURCE_FILENAME"));
		sdm.setDocketContentFile(new File(pathString));
		
		return sdm;
	}
	
	
}

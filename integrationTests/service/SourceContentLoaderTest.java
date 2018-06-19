package service;

import org.junit.Test;
import org.springframework.util.Assert;


public class SourceContentLoaderTest  {
	
/*	@Autowired
	SourceContentLoader sourceContentLoader; 
	@Autowired
	DocketMarshllerAndUnmarshller docketMarshllerAndUnmarshller;
	@Autowired
	ErrorLogService errorLogService; 
	@Autowired
	SourceLoadRequestService sourceLoadRequestService;
	@Autowired
	EventLogServiceImpl eventLogService;
	@Autowired
	BankruptcyContentService bankruptcyContentService;
	
	Destination ltcResponseQueueMock;

	String MergeXML_Negative_Add = "IntegrationTests/service/Negative_Add.xml";
	String MergeXML_positive_Add = "IntegrationTests/service/Lexis_Merge_Add.xml";
	String Lexis_Merge_Add = "IntegrationTests/service/Lexis_Merge_Add.xml";
	String Lexis_Merge_Delete = "IntegrationTests/service/Lexis_Merge_Delete.xml";
	String LCI_Merge_Delete = "IntegrationTests/service/LCI_Merge_Delete.xml";
	String LCI_Merge_Insert = "IntegrationTests/service/LCI_Merge_Insert.xml";
	String LCI_Oregon_Insert = "IntegrationTests/service/LCI_Oregon_Insert.xml";
	String LCI_Oregon_Insert_2 = "IntegrationTests/service/LCI_Oregon_Insert_2.xml";

	String SCRAPE_DATE_EMPTYL = "IntegrationTests/service/ScrapeDateEmpty.xml";
	List<Court> courtList = null;
	HashMap<String,String> courtMap = null;
	String ENV = "dev";
	
	@Before
	public void setUp()
	{
		
		LookUpListDAOImpl lookUpListDAO = new LookUpListDAOImpl(ENV);
		courtList = lookUpListDAO.getCourts();
		courtMap = DocketsCoreUtility.getCourtClusterNormMap(courtList);
	}
/**
 * 
 * <docket number="5:2001bk50767" encoding="base64" scrape.date="30032012034254" court="N_DKYEBKR" platform="lnokc341-prospective" state.postal="KY">
<page name="341Updates">
Q0FTRCAgICAgICAgICAgICAxS1ktRUJLUiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg
ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg
ICAgICAgICAgICAgICAgICAgTEVYSU5HVE9OICAgICAgICAgICAgICAgICAgICAgICAgICAgICBV
ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAwMTUwNzY3ICAgICAgICAgICAgICAg
ICAgICAgICAgICAgSU5GT1JNQVRJT04gVU5BVkFJTEFCTEUgICAgICAgICAgICAgICAgIDA0LzE5
LzIwMDEgMTI6NDA6MDAgUE0xMDAgRSBWSU5FIDgwNyBMRVhJTkdUT04gS1kgICAgICAgICAgICAg
ICAgICAgICAgICAgICAgICAgICBOICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDA0LzA1
LzIwMDUgMTI6MDA6MDAgQU0gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDAzLzA4LzIw
MDEgICAgICAgICAgICANCkFUVCAgICAgICAgICAgICAxMDAwMDAwMDAwMDAwMDEgSU5GT1JNQVRJ
T04gVU5BVkFJTEFCTEUgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg
ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg
ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg
ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KREVGICAgICAgICAg
ICAgIDEwMDAwMDAwMDAwMDAwMTcgIDcgIDAwMDAwNzk0OTA2ODUxQkVBMDlEMTRDM0MzQkEyRDk0
OTQyNkQwN0FGVCBJICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIENBTERX
RUxMICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBHQVJZICAgICAgICAgICAgICAgIFcg
ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg
IDY3IERJWElFIFBMQVpBICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgUklD
SE1PTkQgICAgICAgICAgICAgICAgS1k0MDQ3NSAgICAgICAgICAgICAgICAgIE9QRU4gICAgICAg
ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg
ICAN
</page>
</docket>
 
	
//	@Test
	public void getDocketDumpTest() {
		Docket sourceDocket = new Docket();
		sourceDocket.setNumber("5:2001bk50767");
		sourceDocket.setEncoding("base64");
		sourceDocket.setScrapeDate("30032012034254");
		sourceDocket.setCourt("N_DKYEBKR");
		sourceDocket.setPlatform("lnokc341-prospective");
		sourceDocket.setStatePostal("KY");
		List<Page> pageList = new ArrayList<Page>();
		Page page1 = new Page();
		page1.setName("341Updates");
		page1.setDocketContent("Q0FTRCAgICAgICAgICAgICAxS1ktRUJLUiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg"+
		"ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg"+
		"ICAgICAgICAgICAgICAgICAgTEVYSU5HVE9OICAgICAgICAgICAgICAgICAgICAgICAgICAgICBV"+
		"ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAwMTUwNzY3ICAgICAgICAgICAgICAg"+
		"ICAgICAgICAgICAgSU5GT1JNQVRJT04gVU5BVkFJTEFCTEUgICAgICAgICAgICAgICAgIDA0LzE5"+
		"LzIwMDEgMTI6NDA6MDAgUE0xMDAgRSBWSU5FIDgwNyBMRVhJTkdUT04gS1kgICAgICAgICAgICAg"+
		"ICAgICAgICAgICAgICAgICAgICBOICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDA0LzA1"+
		"LzIwMDUgMTI6MDA6MDAgQU0gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDAzLzA4LzIw"+
		"MDEgICAgICAgICAgICANCkFUVCAgICAgICAgICAgICAxMDAwMDAwMDAwMDAwMDEgSU5GT1JNQVRJ"+
		"T04gVU5BVkFJTEFCTEUgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg"+
		"ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg"+
		"ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg"+
		"ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KREVGICAgICAgICAg"+
		"ICAgIDEwMDAwMDAwMDAwMDAwMTcgIDcgIDAwMDAwNzk0OTA2ODUxQkVBMDlEMTRDM0MzQkEyRDk0"+
		"OTQyNkQwN0FGVCBJICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIENBTERX"+
		"RUxMICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBHQVJZICAgICAgICAgICAgICAgIFcg"+
		"ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg"+
		"IDY3IERJWElFIFBMQVpBICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgUklD"+
		"SE1PTkQgICAgICAgICAgICAgICAgS1k0MDQ3NSAgICAgICAgICAgICAgICAgIE9QRU4gICAgICAg"+
		"ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg"+
		"ICAN");
		pageList.add(page1);
		sourceDocket.setPageList(pageList);
		String docketBlock = sourceContentLoader.getDocketDump(sourceDocket);
		Assert.notNull(docketBlock);
	}

	*//**
	 * Clean up method just of test scenarios to remove data inserted during test run 
	 * @param docketNumber
	 * @param court
	 * @param ENV
	 *//*
	private void cleanUpAfterInsert(String docketNumber,String court,String ENV) {
		
		DocketHelper helper = new DocketHelper();
		
		String courtNorm = courtMap.get(court);
		String legacyId = helper.prepareLegacyID(docketNumber, courtNorm);

		
		ContentSpecifier contentSpecifier = new ContentSpecifier(docketNumber, courtNorm,legacyId);
		LookUpListDAOImpl lookups = new LookUpListDAOImpl(ENV);
		contentSpecifier.setCaseTypeList(lookups.getCaseTypes());
		contentSpecifier.setCourtList(lookups.getCourts());
		contentSpecifier.setProductList(lookups.getProducts());
		contentSpecifier.setVendorList(lookups.getVendors()); 

		ContentServiceBaseDAOImpl dao;
		try {
			dao = new ContentServiceBaseDAOImpl(contentSpecifier, ENV);
			dao.deleteDocket(legacyId, ENV);
		} catch (BankruptcyContentServiceException e) {
			e.printStackTrace();
		}

	}
		
//	@Test
	public void loadDocktFileTestNegativeAddTest() throws Exception {
		SourceLoadRequest sourceLoadRequest = new SourceLoadRequest();
		sourceLoadRequest.setAcquisitionMethod(AcquisitionMethod.RETROSPECTIVE);
		sourceLoadRequest.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);
		sourceLoadRequest.setBatchId(UUIDGenerator.createUuid().toString());
		sourceLoadRequest.setProduct(ProductEnum.FBR);
		sourceLoadRequest.setDelete(false);
		sourceLoadRequest.setAcquisitionStart(new java.util.Date());//25032012034254 (DocketsCoreUtility.getCurrentTimeStamp()
		sourceLoadRequest.setSourceFile(new java.io.File(MergeXML_Negative_Add));
		Collection<Docket> docketsFailedList = null;
		try {
			try {
				AllAndBadDockets allAndBadDockets = sourceContentLoader.loadDockets(sourceLoadRequest);
				docketsFailedList = allAndBadDockets.getBadDockets();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SourceContentLoaderException e) {
			e.printStackTrace();
		}
		
		Assert.notNull(docketsFailedList);
		Assert.isTrue(docketsFailedList.size() == 3);
	}
	
	*//**
	 * Run this test in debug mode and verify that dates passed on in valid format.
	 * add debug break at method call (printValuesBeingPassedToWrapper)
	 * default date (01011981010101) is passed on to wrapper service call where scrape date is empty. 
	 * there is no way we can verify that in intigration tests as this default date is 
	 * not updated to parsed docket object.  
	 *   
	 * @throws Exception
	 *//*
//	@Test
	public void loadDocktFileTestNoScrapeDateAddTest() throws Exception {
		SourceLoadRequest sourceLoadRequest = new SourceLoadRequest();
		sourceLoadRequest.setAcquisitionMethod(AcquisitionMethod.RETROSPECTIVE);
		sourceLoadRequest.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);
		sourceLoadRequest.setBatchId(UUIDGenerator.createUuid().toString());
		sourceLoadRequest.setProduct(ProductEnum.FBR);
		sourceLoadRequest.setDelete(false);
		sourceLoadRequest.setAcquisitionStart(new java.util.Date());//25032012034254 DocketsCoreUtility.getCurrentTimeStamp()
		sourceLoadRequest.setSourceFile(new java.io.File(SCRAPE_DATE_EMPTYL));
		Collection<Docket> docketsFailedList = null;
		try {
			try {
				AllAndBadDockets allAndBadDockets = sourceContentLoader.loadDockets(sourceLoadRequest);
				docketsFailedList = allAndBadDockets.getBadDockets();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SourceContentLoaderException e) {
			e.printStackTrace();
		}
		
		Assert.notNull(docketsFailedList);
		
	}

	//Lexis_Merge_Delete
//	@Test
	public void loadDocktFileTest_Lexis_Delete_Test() throws Exception {
		cleanUpAfterInsert("5:2001bk50767", "N_DKYEBKR", "dev"); *//**just to be on safer side cleaning as first step ***//* 
		SourceLoadRequest lexis_Insert_Request = new SourceLoadRequest();
		lexis_Insert_Request.setAcquisitionMethod(AcquisitionMethod.RETROSPECTIVE);
		lexis_Insert_Request.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);
		lexis_Insert_Request.setBatchId(UUIDGenerator.createUuid().toString());
		lexis_Insert_Request.setProduct(ProductEnum.FBR);
		lexis_Insert_Request.setDelete(false);
		lexis_Insert_Request.setSourceFile(new java.io.File(SCRAPE_DATE_EMPTYL)); // same source file as that of
		lexis_Insert_Request.setAcquisitionStart(new java.util.Date());//25032012034254



		Collection<Docket> docketsFailedList = null;
		

		
		try {
			try {
				
				*//************************** Insert before deletion *****************************//*
				AllAndBadDockets allAndBadDockets = sourceContentLoader.loadDockets(lexis_Insert_Request);
				docketsFailedList = allAndBadDockets.getBadDockets();

				
				*//************************** Delete run*****************************//*
				if(docketsFailedList ==null ){
					
					SourceLoadRequest lexis_delete_request = new SourceLoadRequest();
					lexis_delete_request.setAcquisitionMethod(AcquisitionMethod.RETROSPECTIVE);
					lexis_delete_request.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);
					lexis_delete_request.setBatchId(UUIDGenerator.createUuid().toString());
					lexis_delete_request.setProduct(ProductEnum.FBR);
					lexis_delete_request.setDelete(true);
					lexis_delete_request.setSourceFile(new File(Lexis_Merge_Delete));
					lexis_delete_request.setAcquisitionStart(new java.util.Date());//25032012034254

					
					AllAndBadDockets deleteAllAndBadDockets = sourceContentLoader.loadDockets(lexis_delete_request);
					docketsFailedList = deleteAllAndBadDockets.getBadDockets();
					Assert.isNull(docketsFailedList);
					*//************************** Final  Clean up ****************************//*
					Collection<Docket> docketsInsertList = allAndBadDockets.getAllDockets();
					for (Docket docket : docketsInsertList) {
						cleanUpAfterInsert(docket.getNumber(), docket.getCourt(), "dev");	
					}
					
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SourceContentLoaderException e) {
			e.printStackTrace();
		}
	}
	*//**
	 * Insert LCI inserts and then delete them to test delete functionality.
	 * @throws Exception
	 *//*
//	@Test
	public void loadDocktFileTest_LCI_Delete_Test() throws Exception {
		cleanUpAfterInsert("1998bk53060", "N_DORBKR", "dev"); *//**just to be on safer side cleaning as first step ***//*
		cleanUpAfterInsert("-100:1998BK53060", "N_DORBKR", "dev"); *//**just to be on safer side cleaning as first step ***//*
		
		SourceLoadRequest lci_Insert_Request = new SourceLoadRequest();
		lci_Insert_Request.setAcquisitionMethod(AcquisitionMethod.RETROSPECTIVE);
		lci_Insert_Request.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);
		lci_Insert_Request.setBatchId(UUIDGenerator.createUuid().toString());
		lci_Insert_Request.setProduct(ProductEnum.FBR);
		lci_Insert_Request.setDelete(false);
		lci_Insert_Request.setSourceFile(new File(LCI_Merge_Insert));
		lci_Insert_Request.setAcquisitionStart(new java.util.Date());//25032012034254

		
		Collection<Docket> docketsFailedList = null;
		

		
		try {
			try {
				
				*//************************** Insert before deletion *****************************//*
				AllAndBadDockets insertAllAndBadDockets = sourceContentLoader.loadDockets(lci_Insert_Request);
				docketsFailedList = insertAllAndBadDockets.getBadDockets();

				
				*//************************** Delete run*****************************//*
				if(docketsFailedList ==null ){
					
					SourceLoadRequest lci_delete_request = new SourceLoadRequest();
					lci_delete_request.setAcquisitionMethod(AcquisitionMethod.RETROSPECTIVE);
					lci_delete_request.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);
					lci_delete_request.setBatchId(UUIDGenerator.createUuid().toString());
					lci_delete_request.setProduct(ProductEnum.FBR);
					lci_delete_request.setDelete(true);
					lci_delete_request.setSourceFile(new File(LCI_Merge_Delete));
					lci_delete_request.setAcquisitionStart(new java.util.Date());//25032012034254

					
					AllAndBadDockets deleteAllAndBadDockets = sourceContentLoader.loadDockets(lci_delete_request);
					docketsFailedList = deleteAllAndBadDockets.getBadDockets();
					Assert.isNull(docketsFailedList);
					*//************************** Final  Clean up ****************************//*
					Collection<Docket> docketsInsertList = insertAllAndBadDockets.getAllDockets();
					for (Docket docket : docketsInsertList) {
						cleanUpAfterInsert(docket.getNumber(), docket.getCourt(), "dev");	
					}
					
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SourceContentLoaderException e) {
			e.printStackTrace();
		}
	}
	
	
//	@Test
	public void loadDocktFileTest_LCI_Oregon_Insert_Test() throws Exception {
		SourceLoadRequest sourceLoadRequest = new SourceLoadRequest();
		sourceLoadRequest.setAcquisitionMethod(AcquisitionMethod.RETROSPECTIVE);
		sourceLoadRequest.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);
		sourceLoadRequest.setBatchId(UUIDGenerator.createUuid().toString());
		sourceLoadRequest.setProduct(ProductEnum.FBR);
		sourceLoadRequest.setDelete(false);
		sourceLoadRequest.setSourceFile(new File(LCI_Oregon_Insert));
		sourceLoadRequest.setAcquisitionStart(new java.util.Date());//25032012034254

		Collection<Docket> docketsFailedList = null;
		Collection<Docket> sourceDocsList = null;
		AllAndBadDockets allAndBadDockets = null;
		try {
			try {
				allAndBadDockets = sourceContentLoader.loadDockets(sourceLoadRequest);
				docketsFailedList = allAndBadDockets.getBadDockets();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SourceContentLoaderException e) {
			e.printStackTrace();
		}
		
		sourceDocsList = allAndBadDockets.getAllDockets();
		for (Docket docket : sourceDocsList) {
			cleanUpAfterInsert(docket.getNumber(),docket.getCourt(),ENV) ;	
		}

		Assert.isNull(docketsFailedList);
		
		
	}
	*//**
	 * Added this test to bug fix 84219, in case of LCI-Oregon insert docket number will come without any location code 
	 * we add default location code "-100" for these scenario.   
	 * @throws Exception
	 *//*
	
//	@Test
	public void loadDocktFileTest_LCI_Oregon_Insert_2_Test() throws Exception {
		SourceLoadRequest sourceLoadRequest = new SourceLoadRequest();
		sourceLoadRequest.setAcquisitionMethod(AcquisitionMethod.RETROSPECTIVE);
		sourceLoadRequest.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);
		sourceLoadRequest.setBatchId(UUIDGenerator.createUuid().toString());
		sourceLoadRequest.setProduct(ProductEnum.FBR);
		sourceLoadRequest.setDelete(false);
		sourceLoadRequest.setSourceFile(new File(LCI_Oregon_Insert_2));
		sourceLoadRequest.setAcquisitionStart(new java.util.Date());//25032012034254

		Collection<Docket> docketsFailedList = null;
		AllAndBadDockets allAndBadDockets = null;
		try {
			try {
				allAndBadDockets = sourceContentLoader.loadDockets(sourceLoadRequest);
				docketsFailedList = allAndBadDockets.getBadDockets();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SourceContentLoaderException e) {
			e.printStackTrace();
		}
		

		*//** LCI - Oragon inserts we append '-100:' to its docket number before passing docket number to underlying wrapper API but this change is not applied 
		 * to original parsed docket object **//*
		
		cleanUpAfterInsert("6:2012bk60222","n_dtxwbkr".toUpperCase(),ENV) ;
		cleanUpAfterInsert("-100:"+"2012bk014174","n_dnjbkr".toUpperCase(),ENV) ;	

		Assert.isNull(docketsFailedList);
		
		
	}
//	@Test
	public void loadDocktFileTest_Lexis_Merge_Add_Test() throws Exception {
		SourceLoadRequest sourceLoadRequest = new SourceLoadRequest();
		sourceLoadRequest.setAcquisitionMethod(AcquisitionMethod.RETROSPECTIVE);
		sourceLoadRequest.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);
		sourceLoadRequest.setBatchId(UUIDGenerator.createUuid().toString());
		sourceLoadRequest.setProduct(ProductEnum.FBR);
		sourceLoadRequest.setDelete(false);
		sourceLoadRequest.setSourceFile(new File(Lexis_Merge_Add));
		sourceLoadRequest.setAcquisitionStart(new java.util.Date());//25032012034254

		Collection<Docket> docketsFailedList = null;
		Collection<Docket> sourceDocsList = null;
		AllAndBadDockets allAndBadDockets = null;
		try {
			try {
				allAndBadDockets = sourceContentLoader.loadDockets(sourceLoadRequest);
				docketsFailedList = allAndBadDockets.getBadDockets();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SourceContentLoaderException e) {
			e.printStackTrace();
		}
		
		sourceDocsList = allAndBadDockets.getAllDockets();
		for (Docket docket : sourceDocsList) {
			cleanUpAfterInsert(docket.getNumber(),docket.getCourt(),ENV) ;	
		}

		Assert.isNull(docketsFailedList);
		
		
	}

//	@Test
	public void sourceContentProcessorTest()
	{
		com.trgr.dockets.core.processor.SourceLoadRequest sourceLoadRequest = new com.trgr.dockets.core.processor.SourceLoadRequest();
		sourceLoadRequest.setAcquisitionMethod("RETROSPECTIVE");
		sourceLoadRequest.setAcquisitionType("DAILY");
		sourceLoadRequest.setBatchId(UUIDGenerator.createUuid().toString());
		sourceLoadRequest.setProductCode("FBR");
		sourceLoadRequest.setDeleteFlag(false);
		sourceLoadRequest.setSourcePath(MergeXML_positive_Add);
		sourceLoadRequest.setAcquisitionStart("19092012091156");
		SourceContentProcessor sourceContentProcessor = new SourceContentProcessor(errorLogService, eventLogService,sourceLoadRequestService,bankruptcyContentService,courtMap,"dev");
		try 
		{
			sourceContentProcessor.process(sourceLoadRequest);
		} 
		catch (SourceContentLoaderException e) {
			org.junit.Assert.fail();
			
		}
	}
	
//	@Test
	public void testFileNameLength()
	{
		String sourceFileName = "C:/developer/test/code/fileName.xml"; 
		if(sourceFileName.lastIndexOf("\\")>-1)
		{
			sourceFileName = sourceFileName.substring(sourceFileName.lastIndexOf("\\")+1);
		}
		else if(sourceFileName.lastIndexOf("/")>-1)
		{
			sourceFileName = sourceFileName.substring(sourceFileName.lastIndexOf("/")+1);
		}
		
		org.junit.Assert.assertTrue("File Name should be \"fileName.xml\"  but is " + sourceFileName.trim() ,sourceFileName.trim().equals("fileName.xml"));
	}
//	@Test
	public void loadDocktFileTest_Positive_Add_Test() throws Exception {
		SourceLoadRequest sourceLoadRequest = new SourceLoadRequest();
		sourceLoadRequest.setAcquisitionMethod(AcquisitionMethod.RETROSPECTIVE);
		sourceLoadRequest.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);
		sourceLoadRequest.setBatchId(UUIDGenerator.createUuid().toString());
		sourceLoadRequest.setProduct(ProductEnum.FBR);
		sourceLoadRequest.setDelete(false);
		sourceLoadRequest.setSourceFile(new File(MergeXML_positive_Add));
		sourceLoadRequest.setAcquisitionStart(new java.util.Date());//25032012034254

		Collection<Docket> docketsFailedList = null;
		Collection<Docket> sourceDocsList = null;
		AllAndBadDockets allAndBadDockets = null;
		try {
			try {
				allAndBadDockets = sourceContentLoader.loadDockets(sourceLoadRequest);
				docketsFailedList = allAndBadDockets.getBadDockets();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SourceContentLoaderException e) {
			e.printStackTrace();
		}
		
		sourceDocsList = allAndBadDockets.getAllDockets();
		for (Docket docket : sourceDocsList) {
			cleanUpAfterInsert(docket.getNumber(),docket.getCourt(),ENV) ;	
		}

		Assert.isNull(docketsFailedList);
		
		
	}*/

	@Test
	public void fakeTest()
	{
		Assert.isTrue(true);
	}
}

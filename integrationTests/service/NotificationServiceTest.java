package service;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.domain.NotificationInfo;
import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.NotificationGroup;
import com.trgr.dockets.core.entity.NotificationRequest;
import com.trgr.dockets.core.entity.NotificationType;
import com.trgr.dockets.core.entity.NotificationTypeEvent;
import com.trgr.dockets.core.entity.Product;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.service.NotificationService;
import com.trgr.dockets.core.service.NotificationServiceUtil;
import com.trgr.dockets.core.util.Environment;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "NotificationServiceIntegrationTest-context.xml" })
@Transactional
public class NotificationServiceTest {
	

	@Autowired
	private NotificationService notificationService;
	
	@Before
	public void setUp() {
		
	}
	
	@Test
	public void testSaveNotificationRequestTest() 
	{
		try
		{
			Date sampleDate = new Date();
			NotificationRequest notificationRequest = new NotificationRequest();
			notificationRequest.setNotificationGroup("TestGroup");
			notificationRequest.setSubject("Request Failed To Process.");
			notificationRequest.setBody("testBody");
			notificationRequest.setCreatedTime(sampleDate);
			notificationService.save(notificationRequest);
			assertNotNull(notificationRequest);

		}
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	/*
	 * get list of notification event for a given notificationType and Status
	 */
	@Test
	public void findNotifEventByTypeAndStatusTest() 
	{
		
		NotificationType notificationType = new NotificationType();
		notificationType.setNotificationTypeId(1000l);
		notificationType.setNotificationType("testType");
		notificationService.save(notificationType);
	
		NotificationTypeEvent notificationTypeEvent = new NotificationTypeEvent();
		notificationTypeEvent.setBody("test");
		notificationTypeEvent.setNotificationTypeTable(notificationType);
		notificationTypeEvent.setNotificationTypeEventId(9999);
		notificationTypeEvent.setStatus("1");
		notificationTypeEvent.setSubject("test");
		notificationService.save(notificationTypeEvent);
		
		NotificationTypeEvent findNotificationTypeEvent = notificationService.findNotifEventByTypeAndStatus("REQUEST", "5");
		assertNotNull(findNotificationTypeEvent);		
	}
	
	/*
	 * @Test to get notification group for a given notificationType and Status
	 */
	@Test
	public void findNotificationGroupByTypeAndStatusTest()
	{
		
		String notificationGroup = notificationService.findNotificationGroupByTypeAndStatus("REQUEST", "1"); 
		assertNotNull(notificationGroup);		
	}

	@Test
	public void createNotificationRequestEmailForRequestFailure() throws UUIDException
	{
		StatusEnum status = StatusEnum.COMPLETED_WITH_ERRORS;
		boolean ppOnly = true;
		
		PublishingRequest pubReq = new PublishingRequest("I90d22c30d73f11e1af2dc5d0ca0406ae");
		
		NotificationTypeEvent notificationTypeEvent = new NotificationTypeEvent();
		notificationTypeEvent.setSubject("%REQUEST_NAME% failed at %TIME%");
		notificationTypeEvent.setBody(
									"Request Name: %REQUEST_NAME% \n"
									+"Time stamp: %TIME% \n"
									+"Error Code: %ERROR_CODE% \n"
									+"Error Description: %ERROR_DESCRIPTION% \n"
									+"\n"
									+"\n"
									+"Environment: %ENV% \n"
									+"PBUI Link: http://uat.productbuilderui.dockets.int.westgroup.com:8180/ProductBuilderUI/login.htm \n");

		NotificationGroup notificationGroup = new NotificationGroup();
		notificationGroup.setEmailGroup("sagun.khanal@thomsonreuters.com");
		
//		NotificationRequest notificationRequest = NotificationServiceUtil.createNotificationForNotificationTypes(notificationTypeEvent, notificationGroup.getNotificationGroupName(), 
//				pubReq, "", errorCode, errorDescription, environment);
		notificationService.createNotificationForRequest(pubReq, status, ppOnly);
//		assertNotNull(notificationRequest.getSubject());
//		assertNotNull(notificationRequest.getBody());
	}
	
	@Test
	public void createNotificationRequestEmailForBatchFailure()
	{
		String errorCode = "TestError_101";
		String errorDescription = "Request Failed due to test reasons";
		String environment = "DEV";
		
		PublishingRequest pubReq = new PublishingRequest();
		pubReq.setRequestName("Notification_request_101");
		pubReq.makeIntoDataCapRequest();
		Product product = new Product();
		product.setId(3l);
		product.setCode("TEST");
		product.setDisplayName("TESTProduct");
		pubReq.setProduct(product);
		
		NotificationTypeEvent notificationTypeEvent = new NotificationTypeEvent();
		notificationTypeEvent.setSubject("%REQUEST_NAME% failed at %TIME%");
		notificationTypeEvent.setBody(
									"Request Name: %REQUEST_NAME% \n"
									+"Time stamp: %TIME% \n"
									+"Error Code: %ERROR_CODE% \n"
									+"Error Description: %ERROR_DESCRIPTION% \n"
									+"\n"
									+"\n"
									+"Environment: %ENV% \n"
									+"PBUI Link: http://uat.productbuilderui.dockets.int.westgroup.com:8180/ProductBuilderUI/login.htm \n");

		NotificationGroup notificationGroup = new NotificationGroup();
		notificationGroup.setEmailGroup("sagun.khanal@thomsonreuters.com");
		
		NotificationRequest notificationRequest = NotificationServiceUtil.createNotificationForNotificationTypes(notificationTypeEvent, notificationGroup.getEmailGroup(), 
				pubReq, "", errorCode, errorDescription, environment);
		assertNotNull(notificationRequest.getSubject());
		assertNotNull(notificationRequest.getBody());
	}
	
	@Test
	public void createNotificationRequestEmailForProcessFailure()
	{
		String errorCode = "TestError_102";
		String errorDescription = "Request Failed due to test reasons";
		String environment = "DEV";
		
		PublishingRequest pubReq = new PublishingRequest();
		pubReq.setRequestName("Notification_request_102");
		pubReq.makeIntoDataCapRequest();
		Product product = new Product();
		product.setId(3l);
		product.setCode("TEST");
		product.setDisplayName("TESTProduct");
		pubReq.setProduct(product);
		
		NotificationTypeEvent notificationTypeEvent = new NotificationTypeEvent();
		notificationTypeEvent.setSubject("%REQUEST_NAME% failed at %TIME%");
		notificationTypeEvent.setBody(
									"Request Name: %REQUEST_NAME% \n"
									+"Time stamp: %TIME% \n"
									+"Error Code: %ERROR_CODE% \n"
									+"Error Description: %ERROR_DESCRIPTION% \n"
									+"\n"
									+"\n"
									+"Environment: %ENV% \n"
									+"PBUI Link: http://uat.productbuilderui.dockets.int.westgroup.com:8180/ProductBuilderUI/login.htm \n");

		NotificationGroup notificationGroup = new NotificationGroup();
		notificationGroup.setEmailGroup("mahendra.survase@thomsonreuters.com");
		
		NotificationRequest notificationRequest = NotificationServiceUtil.createNotificationForNotificationTypes(notificationTypeEvent, notificationGroup.getEmailGroup(), 
				pubReq, "", errorCode, errorDescription, environment);
		assertNotNull(notificationRequest.getSubject());
		assertNotNull(notificationRequest.getBody());
	}
	
	@Test
	public void createNotificationRequestEmailForFTP()
	{
		
		NotificationInfo notificationInfo = new NotificationInfo();

		PublishingRequest pubReq = new PublishingRequest();
		pubReq.setRequestName("Notification_request_102");
		pubReq.makeIntoDataCapRequest();
		Product product = new Product();
		product.setId(3l);
		product.setCode("TEST");
		product.setDisplayName("TESTProduct");
		pubReq.setProduct(product);
		Court court = new Court();
		court.setCourtCluster("TestCluster");
		pubReq.setCourt(court);

//		notificationService.createNotificationForFTP(pubReq, StatusEnum.COMPLETED_SUCCESSFULLY, "TestFileName");

		NotificationTypeEvent notificationTypeEvent = new NotificationTypeEvent();
		notificationTypeEvent.setSubject("%ENV% Product: %PRODUCT%: FTP File Notification for %FTP_FILE% had Status %STATUS%");
		notificationTypeEvent.setBody(
									"Request Name:    %REQUEST_NAME%"+
		"Request Owner:       %REQUEST_OWNER%"+
		"TimeStamp:           %TIME%"+
		"Status:              %STATUS%"+
		"\n"+
		"Environment:  %ENV%"+
		"PBUI Link: http://%ENV%.productbuilderui.dockets.int.westgroup.com:8180/ProductBuilderUI/login.htm");

		//Get Environment name
		String env = Environment.getInstance().getEnv();				
		//Call Notification Util to get new Notification Request
		notificationInfo.setNotificationTypeEvent(notificationTypeEvent);
		notificationInfo.setNotificationGroupString("kirsten.gunn@thomsonreuters.com");
		notificationInfo.setPubReq(pubReq);
		notificationInfo.setFTPFile("TestFileName");
		notificationInfo.setEnv(env);
		notificationInfo.setStatusName(StatusEnum.COMPLETED_SUCCESSFULLY.name());

		
		NotificationRequest notificationRequest = NotificationServiceUtil.createNotificationForNotificationTypes(notificationInfo);
		
		System.out.println("Notification Subject: " + notificationRequest.getSubject());
		System.out.println("Notification Body: " + notificationRequest.getBody());
		assertNotNull(notificationRequest.getSubject());
		assertNotNull(notificationRequest.getBody());
	}

	@Test
	public void createNotificationRequestEmailMetadockErrors()
	{
		
		NotificationInfo notificationInfo = new NotificationInfo();

		PublishingRequest pubReq = new PublishingRequest();
		pubReq.setRequestName("Metadock_Notification_request_102");
		pubReq.makeIntoDataCapRequest();
		Product product = new Product();
		product.setId(3l);
		product.setCode("TEST");
		product.setDisplayName("TESTProduct");
		pubReq.setProduct(product);
		Court court = new Court();
		court.setCourtCluster("TestCluster");
		court.setPublicationId(101l);
		pubReq.setCourt(court);

		NotificationTypeEvent notificationTypeEvent = new NotificationTypeEvent();
		notificationTypeEvent.setSubject("%ENV% METADOC - Request FAILED Notification");
		notificationTypeEvent.setBody(
		"Request Name:    %REQUEST_NAME%"+
        "Request Owner:       %REQUEST_OWNER%"+
        "TimeStamp:           %TIME%"+
        "Publication_ID:	     %PUBLICATION_ID%"+
        "Collection Name:     %COLLECTION_NAME%"+
        "Metadoc InputFile:   %METADOC_INPUT_FILE%"+
        "Error Description:   %BATCH_ERRORS%"+
        "Environment:  %ENV%"+
        "PBUI Link: http://prod.productbuilderui.dockets.int.westgroup.com/ProductBuilderUI/monitor.htm'");

		//Get Environment name
		String env = Environment.getInstance().getEnv();				
		//Call Notification Util to get new Notification Request
		notificationInfo.setNotificationTypeEvent(notificationTypeEvent);
		notificationInfo.setNotificationGroupString("mahendra.survase@thomsonreuters.com");
		notificationInfo.setPubReq(pubReq);
		notificationInfo.setMetaDocInputFile("test/metadoc/inputFile.xml");
		notificationInfo.setEnv(env);
		notificationInfo.setStatusName(StatusEnum.COMPLETED_SUCCESSFULLY.name());

		
		NotificationRequest notificationRequest = NotificationServiceUtil.createNotificationForNotificationTypes(notificationInfo);
		
		System.out.println("Notification Subject: " + notificationRequest.getSubject());
		System.out.println("Notification Body: " + notificationRequest.getBody());
		assertNotNull(notificationRequest.getSubject());
		assertNotNull(notificationRequest.getBody());
	}
	
	@Test
	public void createNotificationRequestEmailAcquisitionRecordErrors()
	{
		
		NotificationInfo notificationInfo = new NotificationInfo();

		PublishingRequest pubReq = new PublishingRequest();
		pubReq.setRequestName("Acquisition_Notification_request_102");
		pubReq.makeIntoDataCapRequest();
		Product product = new Product();
		product.setId(3l);
		product.setCode("TEST");
		product.setDisplayName("TESTProduct");
		pubReq.setProduct(product);
		Court court = new Court();
		court.setCourtCluster("TestCluster");
		court.setPublicationId(101l);
		pubReq.setCourt(court);

		NotificationTypeEvent notificationTypeEvent = new NotificationTypeEvent();
		notificationTypeEvent.setSubject("%ENV% Court: %COURT%  ACQUISITION WEB ERROR Notification");
		notificationTypeEvent.setBody(
		"Request Name:              %REQUEST_NAME%"+
		"Request Owner:               %REQUEST_OWNER%"+
		"TimeStamp:                        %TIME%"+
		"Error Description:             %ERROR_DESCRIPTION%"+
		"Environment:  %ENV%");

		//Get Environment name
		String env = Environment.getInstance().getEnv();				
		//Call Notification Util to get new Notification Request
		notificationInfo.setNotificationTypeEvent(notificationTypeEvent);
		notificationInfo.setNotificationGroupString("mahendra.survase@thomsonreuters.com");
		notificationInfo.setPubReq(pubReq);
		notificationInfo.setEnv(env);
		notificationInfo.setStatusName(StatusEnum.FAILED.name());

		
		NotificationRequest notificationRequest = NotificationServiceUtil.createNotificationForNotificationTypes(notificationInfo);
		
		System.out.println("Notification Subject: " + notificationRequest.getSubject());
		System.out.println("Notification Body: " + notificationRequest.getBody());
		assertNotNull(notificationRequest.getSubject());
		assertNotNull(notificationRequest.getBody());
	}


	
}

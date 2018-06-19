/* Copyright 2015: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;
import org.xml.sax.InputSource;

import com.trgr.dockets.core.CoreConstants.AcquisitionMethod;
import com.trgr.dockets.core.domain.Docket;
import com.trgr.dockets.core.domain.DocketsList;
import com.trgr.dockets.core.domain.Page;
import com.trgr.dockets.core.domain.SourceLoadRequest;
import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.exception.SourceContentLoaderException;
import com.trgr.dockets.core.util.DocketMarshllerAndUnmarshller;

/**
 * @author U0105927 Mahendra Survase (Mahendra.Survase@thomsonreuters.com)
 *
 */
public class DocketMarshllerAndUnmarshllerTest {

	private DocketMarshllerAndUnmarshller docketMarshllerAndUnmarshller;

	File projectFolder = new File(".");
	String docketMergeXML = "integrationTests/util/WEST.BKR.preprocout.030212.002041.xml";
	String docketMergegZip = "integrationTests/util/WEST.BKR341.preprocout.CASES.030812.031730.xml.gz";
	String docketMergeXmlException = "integrationTests/util/Exception_WEST.BKR.preprocout.030212.002041.xml";
	String sourceContentRequestXml = "integrationTests/util/SourceContentRequest.xml";
	String sourceContentRequestExceptionXml = "integrationTests/util/ExceptionSourceContentRequest.xml";

	@Before
	public void setUp() {
		docketMarshllerAndUnmarshller = new DocketMarshllerAndUnmarshller();
	}

	@Test
	public void docketsUnmarshlerXMLTest() {
		InputStream in;
		DocketsList docketsList = null;
		try {
			in = new FileInputStream(new File(docketMergeXML));
			docketsList = docketMarshllerAndUnmarshller.docketsUnmarshller(in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SourceContentLoaderException e) {
			// TODO Auto-generated catch block
		}
		Assert.notNull(docketsList);
		Assert.notEmpty(docketsList.getDocketList());
	}

	@Test
	public void docketsUnmarshlerGZTest() {
		InputStream in;
		DocketsList docketsList = null;
		try {
			in = new FileInputStream(new File(docketMergeXML));
			docketsList = docketMarshllerAndUnmarshller.docketsUnmarshller(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SourceContentLoaderException e) {
			e.printStackTrace();
		}
		Assert.notNull(docketsList);
		Assert.notEmpty(docketsList.getDocketList());
	}

	/**
	 * Tests to verify if docketMarshller is throwing SourceContentLoaderExcption if inappropriate docket is passed to it.
	 * 
	 */
	//@Test
	// (expected = SourceContentLoaderException.class) /** this test is commented as of now because validator related code from marshller is commented as file access related issues needs to be resolved **/
	public void docketsUnmarshlerXmlExceptionTest() {
		InputStream in;
		boolean exceptionFlag = false;
		try {
			in = new FileInputStream(new File(docketMergeXmlException));
			docketMarshllerAndUnmarshller.docketsUnmarshller(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SourceContentLoaderException e) {
			Assert.isTrue(e.toString().contains("Failed to parse file "));
			exceptionFlag = true;
		}

		assertTrue(exceptionFlag);
	}

	@Test
	public void requestMessageUnmashallTest() throws Exception {
		InputStream in;
		SourceLoadRequest sourceLoadRequestActual = null;
		try {
			SourceLoadRequest sourceLoadRequestExpected = new SourceLoadRequest();
			sourceLoadRequestExpected.setBatchId("I63e4a5c001df11e28c1e842b2ba7eb1f");
			sourceLoadRequestExpected.setProduct(ProductEnum.FBR);
			sourceLoadRequestExpected.setDelete(false);
			sourceLoadRequestExpected.setSourceFile(new File("/dockets/BKR_FBR/LexMergePros_Test.xml"));
			sourceLoadRequestExpected.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);
			sourceLoadRequestExpected.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);

			String acquisitionStart = "19092012091156";
			SimpleDateFormat sdf = new SimpleDateFormat("ddmmyyyyhhMMss");
			Date acquisitionStartDate = sdf.parse(acquisitionStart);
			sourceLoadRequestExpected.setAcquisitionStart(acquisitionStartDate);
			in = new FileInputStream(new File(sourceContentRequestXml));
			InputSource inputSource = new InputSource(in);
			sourceLoadRequestActual = docketMarshllerAndUnmarshller.requestMessageUnmarshal(inputSource);

			Assert.notNull(sourceLoadRequestActual);

			assertEquals("Expected  " + sourceLoadRequestExpected.getAcquisitionMethod() + " but is " + sourceLoadRequestActual.getAcquisitionMethod(), sourceLoadRequestActual.getAcquisitionMethod(), sourceLoadRequestExpected.getAcquisitionMethod());
			assertEquals("Expected  " + sourceLoadRequestExpected.getProduct().getKey() + " but is " + sourceLoadRequestActual.getProduct().getKey(), sourceLoadRequestActual.getProduct().getKey(), sourceLoadRequestActual.getProduct().getKey());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SourceContentLoaderException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	@Test
	public void requestMessageUnmashllExceptionTest() {
		InputStream in;
		boolean exceptionFlag = false;
		try {
			in = new FileInputStream(new File(sourceContentRequestExceptionXml));
			InputSource inputSource = new InputSource(in);
			docketMarshllerAndUnmarshller.requestMessageUnmarshal(inputSource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SourceContentLoaderException e) {
			Assert.isTrue(e.toString().contains("Failed to parse file "));
			exceptionFlag = true;
		}
		assertTrue(exceptionFlag);
	}

	@Test
	public void requestMessageUnmashllValidationTest() {
		InputStream in;
		boolean exceptionFlag = false;
		try {
			in = new FileInputStream(new File(sourceContentRequestExceptionXml));
			InputSource inputSource = new InputSource(in);
			docketMarshllerAndUnmarshller.requestMessageUnmarshal(inputSource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SourceContentLoaderException e) {
			Assert.isTrue(e.toString().contains("Failed to parse file "));
			exceptionFlag = true;
		}
		assertTrue(exceptionFlag);
	}

	@Test
	public void docketDumpMarshllerTest() {
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
		page1.setDocketContent("Q0FTRCAgICAgICAgICAgICAxS1ktRUJLUiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg" + "ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg"
				+ "ICAgICAgICAgICAgICAgICAgTEVYSU5HVE9OICAgICAgICAgICAgICAgICAgICAgICAgICAgICBV" + "ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAwMTUwNzY3ICAgICAgICAgICAgICAg"
				+ "ICAgICAgICAgICAgSU5GT1JNQVRJT04gVU5BVkFJTEFCTEUgICAgICAgICAgICAgICAgIDA0LzE5" + "LzIwMDEgMTI6NDA6MDAgUE0xMDAgRSBWSU5FIDgwNyBMRVhJTkdUT04gS1kgICAgICAgICAgICAg"
				+ "ICAgICAgICAgICAgICAgICAgICBOICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDA0LzA1" + "LzIwMDUgMTI6MDA6MDAgQU0gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDAzLzA4LzIw"
				+ "MDEgICAgICAgICAgICANCkFUVCAgICAgICAgICAgICAxMDAwMDAwMDAwMDAwMDEgSU5GT1JNQVRJ" + "T04gVU5BVkFJTEFCTEUgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg"
				+ "ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg" + "ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg"
				+ "ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KREVGICAgICAgICAg" + "ICAgIDEwMDAwMDAwMDAwMDAwMTcgIDcgIDAwMDAwNzk0OTA2ODUxQkVBMDlEMTRDM0MzQkEyRDk0"
				+ "OTQyNkQwN0FGVCBJICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIENBTERX" + "RUxMICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBHQVJZICAgICAgICAgICAgICAgIFcg"
				+ "ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg" + "IDY3IERJWElFIFBMQVpBICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgUklD"
				+ "SE1PTkQgICAgICAgICAgICAgICAgS1k0MDQ3NSAgICAgICAgICAgICAgICAgIE9QRU4gICAgICAg" + "ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg" + "ICAN");
		pageList.add(page1);
		sourceDocket.setPageList(pageList);
		String docketBlock = null;
		try {
			docketBlock = docketMarshllerAndUnmarshller.docketDumpMarshaller(sourceDocket);
		} catch (SourceContentLoaderException e) {
			e.printStackTrace();
		}

		Assert.notNull(docketBlock);
	}
}

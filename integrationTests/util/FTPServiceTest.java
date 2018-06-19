/* Copyright 2015: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package util;

import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;

import com.trgr.dockets.core.util.DocketFTPClient;
import com.trgr.dockets.core.util.FTPFile;
import com.trgr.dockets.core.util.service.FTPServiceImpl;

/**
 * @author C047166
 *
 */
public class FTPServiceTest extends TestCase {
	FTPServiceImpl ftpService;
	String nasLandingPath;
	DocketFTPClient docketFTPClient;
	String srcFileNasFolderName;

	public void testLoadAndRetrieveFiles() throws Exception {
		InputStream inputStream = FTPServiceTest.class.getResourceAsStream("feddctdocket-product-sample.xml");
		FTPFile ftpFile = new FTPFile();
		ftpFile.setFileName("feddctdocket-product-sample.xml");
		ftpFile.setInputStream(inputStream);
		ftpService.ftpFileToLandingStrip(ftpFile, nasLandingPath, srcFileNasFolderName);
		ftpFile.setInputStream(null);
		ftpService.retrieveFileFromLandingStrip(ftpFile, nasLandingPath, srcFileNasFolderName);
		assertNotNull(ftpFile.getInputStream());
		try {
			String actualFileContentString = IOUtils.toString(ftpFile.getInputStream());
			String expectedFileContentString = IOUtils.toString(FTPServiceTest.class.getResourceAsStream("feddctdocket-product-sample.xml"));
			assertEquals(expectedFileContentString, actualFileContentString);
		} catch (Exception e) {
			System.out.println("FTP Test for " + nasLandingPath + "/" + srcFileNasFolderName + " error: " + e.getMessage());
			e.printStackTrace();
			assertEquals(1, 0); // purposely fail test.
		}
		ftpService.cleanupNasFolder(nasLandingPath, srcFileNasFolderName);
	}

	public void testNegativeLoadAndRetrieveFiles() throws Exception {
		docketFTPClient.setUserName("badDocketsUser");
		InputStream inputStream = FTPServiceTest.class.getResourceAsStream("feddctdocket-product-sample.xml");
		FTPFile ftpFile = new FTPFile();
		ftpFile.setFileName("feddctdocket-product-sample.xml");
		ftpFile.setInputStream(inputStream);

		try {
			ftpService.ftpFileToLandingStrip(ftpFile, nasLandingPath, srcFileNasFolderName);
			ftpFile.setInputStream(null);
			ftpService.retrieveFileFromLandingStrip(ftpFile, nasLandingPath, srcFileNasFolderName);
			assertNotNull(ftpFile.getInputStream());
			String actualFileContentString = IOUtils.toString(ftpFile.getInputStream());
			String expectedFileContentString = IOUtils.toString(FTPServiceTest.class.getResourceAsStream("feddctdocket-product-sample.xml"));
			assertEquals(expectedFileContentString, actualFileContentString);

			ftpService.cleanupNasFolder(nasLandingPath, srcFileNasFolderName);
		} catch (Exception e) {
			System.out.println("FTP Test for " + nasLandingPath + "/" + srcFileNasFolderName + " error: " + e.getMessage());
			e.printStackTrace();
			String expected = "Error ocurred while FTPing the file feddctdocket-product-sample.xml";
			assertEquals(expected, e.getMessage()); // purposely fail test.
		}
	}

	public void setUp() {
		ftpService = new FTPServiceImpl();
		nasLandingPath = "/nas/sdgshared/docketsprt";

		docketFTPClient = new DocketFTPClient();
		docketFTPClient.setServerName("uat.pubsvc.nasdisk.int.westgroup.com");
		docketFTPClient.setUserName("dockets");
		docketFTPClient.setPassword("h0L!day");
		docketFTPClient.setFtpClient(new FTPClient());
		ftpService.setDocketFtpClient(docketFTPClient);

		srcFileNasFolderName = "FTPServiceIntegrationTest";
	}
}

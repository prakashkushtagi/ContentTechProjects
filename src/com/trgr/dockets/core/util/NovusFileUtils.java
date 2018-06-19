package com.trgr.dockets.core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

public class NovusFileUtils {

	private static final Logger log = Logger.getLogger(NovusFileUtils.class);
	private static final String TEMPLMS_FILENAME = "Temp_File_For_LMS_Deletes.xml";
	public static final String DOCKET_NUMBER_XPATH = "n-document/n-metadata/metadata.block/md.identifiers/md.westlawids/md.docketnum/text()";
	public static final String LEGACY_ID_XPATH = "n-document/n-metadata/metadata.block/md.identifiers/md.legacy.id/text()";
	public static final String CASE_SUB_TYPE_XPATH = "n-document/n-metadata/metadata.block/md.sortkeys/md.sortvalues/md.case.subtype/text()";
	public static final String N_LOAD_START_ELEMENT = "<n-load>";
	public static final String N_LOAD_END_ELEMENT = "</n-load>";

	public static String getDocketNumber(String nDocumentXml) throws Exception {
		return getElementContent(nDocumentXml, DOCKET_NUMBER_XPATH);
	}

	public static String getLegacyId(String nDocumentXml) throws Exception {
		return getElementContent(nDocumentXml, LEGACY_ID_XPATH);
	}

	public static String getElementValue(String nDocumentXml, String xPath) throws Exception {
		return getElementContent(nDocumentXml, xPath);
	}

	private static String getElementContent(String nDocumentXml, String xpathPattern) throws Exception {
		String content = null;
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(nDocumentXml.getBytes()));
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		XPathExpression expr = xpath.compile(xpathPattern);
		content = (String) expr.evaluate(doc, XPathConstants.STRING);
		return content;
	}

	/**
	 * Splits a novus file in to two files. One with adds and one with deletes. If there is one control, only that file would be written.
	 * 
	 * @param sourceFile
	 * @param controlAddTargetFile
	 * @param controlDeleteTargetFile
	 * @throws Exception
	 */
	public static void splitFileBasedOnControl(File sourceFile, File controlAddTargetFile, File controlDeleteTargetFile) throws Exception {
		boolean isFirstAddDocket = true;
		boolean isFirstDeleteDocket = true;
		boolean addTargetFileHasContents = false;
		boolean deleteTargetFileHasContents = false;
		boolean isTempFileCreated = false;
		InputStream novusFileInputStream = null;
		
		//Bug 142653. Create temp file to persist Delete content for later use.
		String pathPlusFile = sourceFile.getAbsolutePath();
		String justPath = pathPlusFile.substring(0,pathPlusFile.lastIndexOf(File.separator));
		File tempFileForLMS = new File (justPath + "/" + TEMPLMS_FILENAME);
		if (tempFileForLMS.exists()) {
			tempFileForLMS.delete();
			isTempFileCreated = tempFileForLMS.createNewFile();
		}
		else {
			isTempFileCreated = tempFileForLMS.createNewFile();
		}
		//End
		try {
			novusFileInputStream = new FileInputStream(sourceFile);
			XMLInputFactory xif = XMLInputFactory.newInstance();
			XMLStreamReader xsr = xif.createXMLStreamReader(novusFileInputStream);

			xsr.nextTag(); // Advance to n-load element
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			while (xsr.nextTag() == XMLStreamConstants.START_ELEMENT) {
				StringWriter writer = new StringWriter();
				t.transform(new StAXSource(xsr), new StreamResult(writer));
				String nDocumentString = writer.toString();
				nDocumentString = nDocumentString.replaceAll("'", "&apos;"); // replace ' by &apos;
				if (nDocumentString.contains("control=\"ADD\"")) {
					if (isFirstAddDocket) {
						FileUtils.writeStringToFile(controlAddTargetFile, N_LOAD_START_ELEMENT);
						FileUtils.writeStringToFile(controlAddTargetFile, nDocumentString, true);
						isFirstAddDocket = false;
					} else {
						FileUtils.writeStringToFile(controlAddTargetFile, nDocumentString, true);
					}
					if (!addTargetFileHasContents) {
						addTargetFileHasContents = true;
					}
				} else {
					if (isFirstDeleteDocket) {
						FileUtils.writeStringToFile(controlDeleteTargetFile, N_LOAD_START_ELEMENT);
						FileUtils.writeStringToFile(controlDeleteTargetFile, "<" + StringUtils.substringBetween(nDocumentString, "<", ">") + "/>", true);
						isFirstDeleteDocket = false;
						if (isTempFileCreated) {
							FileUtils.writeStringToFile(tempFileForLMS, N_LOAD_START_ELEMENT);
							FileUtils.writeStringToFile(tempFileForLMS, nDocumentString, true);
						}
					} else {
						FileUtils.writeStringToFile(controlDeleteTargetFile, "<" + StringUtils.substringBetween(nDocumentString, "<", ">") + "/>", true);
						if (isTempFileCreated) {
							FileUtils.writeStringToFile(tempFileForLMS, nDocumentString, true);
						}
					}
					if (!deleteTargetFileHasContents) {
						deleteTargetFileHasContents = true;
					}
				}
			}
			if (addTargetFileHasContents) {
				FileUtils.writeStringToFile(controlAddTargetFile, N_LOAD_END_ELEMENT, true);
			}
			if (deleteTargetFileHasContents) {
				FileUtils.writeStringToFile(controlDeleteTargetFile, N_LOAD_END_ELEMENT, true);
				if (isTempFileCreated) {
					FileUtils.writeStringToFile(tempFileForLMS, N_LOAD_END_ELEMENT, true);
				}
			}
			else {
				tempFileForLMS.delete();
			}
		} catch (Exception e) {
			log.error("Error occurred while creating the split files for novus file " + sourceFile.getPath());
			throw e;
		} finally {
			IOUtils.closeQuietly(novusFileInputStream);
		}
	}

	/**
	 * Merges two novus input files in1 and in2 to one novus file outputFile. in1 and in2 are in <n-load><n-document>...</n-document>...</n-load> format The generated outputFile will also be in above format.
	 * 
	 * @param outputFile
	 * @param in1
	 * @param in2
	 * @throws IOException
	 */
	public static void mergeNovusFiles(File outputFile, File in1, File in2) throws IOException {
		//Bug.142653 Using the temp file content to send to LMS
		String pathPlusFil = outputFile.getAbsolutePath();
		String justPath = pathPlusFil.substring(0,pathPlusFil.lastIndexOf(File.separator));
		File tmpFileForLMS = new File (justPath + "/" + TEMPLMS_FILENAME);
		boolean isTempFileExist = tmpFileForLMS.exists();
		//End
		
		FileUtils.writeStringToFile(outputFile, N_LOAD_START_ELEMENT);
		// Take everything between <n-load> tag.
		Pattern p = Pattern.compile("<n-load.*?>(.*)</n-load>", Pattern.DOTALL);
		Matcher matcher = p.matcher(FileUtils.readFileToString(in1));
		if (matcher.matches()) {
			FileUtils.writeStringToFile(outputFile, matcher.group(1), true);
		}
//		Bug.142653. Ignore the file that is being passed and use the temp file.
//		matcher = p.matcher(FileUtils.readFileToString(in2));
//		if (matcher.matches()) {
//			FileUtils.writeStringToFile(outputFile, matcher.group(1), true);
//		}
		if (isTempFileExist) {
			matcher = p.matcher(FileUtils.readFileToString(tmpFileForLMS));
			if (matcher.matches()) {
				FileUtils.writeStringToFile(outputFile, matcher.group(1), true);
			}
		}
		//End
		FileUtils.writeStringToFile(outputFile, N_LOAD_END_ELEMENT, true);
		tmpFileForLMS.delete();
	}
}

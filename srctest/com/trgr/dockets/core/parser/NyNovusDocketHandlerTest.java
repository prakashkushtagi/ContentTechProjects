package com.trgr.dockets.core.parser;

import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.trgr.dockets.core.domain.NovusDocumentMetadata;

public class NyNovusDocketHandlerTest
{
	@Test
	public void testNyAndJpmlNovusDocketHandlerParse() throws IOException, XMLStreamException{
		String contentXml = "<n-load> " +
		"<n-document control=\"ADD\" guid=\"I6832b07f4f9511e2a531ef6793d44951\"><n-metadata><metadata.block><md.identifiers><md.westlawids><md.docketnum>39/1994/0133</md.docketnum></md.westlawids><md.uuid>I6832b07f4f9511e2a531ef6793d44951</md.uuid><md.legacy.id>19940013339</md.legacy.id></md.identifiers></metadata.block></n-metadata><n-docbody><r><scrape.date>20120815</scrape.date><publish.date>20120815010101</publish.date><convert.date>20120810</convert.date></r></n-docbody></n-document>" +
		"<n-document control=\"ADD\" guid=\"I6832b07f4f9511e2a531ef6793d44951\"><n-metadata><metadata.block><md.identifiers><md.westlawids><md.docketnum>39/1994/0134</md.docketnum></md.westlawids><md.uuid>I6832b07f4f9511e2a531ef6793d44951</md.uuid><md.legacy.id>19940013340</md.legacy.id></md.identifiers></metadata.block></n-metadata><n-docbody><r><scrape.date>20120816</scrape.date><publish.date>20120815010102</publish.date><convert.date>20120811</convert.date></r></n-docbody></n-document>" +
		"<n-document control=\"ADD\" guid=\"I6832b07f4f9511e2a531ef6793d44951\"><n-metadata><metadata.block><md.identifiers><md.westlawids><md.docketnum>39/1994/0135</md.docketnum></md.westlawids><md.uuid>I6832b07f4f9511e2a531ef6793d44951</md.uuid><md.legacy.id>19940013341</md.legacy.id></md.identifiers></metadata.block></n-metadata><n-docbody><r><scrape.date>20120817</scrape.date><publish.date>20120815010103</publish.date><convert.date>20120812</convert.date></r></n-docbody></n-document>" +
		"</n-load>";
		
		StateNovusDocketHandler handler =  new StateNovusDocketHandler("test", 9L);
		List <NovusDocumentMetadata> docketMetadataList = handler.parse(IOUtils.toInputStream(contentXml));
		Assert.assertEquals(3, docketMetadataList.size());
		Assert.assertEquals("19940013339", docketMetadataList.get(0).getLegacyId());
		Assert.assertEquals("39/1994/0133", docketMetadataList.get(0).getDocketNumber());
		Assert.assertEquals("ADD", docketMetadataList.get(0).getControl());
		Assert.assertEquals("20120815", docketMetadataList.get(0).getScrapeDate());
		Assert.assertEquals("20120810", docketMetadataList.get(0).getConvertDate());
		Assert.assertEquals("20120815010101", docketMetadataList.get(0).getPublishDate());
		
		Assert.assertEquals("19940013340", docketMetadataList.get(1).getLegacyId());
		Assert.assertEquals("39/1994/0134", docketMetadataList.get(1).getDocketNumber());
		Assert.assertEquals("ADD", docketMetadataList.get(1).getControl());
		Assert.assertEquals("20120816", docketMetadataList.get(1).getScrapeDate());
		Assert.assertEquals("20120811", docketMetadataList.get(1).getConvertDate());
		Assert.assertEquals("20120815010102", docketMetadataList.get(1).getPublishDate());
		
		Assert.assertEquals("19940013341", docketMetadataList.get(2).getLegacyId());
		Assert.assertEquals("39/1994/0135", docketMetadataList.get(2).getDocketNumber());
		Assert.assertEquals("ADD", docketMetadataList.get(2).getControl());
		Assert.assertEquals("20120817", docketMetadataList.get(2).getScrapeDate());
		Assert.assertEquals("20120812", docketMetadataList.get(2).getConvertDate());
		Assert.assertEquals("20120815010103", docketMetadataList.get(2).getPublishDate());
	}
}

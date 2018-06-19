package com.trgr.dockets.preprocessor.domain;

import java.io.InputStream;
import java.util.Date;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.trgr.dockets.core.entity.PBUIRequest;
import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum;
import com.trgr.dockets.core.util.JaxbMarshallingUtil;

public class PBUIRequestTest
{
	@Ignore
	public void testPBUIRequestAllElements() throws Exception{
		InputStream in = PBUIRequest.class.getResourceAsStream("sample.all.parameters.pbuirequest.xml");
		PBUIRequest payload = (PBUIRequest) JaxbMarshallingUtil.unmarshal(IOUtils.toString(in), PBUIRequest.class);
		Assert.assertEquals(11, payload.getParameters().getParameterList().size());
		Assert.assertEquals("Ie7dc9870713711e284ac842b2ba7fabb", payload.getRequestKey().toString());
		Assert.assertEquals(RequestTypeEnum.ALL, payload.getRequestType());
		Assert.assertEquals("STATE", payload.getProduct().getCode());
		Assert.assertEquals("CUSTOM", payload.getRequestInitiatorType().getCode());
		Assert.assertEquals("cbaTest.020713.090610", payload.getRequestName());
		Assert.assertEquals("u0007094", payload.getRequestOwner());
		Assert.assertEquals("FEDBKRDOCKET-BIGDOCKET-RPX-LINKED-PRT", payload.getWorkflowType().getCode());
		Date date = new Date(1360249570168l);
		Assert.assertEquals(date, payload.getStartTime());
		Assert.assertEquals("false", payload.isDeleteOverride().toString());
		Assert.assertEquals("false", payload.isPrismClipDateOverride().toString());
		Assert.assertEquals("C:\\temp\\bkr feature stories.xlsx", payload.getSourceFile().getAbsolutePath());
	}
	
	@Test
	public void testPBUIRequestFewElements() throws Exception{
		InputStream in = PBUIRequestTest.class.getResourceAsStream("sample.few.parameters.pbuirequest.xml");
		PBUIRequest payload = (PBUIRequest) JaxbMarshallingUtil.unmarshal(IOUtils.toString(in), PBUIRequest.class);
		Assert.assertEquals(4, payload.getParameters().getParameterList().size());
		Assert.assertEquals("Ie7dc9870713711e284ac842b2ba7fabb", payload.getRequestKey().toString());
		Assert.assertEquals(RequestTypeEnum.PP_ONLY, payload.getRequestType());
		Assert.assertEquals(ProductEnum.STATE, payload.getProduct());
		Assert.assertEquals(null, payload.getRequestInitiatorType());
		Assert.assertEquals("C:\\temp\\bkr feature stories.xlsx", payload.getSourceFile().getAbsolutePath());
	}
}

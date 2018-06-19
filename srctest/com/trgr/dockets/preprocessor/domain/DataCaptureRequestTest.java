package com.trgr.dockets.preprocessor.domain;

import junit.framework.Assert;

import org.junit.Test;

import com.trgr.dockets.core.entity.DataCaptureRequest;
import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.WorkflowTypeEnum;
import com.trgr.dockets.core.util.JaxbMarshallingUtil;

public class DataCaptureRequestTest {
	
	@Test
	public void testDCRequest() {
		DataCaptureRequest expected = new DataCaptureRequest();
		expected.setCourtName("theCourtName");
		expected.setProduct(ProductEnum.STATE);
		expected.setWorkflowType(WorkflowTypeEnum.STATEDOCKET_BIGDOCKET_UNLINKED);
		expected.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);
		expected.setPublishingPriority(11l);
		expected.setSharedServicePriority("HIGH");
		
		String xml = JaxbMarshallingUtil.marshal(expected);
		
		DataCaptureRequest actual = (DataCaptureRequest) JaxbMarshallingUtil.unmarshal(xml, DataCaptureRequest.class);
		
		Assert.assertEquals(expected, actual);
	}

}

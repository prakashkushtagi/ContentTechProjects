package com.trgr.dockets.core.domain.ltc;

import org.junit.Assert;
import org.junit.Test;

public class LtcCollaborationKeyTest {

	@Test
	public void testStateCollabKeySingleFile(){
		//below key is copied from prod
		LtcCollaborationKey ltcKey = new LtcCollaborationKey("STATE_I8ac10c23b33311e69822eed485bc7ca1_Ia1ae2f70b33311e69f97bae8e880509d_N_STDCKLNK09");
		String productCode = ltcKey.getProductCode();
		String requestId = ltcKey.getRequestId();
		String batchId = ltcKey.getBatchId();
		String collectionName = ltcKey.getCollectionName();
		Assert.assertEquals("STATE", productCode);
		Assert.assertEquals("I8ac10c23b33311e69822eed485bc7ca1", requestId);
		Assert.assertEquals("Ia1ae2f70b33311e69f97bae8e880509d", batchId);
		Assert.assertEquals("N_STDCKLNK09", collectionName);
	}
	
	
	@Test
	public void testStateCollabKeyMultipleFiles(){
		//below key is new functionality for large NL requests
		LtcCollaborationKey ltcKey = new LtcCollaborationKey("STATE_I8ac10c23b33311e69822eed485bc7ca1_Ia1ae2f70b33311e69f97bae8e880509d_N_STDCKLNK09_1");
		String productCode = ltcKey.getProductCode();
		String requestId = ltcKey.getRequestId();
		String batchId = ltcKey.getBatchId();
		String collectionName = ltcKey.getCollectionName();
		Assert.assertEquals("STATE", productCode);
		Assert.assertEquals("I8ac10c23b33311e69822eed485bc7ca1", requestId);
		Assert.assertEquals("Ia1ae2f70b33311e69f97bae8e880509d", batchId);
		Assert.assertEquals("N_STDCKLNK09", collectionName);
	}
	
	@Test
	public void testStateCollabKeyWoUnderscoreSingleFile(){
		//below key is made up as we have no examples of collection names without an underscore in them
		LtcCollaborationKey ltcKey = new LtcCollaborationKey("STATE_I8ac10c23b33311e69822eed485bc7ca1_Ia1ae2f70b33311e69f97bae8e880509d_NSTDCKLNK09");
		String productCode = ltcKey.getProductCode();
		String requestId = ltcKey.getRequestId();
		String batchId = ltcKey.getBatchId();
		String collectionName = ltcKey.getCollectionName();
		Assert.assertEquals("STATE", productCode);
		Assert.assertEquals("I8ac10c23b33311e69822eed485bc7ca1", requestId);
		Assert.assertEquals("Ia1ae2f70b33311e69f97bae8e880509d", batchId);
		Assert.assertEquals("NSTDCKLNK09", collectionName);
	}
	
	@Test
	public void testStateCollabKeyNoCollSingleFile(){
		//below key is made up as we don't have an example of this... but it was coded for in LTC.
		LtcCollaborationKey ltcKey = new LtcCollaborationKey("STATE_I8ac10c23b33311e69822eed485bc7ca1_Ia1ae2f70b33311e69f97bae8e880509d");
		String productCode = ltcKey.getProductCode();
		String requestId = ltcKey.getRequestId();
		String batchId = ltcKey.getBatchId();
		String collectionName = ltcKey.getCollectionName();
		Assert.assertEquals("STATE", productCode);
		Assert.assertEquals("I8ac10c23b33311e69822eed485bc7ca1", requestId);
		Assert.assertEquals("Ia1ae2f70b33311e69f97bae8e880509d", batchId);
		Assert.assertEquals(null, collectionName);
	}
	
	@Test
	public void testBKRCollabKeyNoCollSingleFile(){
		//below key is made up as we don't have an example of this...but it's how LTC Collab Key is currently coded
		LtcCollaborationKey ltcKey = new LtcCollaborationKey("FBR_I8ac10c23b33311e69822eed485bc7ca1-001_Ia1ae2f70b33311e69f97bae8e880509d");
		String productCode = ltcKey.getProductCode();
		String requestId = ltcKey.getRequestId();
		String batchId = ltcKey.getBatchId();
		String subBatchId = ltcKey.getSubBatchId();
		String collectionName = ltcKey.getCollectionName();
		Assert.assertEquals("FBR", productCode);
		Assert.assertEquals(null, requestId);
		Assert.assertEquals("I8ac10c23b33311e69822eed485bc7ca1-001", batchId);
		Assert.assertEquals("Ia1ae2f70b33311e69f97bae8e880509d", subBatchId);
		Assert.assertEquals(null, collectionName);
	}
	@Test
	public void testBKRCollabKeyCollectionSingleFile(){
		//below key is made up as we don't have an example of this...but it's how LTC Collab Key is currently coded
		LtcCollaborationKey ltcKey = new LtcCollaborationKey("FBR_I8ac10c23b33311e69822eed485bc7ca1-001_Ia1ae2f70b33311e69f97bae8e880509d_N_BKR1");
		String productCode = ltcKey.getProductCode();
		String requestId = ltcKey.getRequestId();
		String batchId = ltcKey.getBatchId();
		String subBatchId = ltcKey.getSubBatchId();
		String collectionName = ltcKey.getCollectionName();
		Assert.assertEquals("FBR", productCode);
		Assert.assertEquals(null, requestId);
		Assert.assertEquals("I8ac10c23b33311e69822eed485bc7ca1-001", batchId);
		Assert.assertEquals("Ia1ae2f70b33311e69f97bae8e880509d", subBatchId);
		Assert.assertEquals("N_BKR1", collectionName);
	}
	
	
	@Test
	public void testBKRCollabKeySingleFile(){
		//below key is copied from prod
		LtcCollaborationKey ltcKey = new LtcCollaborationKey("I5b67d9b0b33511e6a4249f84812afbc3-B0001_I5b67d9b0b33511e6a4249f84812afbc3-S0001");
		String productCode = ltcKey.getProductCode();
		String requestId = ltcKey.getRequestId();
		String subBatchId = ltcKey.getSubBatchId();
		String collectionName = ltcKey.getCollectionName();
		Assert.assertEquals("FBR", productCode);
		Assert.assertEquals("I5b67d9b0b33511e6a4249f84812afbc3", requestId);
		Assert.assertEquals("I5b67d9b0b33511e6a4249f84812afbc3-S0001", subBatchId);
		Assert.assertEquals(null, collectionName);
	}
}

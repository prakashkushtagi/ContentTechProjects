/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package service;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.Batch;
import com.trgr.dockets.core.entity.CodeTableValues.ProcessTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.entity.BatchMonitorKey;
import com.trgr.dockets.core.entity.CodeTableValues.WorkflowTypeEnum;
import com.trgr.dockets.core.entity.Process;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.service.DocketService;
import com.trgr.dockets.core.service.ProcessAdapter;
import com.trgr.dockets.core.service.ProcessService;
import com.westgroup.publishingservices.uuidgenerator.UUID;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("DocketServiceIntegrationTest-context.xml")
@Transactional
public class ProcessAdapterTest
{
	@Autowired
	private ProcessAdapter processAdapter;
	@Autowired
	private ProcessService processService;
	@Autowired
	private DocketService docketService;

	@Before
	public void setUp() 
	{

	}
	
	
	@Test
	public void testStartProcess()
	{
		Date startTime = new Date();
		String subBatchId = "I28E8F810D74411E1881A8802DADA7C88-S0004";
		ProcessTypeEnum processType = ProcessTypeEnum.IC_SUBBATCH;
		
		try 
		{
			PublishingRequest pubRequest = new PublishingRequest();
			UUID pubRequestUUID = new UUID("I28E8F810D74411E1881A8802DADA7C88");
			pubRequest.setPrimaryKey(pubRequestUUID);
			pubRequest.setRequestType(RequestTypeEnum.IC_PB);
			pubRequest.setRequestName("KGTEST");
			pubRequest.setRequestOwner("KGTEST");
			pubRequest.setPublishingStatus(StatusEnum.RUNNING);
			pubRequest.setWorkflowType(WorkflowTypeEnum.FEDBKRDOCKET_BIGDOCKET_RPX_LINKED_PRT);
			docketService.save(pubRequest);
			
			Batch batch = new Batch();
			batch.setPrimaryKey("I28E8F810D74411E1881A8802DADA7C88-B0004");
			batch.setPublishingRequest(pubRequest);
			docketService.save(batch);
			
			Batch subbatch = new Batch();
			subbatch.setPrimaryKey("I28E8F810D74411E1881A8802DADA7C88-S0004");
			subbatch.setParentBatch(batch);
			subbatch.setPublishingRequest(pubRequest);		
			docketService.save(subbatch);

			
			BatchMonitorKey batchMonitorKey = new BatchMonitorKey();
			batchMonitorKey.setPublishingRequestId("I28E8F810D74411E1881A8802DADA7C88");
			batchMonitorKey.setBatchId("I28E8F810D74411E1881A8802DADA7C88-B0004");

			this.processAdapter.startProcess(processType,  batchMonitorKey,  startTime,  subBatchId);
			Process expectedProcess = this.processService.findProcessByBatchSubBatchTypeId("I28E8F810D74411E1881A8802DADA7C88-B0004", "I28E8F810D74411E1881A8802DADA7C88-S0004",ProcessTypeEnum.IC_SUBBATCH.getKey());
			Assert.assertEquals(expectedProcess.getSubBatchId() , subBatchId);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
}

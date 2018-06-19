package com.trgr.dockets.core.entity;

import static com.trgr.dockets.core.entity.PublishingProcessControlKey.newPublishingProcessControlKeyPb;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.trgr.dockets.core.entity.CodeTableValues.CourtEnum;
import com.trgr.dockets.core.entity.CodeTableValues.ProcessTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum;

public class PublishingControlTest {

	private PublishingControl control;
	private PublishingProcessControl pcPP; 
	private PublishingProcessControl pcIC;
	private PublishingProcessControl pcPB;

	private static ProductEnum productEnum = ProductEnum.STATE;
	private static CourtEnum courtEnum = CourtEnum.N_DNYUPSTATE;
	private static Court court;
	
	@Before
	public void setUp() {
		
		this.control = new PublishingControl(100l);
		control.setCourt(new Court(courtEnum.getKey()));
		control.setProduct(new Product(productEnum));
		control.setMaintenanceStartTime(new Date(0));
		control.setMaintenanceEndTime(new Date(Long.MAX_VALUE));
		court = new Court();
		court.setPrimaryKey(courtEnum.N_DNYUPSTATE.getKey());
		

		List<PublishingProcessControl> processControls = new ArrayList<PublishingProcessControl>();
		this.pcPP = new PublishingProcessControl(new PublishingProcessControlKey(100l, ProcessTypeEnum.PP.getKey()), true);
		this.pcIC = new PublishingProcessControl(new PublishingProcessControlKey(200l, ProcessTypeEnum.IC.getKey()), true);
		this.pcPB = new PublishingProcessControl(newPublishingProcessControlKeyPb(300l), true);
		processControls.add(pcPP);
		processControls.add(pcIC);
		processControls.add(pcPB);
		control.setProcessControls(processControls);
	}
	
	@Test
	public void testAllActive() {
		Date time = new Date();
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_ONLY, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_TO_END, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PB_ONLY, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PB_TO_END, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_ONLY, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_IC, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_IC_PB, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.ALL, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_PB, time));
	}
	
	
	@Test
	public void testPPNotActive() {
		pcPP.setActive(false);
		Date time = new Date();
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_ONLY, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_TO_END, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PB_ONLY, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PB_TO_END, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_ONLY, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_IC, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_IC_PB, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.ALL, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_PB, time));
	}
	@Test
	public void testICNotActive() {
		pcIC.setActive(false);
		Date time = new Date();
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_ONLY, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_TO_END, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PB_ONLY, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PB_TO_END, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_ONLY, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_IC, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_IC_PB, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.ALL, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_PB, time));
	}
	@Test
	public void testPBNotActive() {
		Date time = new Date();
		pcPB.setActive(false);
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_ONLY, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_TO_END, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.PB_ONLY, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.PB_TO_END, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_ONLY, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_IC, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_IC_PB, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.ALL, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_PB, time));
	}
	
	@Test
	public void testOutsideTimeWindow() {
		this.control.setMaintenanceEndTime(new Date(System.currentTimeMillis()-1000));  // up to 1 sec ago
		Date time = new Date();
		pcPP.setActive(false);
		pcIC.setActive(false);
		pcPB.setActive(false);
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_ONLY, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_TO_END, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PB_ONLY, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PB_TO_END, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_ONLY, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_IC, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_IC_PB, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.ALL, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_PB, time));
	}
	
	@Test
	public void testDifferentProduct() {
		Date time = new Date();
		pcIC.setActive(false);
		assertTrue(control.isPublishingActive(ProductEnum.JPML, court, RequestTypeEnum.IC_ONLY, time));
		assertTrue(control.isPublishingActive(ProductEnum.JPML, court, RequestTypeEnum.IC_TO_END, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PB_ONLY, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PB_TO_END, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_ONLY, time));
		assertTrue(control.isPublishingActive(ProductEnum.JPML, court, RequestTypeEnum.PP_IC, time));
		assertTrue(control.isPublishingActive(ProductEnum.JPML, court, RequestTypeEnum.PP_IC_PB, time));
		assertTrue(control.isPublishingActive(ProductEnum.JPML, court, RequestTypeEnum.ALL, time));
		assertTrue(control.isPublishingActive(ProductEnum.JPML, court, RequestTypeEnum.IC_PB, time));
	}
	
	@Test
	public void testNullCourt() {
		control.setCourt(null);
		pcIC.setActive(false);
		Date time = new Date();
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_ONLY, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_TO_END, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PB_ONLY, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PB_TO_END, time));
		assertTrue(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_ONLY, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_IC, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.PP_IC_PB, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.ALL, time));
		assertFalse(control.isPublishingActive(productEnum, court, RequestTypeEnum.IC_PB, time));
	}
}

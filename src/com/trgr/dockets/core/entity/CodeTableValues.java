/**
 * Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and 
 * Confidential information of Thomson Reuters. Disclosure, Use or Reproduction 
 * without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.entity;

import static com.trgr.dockets.core.entity.CodeTableValues.RequestStepEnum.IC_RS;
import static com.trgr.dockets.core.entity.CodeTableValues.RequestStepEnum.IS_RS;
import static com.trgr.dockets.core.entity.CodeTableValues.RequestStepEnum.NL_RS;
import static com.trgr.dockets.core.entity.CodeTableValues.RequestStepEnum.PB_RS;
import static com.trgr.dockets.core.entity.CodeTableValues.RequestStepEnum.PP_RS;
import static com.trgr.dockets.core.entity.CodeTableValues.RequestStepEnum.WCW_RS;

import java.util.ArrayList;
import java.util.EnumSet;

public class CodeTableValues {	
	/**
	 * Values in the STATUS table.
	 */
	public enum StatusEnum {
		COMPLETED_SUCCESSFULLY(1l),
		RUNNING(2l),
		PENDING(3l),
		COMPLETED_WITH_ERRORS(4l),
		FAILED(5l),
		SUBMITTED(6l),
		PAUSED(7l),
		NO_UPDATES(8l),
		CANCELED(9l);
		
		
		private Long key;
		
		private StatusEnum(long key) {
			this.key = key;
		}
		public Long getKey() {
			return key;
		}
		public static StatusEnum findByKey(Long key) {
	          for (StatusEnum e : EnumSet.allOf(StatusEnum.class)) {
	        	  if (e.getKey().equals(key)) {
	        		  return e;
	        	  }
	          }
	          return null;
		}
		public static StatusEnum max(StatusEnum status1, StatusEnum status2) {
			if (status1 == StatusEnum.CANCELED || status2 == StatusEnum.CANCELED){
				return StatusEnum.CANCELED;
			}
			
			if (status1 == StatusEnum.FAILED || status2 == StatusEnum.FAILED) {
				return StatusEnum.FAILED;
			}
			if (status1 == StatusEnum.COMPLETED_WITH_ERRORS || status2 == StatusEnum.COMPLETED_WITH_ERRORS) {
				return StatusEnum.COMPLETED_WITH_ERRORS;
			}
			if (status1 == StatusEnum.PENDING || status2 == StatusEnum.PENDING) {
				return StatusEnum.PENDING;
			}
			if (status1 == StatusEnum.COMPLETED_SUCCESSFULLY || status2 == StatusEnum.COMPLETED_SUCCESSFULLY) {
				return StatusEnum.COMPLETED_SUCCESSFULLY;
			}
			return StatusEnum.RUNNING;
		}
		
		public static StatusEnum maxProcess(StatusEnum status1, StatusEnum status2) {
			if (status1 == StatusEnum.CANCELED || status2 == StatusEnum.CANCELED){
				return StatusEnum.CANCELED;
			}
			
			if (status1 == StatusEnum.PENDING || status2 == StatusEnum.PENDING) {
				return StatusEnum.PENDING;
			}
			if (status1 == StatusEnum.RUNNING || status2 == StatusEnum.RUNNING) {
				return StatusEnum.RUNNING;
			}
			if (status1 == StatusEnum.COMPLETED_SUCCESSFULLY && status2 == StatusEnum.COMPLETED_SUCCESSFULLY) {
				return StatusEnum.COMPLETED_SUCCESSFULLY;
			}
			else if (status1 == StatusEnum.FAILED && status2 == StatusEnum.FAILED) 
			{
				return StatusEnum.FAILED;
			}
			if (status1 == StatusEnum.COMPLETED_WITH_ERRORS || status2 == StatusEnum.COMPLETED_WITH_ERRORS) {
				return StatusEnum.COMPLETED_WITH_ERRORS;
			}
			if (status1 == StatusEnum.FAILED || status2 == StatusEnum.FAILED) 
			{
				return StatusEnum.COMPLETED_WITH_ERRORS;
			}
			return StatusEnum.RUNNING;
		}
		
		public static StatusEnum maxBatchStatus(StatusEnum status1, StatusEnum status2)
		{
			if (status1 == StatusEnum.CANCELED || status2 == StatusEnum.CANCELED){
				return StatusEnum.CANCELED;
			}
			
			if(status1 == StatusEnum.RUNNING || status2 == StatusEnum.RUNNING)
			{
				return StatusEnum.RUNNING;
			}
			if (status1 == StatusEnum.PENDING || status2 == StatusEnum.PENDING) {
				return StatusEnum.PENDING;
			}
			if (status1 == StatusEnum.FAILED || status2 == StatusEnum.FAILED) {
				return StatusEnum.FAILED;
			}
			if (status1 == StatusEnum.COMPLETED_WITH_ERRORS || status2 == StatusEnum.COMPLETED_WITH_ERRORS) {
				return StatusEnum.COMPLETED_WITH_ERRORS;
			}
			if (status1 == StatusEnum.COMPLETED_SUCCESSFULLY || status2 == StatusEnum.COMPLETED_SUCCESSFULLY) {
				return StatusEnum.COMPLETED_SUCCESSFULLY;
			}
			return StatusEnum.RUNNING;
		}
		
		public static StatusEnum maxUberBatchStatus(StatusEnum status1, StatusEnum status2)
		{
			if (status1 == StatusEnum.CANCELED || status2 == StatusEnum.CANCELED){
				return StatusEnum.CANCELED;
			}
			
			if(status1 == StatusEnum.RUNNING || status2 == StatusEnum.RUNNING)
			{
				return StatusEnum.RUNNING;
			}
			if (status1 == StatusEnum.PENDING || status2 == StatusEnum.PENDING) {
				return StatusEnum.PENDING;
			}
			if (status1 == StatusEnum.FAILED || status2 == StatusEnum.FAILED) {
				return StatusEnum.COMPLETED_WITH_ERRORS;
			}
			if (status1 == StatusEnum.COMPLETED_WITH_ERRORS || status2 == StatusEnum.COMPLETED_WITH_ERRORS) {
				return StatusEnum.COMPLETED_WITH_ERRORS;
			}
			if (status1 == StatusEnum.COMPLETED_SUCCESSFULLY && status2 == StatusEnum.COMPLETED_SUCCESSFULLY) {
				return StatusEnum.COMPLETED_SUCCESSFULLY;
			}
			return StatusEnum.RUNNING;
		}
		
		/**
		 * Gets the best case status of StatusEnum.COMPLETED_SUCCESSFULLY, StatusEnum.COMPLETED_WITH_ERRORS, StatusEnum.FAILED
		 * 
		 * @param status1
		 * @param status2
		 * @return
		 */
		public static StatusEnum bestCaseStatus(StatusEnum status1, StatusEnum status2) {
			StatusEnum statusEnum = null;
			
			if (status1 == StatusEnum.CANCELED || status2 == StatusEnum.CANCELED){
				return StatusEnum.CANCELED;
			}
			
			if(status1 == StatusEnum.PAUSED)
			{
				statusEnum = StatusEnum.PAUSED;
			}
				
			if (status1 == StatusEnum.COMPLETED_SUCCESSFULLY && status2 == StatusEnum.COMPLETED_SUCCESSFULLY) {
				statusEnum = StatusEnum.COMPLETED_SUCCESSFULLY;
			}else if(status1 == StatusEnum.COMPLETED_WITH_ERRORS && status2 == StatusEnum.COMPLETED_WITH_ERRORS){
				statusEnum = StatusEnum.COMPLETED_WITH_ERRORS;
			}else if((status1 == StatusEnum.COMPLETED_SUCCESSFULLY && status2 == StatusEnum.COMPLETED_WITH_ERRORS) || (status1 == StatusEnum.COMPLETED_WITH_ERRORS && status2 == StatusEnum.COMPLETED_SUCCESSFULLY)){
				statusEnum = StatusEnum.COMPLETED_WITH_ERRORS;
			}else if((status1 == StatusEnum.COMPLETED_SUCCESSFULLY && status2 == StatusEnum.FAILED) || (status1 == StatusEnum.FAILED && status2 == StatusEnum.COMPLETED_SUCCESSFULLY)){
				statusEnum = StatusEnum.COMPLETED_WITH_ERRORS;
			}else if((status1 == StatusEnum.COMPLETED_WITH_ERRORS && status2 == StatusEnum.FAILED) || (status1 == StatusEnum.FAILED && status2 == StatusEnum.COMPLETED_WITH_ERRORS)){
				statusEnum = StatusEnum.COMPLETED_WITH_ERRORS;
			}else if(status1 == StatusEnum.FAILED && status2 == StatusEnum.FAILED){
				statusEnum = StatusEnum.FAILED;
			}
			return statusEnum;
		}
		
		@Override
		public String toString() {
			return String.format("[name=%s,key=%d]", name(), getKey());
		}
	}
	
	/**
	 * Values in the REQUEST_INITIATOR_TYPE code table.
	 */
	public enum RequestInitiatorTypeEnum {
		UPDATE_LINK(1l, "UPDATE LINK"),
		DAILY(2l, "DAILY"),
		RESCRAPE(3l, "RESCRAPE"),
		CUSTOM(4l, "CUSTOM"),
		ARCHIVE(5l, "ARCHIVE"),
		LEGACY(6l, "LEGACY");
		
		private Long key;
		private String code;
		
		private RequestInitiatorTypeEnum(Long key, String code) {
			this.key = key;
			this.code = code;
		}
		public Long getKey() {
			return key;
		}
		public String getCode() {
			return code;
		}
		public static RequestInitiatorTypeEnum findByKey(Long key) {
	          for (RequestInitiatorTypeEnum e : EnumSet.allOf(RequestInitiatorTypeEnum.class)) {
	        	  if (e.getKey().equals(key)) {
	        		  return e;
	        	  }
	          }
	          return null;
		}
		public static RequestInitiatorTypeEnum findByCode(String code) {
	          for (RequestInitiatorTypeEnum e : EnumSet.allOf(RequestInitiatorTypeEnum.class)) {
	        	  if (e.getCode().equals(code)) {
	        		  return e;
	        	  }
	          }
	          return null;
		}
		@Override
		public String toString() {
			return String.format("[name=%s,key=%d,code=%s]", name(), getKey(), getCode());
		}
	}
	
	public enum RequestStepEnum {
		IC_RS("IC", "Intermediate Conversion"),
		PB_RS("PB", "Product Builder"),
		PP_RS("PP", "Preprocessor"),
		WCW_RS("WCW", "Pre Dockets WCW"),
		IS_RS("IS", "Import Source"),
		NL_RS("NL", "Novus Load");

		private String code;
		private String description;
		
		private RequestStepEnum(final String code, final String desc) {
			this.code = code;
			this.description = desc;
		}
	}
	
	private static class RequestStepEnums extends ArrayList<RequestStepEnum> {
		private static final long serialVersionUID = 1L;

		private RequestStepEnums(final RequestStepEnum... steps) {
			for (final RequestStepEnum step: steps) 
				add(step);
		}
	}

	/**
	 * Values in the REQUEST_TYPE code table.
	 */
	public enum RequestTypeEnum {
		IC_ONLY(1l, IC_RS),
		IC_TO_END(2l, IC_RS, PB_RS, NL_RS),
		PB_ONLY(3l, PB_RS),
		PB_TO_END(4l, PB_RS, NL_RS),
		PP_ONLY(5l, PP_RS),
		PP_IC(6l, PP_RS, IC_RS),
		PP_IC_PB(7l, PP_RS, IC_RS, PB_RS),
		ALL(8l, PP_RS, IC_RS, PB_RS, NL_RS),
		WCW(9l, WCW_RS),
		IC_PB(10l, IC_RS, PB_RS),
		IS_ONLY(11L, IS_RS),
		IS_IC(12l, IS_RS, IC_RS),
		IS_IC_PB(13l, IS_RS, IC_RS, PB_RS),
		IS_IC_PB_NL(14l, IS_RS, IC_RS, PB_RS, NL_RS);
		
		private Long key;
		private String code;
		private String description;
		private final RequestStepEnums steps; 
		
		@SuppressWarnings("synthetic-access")
		private RequestTypeEnum(final Long key, final RequestStepEnum... steps) {
			this.key = key;
			this.steps = new RequestStepEnums(steps);
			String codeSeparator = "";
			String descSeparator = "";
			final StringBuilder codeSB = new StringBuilder();
			final StringBuilder descSB = new StringBuilder();
			for (final RequestStepEnum step: steps) {
				codeSB.append(codeSeparator);
				descSB.append(descSeparator);
				codeSeparator = "-";
				descSeparator = " - ";
				codeSB.append(step.code);
				descSB.append(step.description);
			}
			this.code = codeSB.toString();
			this.description = descSB.toString();
		}
		public Long getKey() {
			return key;
		}
		public String getCode() {
			return code;
		}
		public String getDescription() {
			return description;
		}
		public boolean includesStep(final RequestStepEnum step) {
			return steps.contains(step);
		}
		public boolean includesImportSource() {
			return includesStep(IS_RS);
		}
		public boolean includesPreprocessor() {
			return includesStep(PP_RS);
		}
		public static RequestTypeEnum findByKey(Long key) {
	          for (RequestTypeEnum e : EnumSet.allOf(RequestTypeEnum.class)) {
	        	  if (e.getKey().equals(key)) {
	        		  return e;
	        	  }
	          }
	          return null;
		}
		public static RequestTypeEnum findByCode(String code) {
	          for (RequestTypeEnum e : EnumSet.allOf(RequestTypeEnum.class)) {
	        	  if (e.getCode().equals(code)) {
	        		  return e;
	        	  }
	          }
	          return null;
		}
		public static RequestTypeEnum findByDescription(String desc) {
	          for (RequestTypeEnum e : EnumSet.allOf(RequestTypeEnum.class)) {
	        	  if (e.getDescription().equals(desc)) {
	        		  return e;
	        	  }
	          }
	          return null;
		}
		@Override
		public String toString() {
			return String.format("[name=%s,key=%d,code=%s,description=%s]", name(), getKey(), getCode(), getDescription());
		}
	}

	/**
	 * Values in the PRODUCT code table.
	 */
	public enum ProductEnum {
		FBR(1l, "FBR"),
		JPML(2l, "JPML"),
		STATE(3l, "STATE"),
		FEDERAL(4L,"FEDERAL"),
		DCT(5L, "DCT"),
		CTA(6L, "CTA");
		
		private Long key;
		private String code;
		
		private ProductEnum(Long key, String code) {
			this.key = key;
			this.code = code;
		}
		public String getCode() {
			return code;
		}
		public Long getKey() {
			return key;
		}
		public static ProductEnum findByKey(Long key) {
	        for (ProductEnum e : EnumSet.allOf(ProductEnum.class)) {
	        	if (e.getKey().equals(key)) {
	        		return e;
	        	}
	        }
	        return null;
		}
		public static ProductEnum findByCode(String code) {
			for (ProductEnum e : EnumSet.allOf(ProductEnum.class)) {
				if (e.getCode().equals(code)) {
					return e;
				}
			}
			return null;
		}
		@Override
		public String toString() {
			return String.format("[name=%s,key=%d,code=%s]", name(), getKey(), getCode());
		}
	}

	/**
	 * Values in the PROCESS_TYPE code table.
	 */
	public enum ProcessTypeEnum {
		CREATE_BATCH(9l),
		SCL(10l),
		DCR(11l),
		IC(8l),
		IC_SUBBATCH(18l),
		JRECS(4l),
		PRT(1l),
		RPX(2l),
		DPB(17l),
		LTC(3l),
		NL(19l),
		METADOC(6l),
		DC(9l), //DATACAP
		PP(20l), //PreProcessor
		ACTIVITY_SET(21l), //Activity Set
		PNH(22l), //Publish Novus History
		DPBS(23l), 
		RPX_ADD(24l),
		RPX_DELETE(25l);
		private Long key;
		
		private ProcessTypeEnum(long key) {
			this.key = key;
		}
		public Long getKey() {
			return key;
		}
		public static ProcessTypeEnum findByKey(Long key) {
	          for (ProcessTypeEnum e : EnumSet.allOf(ProcessTypeEnum.class)) {
	        	  if (e.getKey().equals(key)) {
	        		  return e;
	        	  }
	          }
	          return null;
		}
		@Override
		public String toString() {
			return String.format("[name=%s,key=%s]", name(), getKey());
		}
		
		public static Long getDPBKey() {
			return DPB.getKey();
		}
	}

	/**
	 * Values in the DROPPED PROCESS TYPE ENUM code.
	 */
	public enum DroppedProcessTypeEnum {
		PP("PP"),
		IC("IC"),
		PB("PB"),
		NL("NL");
		
		private String code;
		
		private DroppedProcessTypeEnum(String code) {
			this.code = code;
		}
		public String getCode(){
			return code;
		}

		public static DroppedProcessTypeEnum findByCode(String code) {
			for (DroppedProcessTypeEnum e : EnumSet.allOf(DroppedProcessTypeEnum.class)) {
				if (e.getCode().equals(code)) {
					return e;
				}
			}
			return null;
		}
		@Override
		public String toString() {
			return String.format("[name=%s,key=%d,code=%s]", name(), getCode());
		}
	}

	public enum WorkflowTypeEnum {
		
		FEDBKRDOCKET_BIGDOCKET_RPX_LINKED_PRT(1l, "FEDBKRDOCKET-BIGDOCKET-RPX-LINKED-PRT"),
		FEDBKRDOCKET_BIGDOCKET_RPX_LINKED_PRTHIGH(4l, "FEDBKRDOCKET-BIGDOCKET-RPX-LINKED-PRTHIGH"),
		FEDBKRDOCKET_BIGDOCKET_UNLINKED(3l, "FEDBKRDOCKET-BIGDOCKET-UNLINKED"),
		FEDBKRDOCKET_BIGDOCKET_UNLINKED_PRT(2l, "FEDBKRDOCKET-BIGDOCKET-UNLINKED-PRT"),
		FEDBKRDOCKET_BIGDOCKET_UNLINKED_PRTHIGH(5l, "FEDBKRDOCKET-BIGDOCKET-UNLINKED-PRTHIGH"),
		FEDBKRDOCKET_BIGDOCKET_RPX_LINKED(19l, "FEDBKRDOCKET-BIGDOCKET-RPX-LINKED"),

		FEDJPMLDOCKET_RPX_LINKED_PRT(6l, "FEDJPMLDOCKET-RPX-LINKED-PRT"),
		FEDJPMLDOCKET_UNLINKED(8l, "FEDJPMLDOCKET-UNLINKED"),
		FEDJPMLDOCKET_UNLINKED_PRT(7l, "FEDJPMLDOCKET-UNLINKED-PRT"),
		
		FEDDOCKET_RPX_LINKED(18l, "FEDDOCKET-RPX-LINKED"),
		FEDDOCKET_RPX_LINKED_PRT(15l, "FEDDOCKET-RPX-LINKED-PRT"),
		FEDDOCKET_UNLINKED(17l, "FEDDOCKET-UNLINKED"),
		FEDDOCKET_UNLINKED_PRT(16l, "FEDDOCKET-UNLINKED-PRT"),

		STATEDOCKET_BIGDOCKET_RPX_LINKED(12l, "STATEDOCKET-BIGDOCKET-RPX-LINKED"),
		STATEDOCKET_BIGDOCKET_RPX_LINKED_PRT(9l, "STATEDOCKET-BIGDOCKET-RPX-LINKED-PRT"),
		STATEDOCKET_BIGDOCKET_UNLINKED(11l, "STATEDOCKET-BIGDOCKET-UNLINKED"),
		STATEDOCKET_BIGDOCKET_UNLINKED_PRT(10l, "STATEDOCKET-BIGDOCKET-UNLINKED-PRT"),
		STATEDOCKET_PREDOCKET(13l, "STATEDOCKET-PREDOCKET"),
		
		DCT_RPX_LINKED_PRT(20l,"DCT-RPX-LINKED-PRT"),
		DCT_UNLINKED_PRT(21l,"DCT-UNLINKED-PRT"),
		DCT_UNLINKED(22l,"DCT-UNLINKED"),
		DCT_RPX_LINKED(23l,"DCT-RPX-LINKED"),
		DCT_PREDOCKET(24l, "DCT-PREDOCKET")
		;
		
		private Long key;
		private String code;
		
		private WorkflowTypeEnum(long key, String code) {
			this.key = key;
			this.code = code;
		}
		public String getCode() {
			return code;
		}
		public Long getKey() {
			return key;
		}
		public static WorkflowTypeEnum findByKey(Long key) {
	          for (WorkflowTypeEnum e : EnumSet.allOf(WorkflowTypeEnum.class)) {
	        	  if (e.getKey().equals(key)) {
	        		  return e;
	        	  }
	          }
	          return null;
		}
		public static WorkflowTypeEnum findByCode(String code) {
	          for (WorkflowTypeEnum e : EnumSet.allOf(WorkflowTypeEnum.class)) {
	        	  if (code.equals(e.getCode())) {
	        		  return e;
	        	  }
	          }
	          return null;
		}
		
		@Override
		public String toString() {
			return String.format("[name=%s,code=%s]", name(), getCode());
		}
	}


	/**
	 * Values in the DCT TYPE code table.
	 */
	public enum DCTTypeEnum {
		action_types(11, "action_types"),
		appearance_actions(22, "appearance_actions"),
		court_parts(31, "court_parts"),
		jitype(41, "jitype"),
		jury(51, "jury"),
		motion_decisions(61, "motion_decisions"),
		motion_reliefs(71, "motion_reliefs"),
		motion_by(81, "motionby"),
		ny_downstate(91, "ny_downstate");
		
		private int key;
		private String code;
		
		private DCTTypeEnum(int key, String code) {
			this.key = key;
			this.code = code;
		}
		public String getCode() {
			return code;
		}
		public int getKey() {
			return key;
		}
		public static DCTTypeEnum findByKey(int key) {
	        for (DCTTypeEnum e : EnumSet.allOf(DCTTypeEnum.class)) {
	        	if (e.getKey() == key) {
	        		return e;
	        	}
	        }
	        return null;
		}
		public static DCTTypeEnum findByCode(String code) {
			for (DCTTypeEnum e : EnumSet.allOf(DCTTypeEnum.class)) {
				if (e.getCode().equals(code)) {
					return e;
				}
			}
			return null;
		}
		@Override
		public String toString() {
			return String.format("[name=%s,key=%d,code=%s]", name(), getKey(), getCode());
		}
	}


	/**
	 * Values in the Advocate TYPE code table.
	 */
	public enum AdvocateTypeEnum {
		Attorney(1, "Attorney"),
		Firm(2, "Firm"),
		Justice(3, "Justice");
		
		private int key;
		private String code;
		
		private AdvocateTypeEnum(int key, String code) {
			this.key = key;
			this.code = code;
		}
		public String getCode() {
			return code;
		}
		public int getKey() {
			return key;
		}
		public static AdvocateTypeEnum findByKey(int key) {
	        for (AdvocateTypeEnum e : EnumSet.allOf(AdvocateTypeEnum.class)) {
	        	if (e.getKey() == key) {
	        		return e;
	        	}
	        }
	        return null;
		}
		public static AdvocateTypeEnum findByCode(String code) {
			for (AdvocateTypeEnum e : EnumSet.allOf(AdvocateTypeEnum.class)) {
				if (e.getCode().equals(code)) {
					return e;
				}
			}
			return null;
		}
		@Override
		public String toString() {
			return String.format("[name=%s,key=%d,code=%s]", name(), getKey(), getCode());
		}
	}


	/**
	 * Values in the Court code table.<br/>
	 * <br/>
	 * DO NOT USE!
	 * 
	 * @deprecated Note: This was originally used to differentiate publishing requests in the PP stage. Since that information is available on both the publishing
	 * request and in the DB, this enum is deprecated (lest we run into annoyance of having to update both the DB and enum for every single new court we get)
	 */
	@Deprecated
	public enum CourtEnum {
		N_DFEDJPML(94L, "N_DFEDJPML"),
		N_DNYDOWNSTATE(95L, "N_DNYDOWNSTATE"),
		N_DNYUPSTATE(96L, "N_DNYUPSTATE"),
		N_DNYCNTYCLERK(97L, "N_DNYCNTYCLERK"),
		//Forgive me, for I have sinned
		N_DCASTANIS(98L, "N_DCASTANIS"),
		//TODO: Remove these values. They shouldn't have been added in the first place.
		N_DNDSTCTS(99L, "N_DNDSTCTS");

		
		
		
		private Long key;
		private String code;
		
		private CourtEnum(Long key, String code) {
			this.key = key;
			this.code = code;
		}
		public String getCode() {
			return code;
		}
		public Long getKey() {
			return key;
		}
		public static CourtEnum findByKey(int key) {
	        for (CourtEnum e : EnumSet.allOf(CourtEnum.class)) {
	        	if (e.getKey() == key) {
	        		return e;
	        	}
	        }
	        return null;
		}
		public static CourtEnum findByCode(String code) {
			for (CourtEnum e : EnumSet.allOf(CourtEnum.class)) {
				if (e.getCode().equals(code)) {
					return e;
				}
			}
			return null;
		}
		@Override
		public String toString() {
			return String.format("[name=%s,key=%d,code=%s]", name(), getKey(), getCode());
		}
	}
		
	/**
	 * Values in the CASE TYPE code table.
	 */
	public enum CaseTypeEnum {
		UNKNOWN(0l, "UNKNOWN"),
		BANKRUPTCY(1l, "BANKRUPTCY"),
		ACQUISITION_PROCEEDINGS(2l, "ACQUISITION-PROCEEDINGS"),
		MDL(3l, "MDL"),
		ADVERSARY_PROCEEDING(4l, "ADVERSARY-PROCEEDING");
				
		private Long key;
		private String code;
		
		private CaseTypeEnum(Long key, String code) {
			this.key = key;
			this.code = code;
		}
		public String getCode() {
			return code;
		}
		public Long getKey() {
			return key;
		}
		public static CaseTypeEnum findByKey(Long key) {
	        for (CaseTypeEnum e : EnumSet.allOf(CaseTypeEnum.class)) {
	        	if (e.getKey().equals(key)) {
	        		return e;
	        	}
	        }
	        return null;
		}
		public static CaseTypeEnum findByCode(String code) {
			for (CaseTypeEnum e : EnumSet.allOf(CaseTypeEnum.class)) {
				if (e.getCode().equals(code)) {
					return e;
				}
			}
			return null;
		}
		@Override
		public String toString() {
			return String.format("[name=%s,key=%d,code=%s]", name(), getKey(), getCode());
		}
	}
}

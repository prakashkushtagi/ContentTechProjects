package com.trgr.dockets.core.entity;

import java.io.Serializable;
/**
 * @author u0160964 Sagun Khanal (Sagun.Khanal@thomsonreuters.com)
 * 
 */


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="EVENT_REGISTRY",schema="DOCKETS_PUB")
public class EventRegistry implements Serializable{


		private static final long serialVersionUID = 1L;
		private String eventName;
		private String eventTypeId;
		private String processTypeId;
		private String auditId;
		private String isTerminated;
		
		
		public EventRegistry(String eventName, String eventTypeId, String processTypeId, String auditId, String isTerminated) {
			super();
			this.eventName = eventName;
			this.eventTypeId = eventTypeId;
			this.processTypeId = processTypeId;
			this.auditId = auditId;
			this.isTerminated = isTerminated;
		}
		
		@Column(name= "EVENT_NAME",nullable= false)
		public String getEventName() {
			return eventName;
		}
		
		public void setEventName(String eventName) {
			this.eventName = eventName;
		}
		
		@Column(name = "EVENT_TYPE_ID", nullable = false)
		public String getEventTypeId() {
			return eventTypeId;
		}
		public void setEventTypeId(String eventTypeId) {
			this.eventTypeId = eventTypeId;
		}
		
		@Column(name ="PROCESS_TYPE_ID",nullable = false)
		public String getProcessTYpeId() {
			return processTypeId;
		}
		public void setProcessTypeId(String processTypeId) {
			this.processTypeId = processTypeId;
		}
		
		@Column(name="AUDIT_ID", nullable = true)
		public String getAuditId(){
			return auditId;
		}
		
		public void setAuditId(String auditId){
			this.auditId = auditId;
		}
		
		@Column(name = "IS_TERMINATED")
		public String getIsTerminated(){
			return isTerminated;
		}
		
		public void setIsTerminated(String isTerminated){
			this.isTerminated = isTerminated;
		}

	}




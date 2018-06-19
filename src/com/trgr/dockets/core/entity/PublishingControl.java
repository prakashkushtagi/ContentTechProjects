/** Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.trgr.dockets.core.entity.CodeTableValues.ProcessTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum;

@Entity
@Table(name="PUBLISHING_CONTROL", schema="DOCKETS_PUB")
public class PublishingControl implements Serializable {

	private static final long serialVersionUID = -4663222604569940423L;
	private long primaryKey;
	private Product product;
	private Court court;
	private Long vendorId;
	private Date maintenanceStartTime;
	private Date maintenanceEndTime;
	private String pauseUser;
	private List<PublishingProcessControl> processControls = new ArrayList<PublishingProcessControl>();
	private String notificationSent;
	private String display;
	
	public PublishingControl() {
		super();
	}
	public PublishingControl(Long pk) {
		setPrimaryKey(pk);
	}
	@Id
	@Column(name="PUBLISHING_CONTROL_ID")
	public long getPrimaryKey() {
		return primaryKey;
	}
	@OneToOne
	@JoinColumn(name="PRODUCT_ID")
	public Product getProduct() {
		return product;
	}
	@OneToOne
	@JoinColumn(name="COURT_ID")
	public Court getCourt() {
		return court;
	}

	@Column(name = "VENDOR_ID", nullable = true)
	public Long getVendorId() {
		return vendorId;
	}
	
	@Column(name="MAINT_WINDOW_START")
	public Date getMaintenanceStartTime() {
		return maintenanceStartTime;
	}
	@Column(name="MAINT_WINDOW_END")
	public Date getMaintenanceEndTime() {
		return maintenanceEndTime;
	}
	@Column(name="PAUSE_USER")
	public String getPauseUser() {
		return pauseUser;
	}
	@Column(name="NOTIFICATION_SENT")
	public String getNotificationSent() {
		return notificationSent;
	}
	@Column(name="DISPLAY")
	public String getDisplay() {
		return display;
	}
	@OneToMany
	@JoinColumn(name="PUBLISHING_CONTROL_ID")
	public List<PublishingProcessControl> getProcessControls() {
		return processControls;
	}
	
	public boolean isPublishingActive(ProductEnum productEnum, Court courtParam, RequestTypeEnum requestTypeEnum, Date time) {
		if (productCourtAndTimeMatch(productEnum,courtParam,time)) {  // TIME
			switch (requestTypeEnum) {
				case IC_ONLY:
					return isProcessTypeActive(ProcessTypeEnum.IC);
				case IC_PB:
				case IC_TO_END:
					return isProcessTypeActive(ProcessTypeEnum.IC) &&
						   isProcessTypeActive(ProcessTypeEnum.DPB);
				case PB_ONLY:
				case PB_TO_END:
					return isProcessTypeActive(ProcessTypeEnum.DPB);
				case PP_ONLY:
					return isProcessTypeActive(ProcessTypeEnum.PP);
				case PP_IC:
					return isProcessTypeActive(ProcessTypeEnum.PP) &&
						   isProcessTypeActive(ProcessTypeEnum.IC);
				case PP_IC_PB:
				case ALL:
					return isProcessTypeActive(ProcessTypeEnum.PP) &&
						   isProcessTypeActive(ProcessTypeEnum.IC) &&
						   isProcessTypeActive(ProcessTypeEnum.DPB);
				case WCW:
					return true;
				default:
					throw new IllegalArgumentException("Programminig error - Unhandled requestType: " + requestTypeEnum);
			}
		}
		return true;
	}
	
	public boolean isPublishingActiveByProcessType(ProductEnum productEnum, Court courtParam, ProcessTypeEnum processTypeEnum, Date time,Long requestVendorId) {
		if(doVendorsMatch(vendorId, requestVendorId)){
			if (product.hasPrimaryKey(productEnum) && courtParam == null && court == null && inMaintenanceTime(time) ){ 
				return isProcessTypeActive(processTypeEnum);
			}		
			if (productCourtAndTimeMatch(productEnum,courtParam,time)) { 
				return isProcessTypeActive(processTypeEnum);
			}
		}
		return true;
	}
	
	private boolean doVendorsMatch(Long dbVendorId, Long requestVendorId) {
		boolean vendorsMatch = true;
		if(requestVendorId != null && dbVendorId != null) {
			if(requestVendorId.equals(dbVendorId)) {
				vendorsMatch = true;
			}else{
				vendorsMatch = false;	
			}
		}
		return vendorsMatch;
	}
	
	private boolean productCourtAndTimeMatch(final ProductEnum productEnum, final Court courtParam, final Date time) {
		return product.hasPrimaryKey(productEnum) &&	// PRODUCT
				((court == null) || ((courtParam != null) && court.matchesByPrimaryKey(courtParam))) &&  // COURT
				inMaintenanceTime(time);  // TIME
	}
	
	public final boolean inMaintenanceTime(final Date time) {
		return (time != null)
				&& (maintenanceStartTime != null)
				&& (maintenanceEndTime != null)
				&& time.after(maintenanceStartTime) && time.before(maintenanceEndTime);
	}
	
	private boolean isProcessTypeActive(ProcessTypeEnum type) {
		for (PublishingProcessControl control : processControls) {
			if (control.getPrimaryKey().getProcessTypeId() == type.getKey()) {
				return control.isActive();
			}
		}
		// if no record that specifically disables it, means that it is active/enabled
		return true;
	}

	protected void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public void setCourt(Court court) {
		this.court = court;
	}
	public void setMaintenanceStartTime(Date startTime) {
		this.maintenanceStartTime = startTime;
	}
	public void setMaintenanceEndTime(Date endTime) {
		this.maintenanceEndTime = endTime;
	}
	public void setPauseUser(String pauseUsr) {
		this.pauseUser = pauseUsr;
	}
	public void setProcessControls(List<PublishingProcessControl> processControls) {
		this.processControls = processControls;
	}
	public void setNotificationSent(String notificationSent) {
		this.notificationSent = notificationSent;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}


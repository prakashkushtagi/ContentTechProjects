/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.trgr.dockets.core.entity.CodeTableValues.CourtEnum;

@SuppressWarnings("deprecation")
@Entity
@Table(name="COURT", schema="DOCKETS_PUB")
public class Court implements Serializable {

	private static final long serialVersionUID = 1910467830443475838L;
	private Long primaryKey;
	private CollectionEntity collection;
	private String courtCluster;
	private String courtNorm;
	private Product product;
    private Long publicationId;
    private State state;
    private Long scrapeVendorId;
    
	private List<CourtCaseTypeConfig> courtCaseTypeConfigs = new ArrayList<CourtCaseTypeConfig>();
	
    private Long jurisdictionNumber;
    private CourtConfig courtConfig;
    private String courtPrettyName;

	public Court() {
		super();
	}
	public Court(Long pk) {
		setPrimaryKey(pk);
	}
	
	@Id
	@Column(name="COURT_ID")
	public Long getPrimaryKey() {
		return primaryKey;
	}
	@OneToOne
	@JoinColumn(name="COLLECTION_ID")
	public CollectionEntity getCollection() {
		return collection;
	}
	@Column(name="COURT_CLUSTER")
	public String getCourtCluster() {
		return courtCluster;
	}

	@Column(name="COURT_NORM")
	public String getCourtNorm() {
		return courtNorm;
	}
	//TODO: get from court_config?
	@Column(name="PUBLICATION_ID")
	public Long getPublicationId() {
		return publicationId;
	}

	@OneToOne
	@JoinColumn(name="STATE_ID")
	public State getState()
	{
		return state;
	}
	
	@Column(name = "JURIS_NUM")
	public Long getJurisdictionNumber() {
		return jurisdictionNumber;
	}
	
	@Column(name = "COURT_PRETTY_NAME")
	public String getCourtPrettyName() {
		return courtPrettyName;
	}
    
	@OneToOne
	@JoinColumn(name="PRODUCT_ID")
	public Product getProduct()
	{
		return product;
	}
	
    @OneToOne
    @JoinColumn(name = "COURT_ID", nullable = false)
    public CourtConfig getCourtConfig()
    {
    	return courtConfig;
    }

    @Column(name="SCRAPE_VENDOR_ID")
    public Long getScrapeVendorId() {
		return scrapeVendorId;
	}

    public void setScrapeVendorId(Long scrapeVendorId) {
		this.scrapeVendorId = scrapeVendorId;
	}

    
   
    @OneToMany(targetEntity = CourtCaseTypeConfig.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "COURT_ID", nullable = false,  insertable = false, updatable  = false)  
    @org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.ALL } )
   public List<CourtCaseTypeConfig> getCourtCaseTypeConfigs() {
		return courtCaseTypeConfigs;
	}
	
	@Transient
	public CourtEnum getCourtEnum() {
		int key = (int) ((long) primaryKey);
		return CourtEnum.findByKey(key);
	}

	public void setCourtCaseTypeConfigs(
			List<CourtCaseTypeConfig> courtCaseTypeConfigs) {
		this.courtCaseTypeConfigs = courtCaseTypeConfigs;
	}
	
	public void setProduct(Product product)
	{
		this.product = product;
	}
	
	public void setState(State state)
	{
		this.state = state;
	}
	
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}
	public void setCollection(CollectionEntity coll) {
		this.collection = coll;
	}
	public void setCourtCluster(String courtCluster) {
		this.courtCluster = courtCluster;
	}
	public void setCourtNorm(String courtNorm) {
		this.courtNorm = courtNorm;
	}
	public void setPublicationId(Long publicationId)
	{
		this.publicationId = publicationId;
	}
	

	public void setJurisdictionNumber(Long jurisdictionNumber) {
		this.jurisdictionNumber = jurisdictionNumber;
	}
	public void setCourtPrettyName(String courtPrettyName) {
		this.courtPrettyName = courtPrettyName;
	}
    public void setCourtConfig(CourtConfig courtConfig)
    {
    	this.courtConfig = courtConfig;
    }


    
    @Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((collection == null) ? 0 : collection.hashCode());
		result = prime * result + ((courtCaseTypeConfigs == null) ? 0 : courtCaseTypeConfigs.hashCode());
		result = prime * result + ((courtCluster == null) ? 0 : courtCluster.hashCode());
		result = prime * result + ((courtConfig == null) ? 0 : courtConfig.hashCode());
		result = prime * result + ((courtNorm == null) ? 0 : courtNorm.hashCode());
		result = prime * result + ((courtPrettyName == null) ? 0 : courtPrettyName.hashCode());
		result = prime * result + ((jurisdictionNumber == null) ? 0 : jurisdictionNumber.hashCode());
		result = prime * result + ((primaryKey == null) ? 0 : primaryKey.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((publicationId == null) ? 0 : publicationId.hashCode());
		result = prime * result + ((scrapeVendorId == null) ? 0 : scrapeVendorId.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Court other = (Court) obj;
		if (collection == null) {
			if (other.collection != null)
				return false;
		} else if (!collection.equals(other.collection))
			return false;
		if (courtCaseTypeConfigs == null) {
			if (other.courtCaseTypeConfigs != null)
				return false;
		} else if (!courtCaseTypeConfigs.equals(other.courtCaseTypeConfigs))
			return false;
		if (courtCluster == null) {
			if (other.courtCluster != null)
				return false;
		} else if (!courtCluster.equals(other.courtCluster))
			return false;
		if (courtConfig == null) {
			if (other.courtConfig != null)
				return false;
		} else if (!courtConfig.equals(other.courtConfig))
			return false;
		if (courtNorm == null) {
			if (other.courtNorm != null)
				return false;
		} else if (!courtNorm.equals(other.courtNorm))
			return false;
		if (courtPrettyName == null) {
			if (other.courtPrettyName != null)
				return false;
		} else if (!courtPrettyName.equals(other.courtPrettyName))
			return false;
		if (jurisdictionNumber == null) {
			if (other.jurisdictionNumber != null)
				return false;
		} else if (!jurisdictionNumber.equals(other.jurisdictionNumber))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (publicationId == null) {
			if (other.publicationId != null)
				return false;
		} else if (!publicationId.equals(other.publicationId))
			return false;
		if (scrapeVendorId == null) {
			if (other.scrapeVendorId != null)
				return false;
		} else if (!scrapeVendorId.equals(other.scrapeVendorId))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
    
	public final boolean matchesByPrimaryKey(final Court otherCourt) {
		return (otherCourt != null) && (primaryKey.equals(otherCourt.primaryKey));
	}
}

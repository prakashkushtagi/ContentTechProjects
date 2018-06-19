package com.trgr.dockets.core.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
@Table(name="WIPLA_NOVUS_CONTENT", schema="DOCKETS_PUB")
public class WiplaNovusContent implements Serializable {

	private static final long serialVersionUID = -7530601815045849749L;

	public static String LEGACY_ID = "legacyId";
	public static String CONTENT = "content";

	/**  */
	@Id
	@Column(name = "LEGACY_ID")
	private String legacyId;
	/**   */

	@Column(name = "CONTENT")
	private String content;
	
	@Column(name = "LAST_UPDATED_TIMESTAMP")
	private Timestamp lastUpdatedTimestamp;

	public String getLegacyId()
	{
		return legacyId;
	}

	public String getContent()
	{
		return content;
	}

	public void setlegacyId(String legacyId)
	{
		this.legacyId = legacyId;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
	
	public void updateTimestamp(){
		Date currentDate = new Date();
		Long time = currentDate.getTime();
		lastUpdatedTimestamp = new Timestamp(time);
	}
	
	public Timestamp getLastUpdatedTimestamp(){
		return lastUpdatedTimestamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other)
	{
		if ((this == other))
		{
			return true;
		}

		if ((other == null))
		{
			return false;
		}

		if (!(other instanceof WiplaNovusContent))
		{
			return false;
		}

		WiplaNovusContent castOther = (WiplaNovusContent) other;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(this.legacyId, castOther.getLegacyId());
		equalsBuilder.append(this.content, castOther.getContent());
		return equalsBuilder.isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(this.legacyId);
		hashCodeBuilder.append(this.content);

		return hashCodeBuilder.toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();

		buffer.append("WiplaNovusContent@").append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("legacgyId='").append(getLegacyId()).append("' ");
		buffer.append("content='").append(getContent()).append("' ");
		buffer.append("]");

		return buffer.toString();
	}
}

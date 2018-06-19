/**
 * 
 */
package com.trgr.dockets.core.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author C047166
 *
 */
@Entity
@Table(name="COURT_COLL_MAP_KEY", schema="DOCKETS_PUB")
public class CourtCollectionMapKey
{
	private CourtCollectionMapPrimaryKey primaryKey;
	private CollectionEntity collection;
	
	/**
	 * 
	 */
	public CourtCollectionMapKey()
	{
		super();
	}
	
	@Id
	@AttributeOverrides({
		@AttributeOverride(name="courtId", column=@Column(name="COURT_ID")),
		@AttributeOverride(name="mapKey", column=@Column(name="MAP_KEY"))
	})
	public CourtCollectionMapPrimaryKey getPrimaryKey()
	{
		return primaryKey;
	}
	
	public void setPrimaryKey(CourtCollectionMapPrimaryKey primaryKey)
	{
		this.primaryKey = primaryKey;
	}
	
	@OneToOne
	@JoinColumn(name="COLLECTION_ID")
	public CollectionEntity getCollection()
	{
		return collection;
	}
	public void setCollection(CollectionEntity collection)
	{
		this.collection = collection;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((collection == null) ? 0 : collection.hashCode());
		result = prime * result + ((primaryKey == null) ? 0 : primaryKey.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		CourtCollectionMapKey other = (CourtCollectionMapKey) obj;
		if (collection == null)
		{
			if (other.collection != null) return false;
		}
		else if (!collection.equals(other.collection)) return false;
		if (primaryKey == null)
		{
			if (other.primaryKey != null) return false;
		}
		else if (!primaryKey.equals(other.primaryKey)) return false;
		return true;
	}

	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}

package com.trgr.dockets.core.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.util.Assert;

@Entity
@Table(name="DICTIONARY_NORMALIZED", schema = "DOCKETS_PREPROCESSOR")
public class DictionaryNormalized {

	private DictionaryNormalizedKey primaryKey;
	private String normalizedName;
	
	public DictionaryNormalized(){
		super();
	}

	@Id
	@AttributeOverrides({
			@AttributeOverride(name="courtId", column=@Column(name="COURT_ID")),
			@AttributeOverride(name="originalName", column=@Column(name="NAME")),
			@AttributeOverride(name="typeId", column=@Column(name="TYPE_ID"))
	})
	public DictionaryNormalizedKey getPrimaryKey() {
		return primaryKey;
	}
	
	@Column(name="NORMALIZED_NAME")
	public String getNormalizedName() {
		return normalizedName;
	}

	public void setPrimaryKey(DictionaryNormalizedKey primaryKey) {
		Assert.notNull(primaryKey);
		this.primaryKey = primaryKey;
	}

	public void setNormalizedName(String normalizedName) {
		this.normalizedName = normalizedName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((normalizedName == null) ? 0 : normalizedName.hashCode());
		result = prime * result
				+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
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
		DictionaryNormalized other = (DictionaryNormalized) obj;
		if (normalizedName == null) {
			if (other.normalizedName != null)
				return false;
		} else if (!normalizedName.equals(other.normalizedName))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DictionaryNormalized [primaryKey=" + primaryKey
				+ ", normalizedName=" + normalizedName + "]";
	}
}

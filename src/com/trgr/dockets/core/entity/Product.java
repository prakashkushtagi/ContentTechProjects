package com.trgr.dockets.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.Assert;

import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;

@Entity
@Table(name="PRODUCT",schema="DOCKETS_PUB")
public class Product {
	
	private ProductEnum primaryKey;
	private String code;
	private String displayName;
	
	public Product() {
		super();
	}
	public Product(ProductEnum key) {
		setPrimaryKey(key);
	}
	
	@Id
	@Column(name= "PRODUCT_ID")
	public Long getId() {
		return primaryKey.getKey();
	}
	@Transient
	public ProductEnum getPrimaryKey() {
		return primaryKey;
	}
	
	public final boolean hasPrimaryKey(final ProductEnum productEnum) {
		return primaryKey.equals(productEnum);
	}
	
	@Column(name= "PRODUCT_CODE")
	public String getCode() {
		return code;
	}
	@Column(name= "DISPLAY_NAME")
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public void setId(Long id) {
		setPrimaryKey(ProductEnum.findByKey(id));
	}
	public void setPrimaryKey(ProductEnum key) {
		Assert.notNull(key);
		this.primaryKey = key;
	}
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result
				+ ((displayName == null) ? 0 : displayName.hashCode());
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
		Product other = (Product) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (primaryKey != other.primaryKey)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Product [primaryKey=" + primaryKey + ", code=" + code
				+ ", displayName=" + displayName + "]";
	}
}

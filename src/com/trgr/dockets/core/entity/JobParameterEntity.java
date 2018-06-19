package com.trgr.dockets.core.entity;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name="BATCH_JOB_EXECUTION_PARAMS", schema="DOCKETS_SPRING_BATCH")
public class JobParameterEntity {

	private JobParameterEntityKey primaryKey;
	private String stringVal;
	private Date dateVal;
	private Long longVal;
	private Double doubleVal;
	
	@Id
	@AttributeOverrides({
			@AttributeOverride(name="jobExecutionId", column=@Column(name="JOB_EXECUTION_ID")),
			@AttributeOverride(name="keyName", column=@Column(name="KEY_NAME"))
	})
	public JobParameterEntityKey getPrimaryKey() {
		return primaryKey;
	}
	@Column(name="STRING_VAL")
	public String getStringVal() {
		return stringVal;
	}
	@Column(name="DATE_VAL")
	public Date getDateVal() {
		return dateVal;
	}
	@Column(name="LONG_VAL")
	public Long getLongVal() {
		return longVal;
	}
	@Column(name="DOUBLE_VAL")
	public Double getDoubleVal() {
		return doubleVal;
	}
	
	public void setPrimaryKey(JobParameterEntityKey key) {
		this.primaryKey = key;
	}

	public void setStringVal(String stringVal) {
		this.stringVal = stringVal;
	}

	public void setDateVal(Date dateVal) {
		this.dateVal = dateVal;
	}

	public void setLongVal(Long longVal) {
		this.longVal = longVal;
	}

	public void setDoubleVal(Double doubleVal) {
		this.doubleVal = doubleVal;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}

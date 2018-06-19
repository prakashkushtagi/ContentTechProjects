/**
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved. Proprietary and 
 * Confidential information of TRGR. Disclosure, Use or Reproduction without the 
 * written authorization of TRGR is prohibited.
 *
 */
package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


@Entity
@Table(name = "DOCKETS_PUB.STATE")
public class State implements Serializable
{
	private static final long serialVersionUID = 5172325843255700474L;
	public static String STATE_ID = "stateId";
    public static String STATE_CODE = "stateCode";
    public static String STATE_FULL_NAME = "stateFullName";

    /**  */
    @Id
    @Column(name = "STATE_ID")
    private Long stateId;

    /**  */
    @Column(name = "STATE_CODE")
    private String stateCode;

    /**  */
    @Column(name = "STATE_FULL_NAME")
    private String stateFullName;

    /* (non-Javadoc)
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

        if (!(other instanceof State))
        {
            return false;
        }

        State castOther = (State) other;
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        equalsBuilder.append(this.stateCode, castOther.getStateCode());

        return equalsBuilder.isEquals();
    }

    public String getStateCode()
    {
        return stateCode;
    }

    public String getStateFullName()
    {
        return stateFullName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(this.stateCode);

        return hashCodeBuilder.toHashCode();
    }

    public void setStateCode(String stateCode)
    {
        this.stateCode = stateCode;
    }

    public void setStateFullName(String stateFullName)
    {
        this.stateFullName = stateFullName;
    }

    public void setStateId(Long stateId)
    {
        this.stateId = stateId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("State@").append(Integer.toHexString(hashCode())).append(" [");
        buffer.append("stateCode='").append(getStateCode()).append("' ");
        buffer.append("]");

        return buffer.toString();
    }
}

/**
 * Copyright 2017: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited.
 */
package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DOCKETS_PUB.PUBLISHING_CONTROL_DOC_GRABBER")
public class PublishingControlDocGrabber implements Serializable
{
	private static final long serialVersionUID = -8053949367881260405L;
	public static final String ARTIFACT = "artifact";
	public static final String PAUSE_FLAG = "pauseFlag";
	public static final String PAUSE_USER = "pauseUser";

	@Id
	@Column(name = "ARTIFACT")
	private String artifact;
	
	@Column(name = "PAUSE_USER")
	private String pauseUser;

	@Column(name = "PAUSE_FLAG")
	private String pauseFlag;

	public String getArtifact() {
		return artifact;
	}

	public void setArtifact(String artifact) {
		this.artifact = artifact;
	}

	public String getPauseUser() {
		return pauseUser;
	}

	public void setPauseUser(String pauseUser) {
		this.pauseUser = pauseUser;
	}

	public String getPauseFlag() {
		return pauseFlag;
	}

	public void setPauseFlag(String pauseFlag) {
		this.pauseFlag = pauseFlag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((artifact == null) ? 0 : artifact.hashCode());
		result = prime * result
				+ ((pauseFlag == null) ? 0 : pauseFlag.hashCode());
		result = prime * result
				+ ((pauseUser == null) ? 0 : pauseUser.hashCode());
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
		PublishingControlDocGrabber other = (PublishingControlDocGrabber) obj;
		if (artifact == null) {
			if (other.artifact != null)
				return false;
		} else if (!artifact.equals(other.artifact))
			return false;
		if (pauseFlag == null) {
			if (other.pauseFlag != null)
				return false;
		} else if (!pauseFlag.equals(other.pauseFlag))
			return false;
		if (pauseUser == null) {
			if (other.pauseUser != null)
				return false;
		} else if (!pauseUser.equals(other.pauseUser))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PublishingControlDocGrabber [artifact=" + artifact
				+ ", pauseUser=" + pauseUser + ", pauseFlag=" + pauseFlag + "]";
	}

}

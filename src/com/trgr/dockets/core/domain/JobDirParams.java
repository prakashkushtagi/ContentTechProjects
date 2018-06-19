package com.trgr.dockets.core.domain;

import java.io.File;
import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


public class JobDirParams implements Serializable {
	
	private static final long serialVersionUID = 20530885414321343L;

	private File logDir;
	private File errorDir;
	private File workDir;
	private File novusDir;
	
	public JobDirParams(File logDir, File errorDir, File workDir, File novusDir) {
		this.logDir = logDir;
		this.errorDir = errorDir;
		this.workDir = workDir;
		this.novusDir = novusDir;
	}

	public File getLogDir() {
		return logDir;
	}
	public File getErrorDir() {
		return errorDir;
	}
	public File getWorkDir() {
		return workDir;
	}
	public File getNovusDir() {
		return novusDir;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((errorDir == null) ? 0 : errorDir.hashCode());
		result = prime * result + ((logDir == null) ? 0 : logDir.hashCode());
		result = prime * result
				+ ((novusDir == null) ? 0 : novusDir.hashCode());
		result = prime * result + ((workDir == null) ? 0 : workDir.hashCode());
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
		JobDirParams other = (JobDirParams) obj;
		if (errorDir == null) {
			if (other.errorDir != null)
				return false;
		} else if (!errorDir.equals(other.errorDir))
			return false;
		if (logDir == null) {
			if (other.logDir != null)
				return false;
		} else if (!logDir.equals(other.logDir))
			return false;
		if (novusDir == null) {
			if (other.novusDir != null)
				return false;
		} else if (!novusDir.equals(other.novusDir))
			return false;
		if (workDir == null) {
			if (other.workDir != null)
				return false;
		} else if (!workDir.equals(other.workDir))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}

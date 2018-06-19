package com.trgr.dockets.core.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@XmlRootElement(name="jobRequest")
@XmlType (propOrder={"user", "requestName", "courtName", "collection", "parameters"})
public class DeleteRequest {
	
	/** The legal values for the name attribute of the parameter element. */
	public enum ParameterNameEnum { guid };
	
	private String user;
	private String requestName;
	private String courtName;
	private String collection;
	private Parameters parameters = new Parameters();
	
	@XmlElement(name="user")
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	@XmlElement(name="requestName")
	public String getRequestName() {
		return requestName;
	}
	
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	
	@XmlElement(name="courtName")
	public String getCourtName() {
		return courtName;
	}
	
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	
	@XmlElement(name="collection")
	public String getCollection() {
		return collection;
	}
	
	public void setCollection(String collection) {
		this.collection = collection;
	}
	
	@XmlElement(name="parameters")
	public Parameters getParameters() {
		return parameters;
	}
	
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
		
	@XmlTransient
	public List<String> getGuids() {
		return findParameterValues(String.class, ParameterNameEnum.guid);
	}
	
	private Parameters findParameters(ParameterNameEnum searchName) {
		List<Parameter> parameterList = getParameters().getParameterList();
		List<Parameter> returnList = new ArrayList<Parameter>();
		for (Parameter param : parameterList) {
			if (param.getName().equals(searchName.name())) {
				returnList.add(param);
			}
		}
		Parameters params = new Parameters();
		params.setParameterList(returnList);
		return params;
	}
	
	@SuppressWarnings("unused")
	private List<String> findParameterValues(ParameterNameEnum searchName) {
		List<Parameter> params = findParameters(searchName).getParameterList();
		List<String> values = new ArrayList<String>();
		for (Parameter param : params) {
			values.add(param.getValue());
		}
		return values;
	}
	
	private <T> List<T> findParameterValues(Class<T> clazz, ParameterNameEnum searchName) {
		List<Parameter> params = findParameters(searchName).getParameterList();
		List<T> values = new ArrayList<T>();
		
		try {
			for (Parameter param : params) {
				values.add(clazz.getConstructor(param.getValue().getClass()).newInstance(param.getValue()));
			}
		} catch (IllegalArgumentException e) {
			values = null;
		} catch (SecurityException e) {
			values = null;
		} catch (InstantiationException e) {
			values = null;
		} catch (IllegalAccessException e) {
			values = null;
		} catch (InvocationTargetException e) {
			values = null;
		} catch (NoSuchMethodException e) {
			values = null;
		}
		
		return values;
	}
	
	@XmlType
	public static class Parameters {

		private List<Parameter> parameterList = new ArrayList<Parameter>();
		
		public void addParameter(Parameter param) {
			parameterList.add(param);
		}
		
		@XmlElement(name="parameter")
		public List<Parameter> getParameterList() {
			return parameterList;
		}
		
		public void setParameterList(List<Parameter> parameters) {
			this.parameterList = parameters;
		}
		
		@Override
		public String toString() {
			return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
	}
	
	@XmlType
	public static class Parameter {
		
		String name;
		String value;
		
		public Parameter() {
			super();
		}
		
		public Parameter(String name, String value) {
			setName(name);
			setValue(value);
		}
		
		@XmlAttribute(name="name")
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		@XmlValue
		public String getValue() {
			return value;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((user == null) ? 0 : user.hashCode());
		result = prime * result
				+ ((requestName == null) ? 0 : requestName.hashCode());
		result = prime * result
				+ ((courtName == null) ? 0 : courtName.hashCode());
		result = prime * result
				+ ((collection == null) ? 0 : collection.hashCode());
		result = prime * result
				+ ((parameters == null) ? 0 : parameters.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeleteRequest other = (DeleteRequest) obj;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (requestName == null) {
			if (other.requestName != null)
				return false;
		} else if (!requestName.equals(other.requestName))
			return false;
		if (courtName == null) {
			if (other.courtName != null)
				return false;
		} else if (!courtName.equals(other.courtName))
			return false;
		if (collection == null) {
			if (other.collection != null)
				return false;
		} else if (!collection.equals(other.collection))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		return true;
	}
}

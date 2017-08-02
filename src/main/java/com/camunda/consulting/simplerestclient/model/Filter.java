package com.camunda.consulting.simplerestclient.model;

import java.util.HashMap;
import java.util.Map;

public class Filter {

	private String id = "";
	private Map<String, String> parameters = new HashMap<String, String>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	public Filter addParameter(String key, String value) {
		this.parameters.put(key, value);
		return this;
	}

}

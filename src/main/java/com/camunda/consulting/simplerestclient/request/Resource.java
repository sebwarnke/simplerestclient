package com.camunda.consulting.simplerestclient.request;

import java.util.ArrayList;
import java.util.List;

public class Resource {

	private final String resourceName;
	private final List<String> descriptorChain;

	public Resource(String resourceName) {
		this.resourceName = resourceName;
		this.descriptorChain = new ArrayList<String>();
	}
	
	public Resource resourceDescriptor(String descriptor) {
		this.descriptorChain.add(descriptor);
		return this;
	}
	
	@Override
	public String toString() {
		String result = "/" + resourceName;
		
		for (String descriptor : descriptorChain) {
			result += "/" + descriptor;
		}
		return result;
	}
}

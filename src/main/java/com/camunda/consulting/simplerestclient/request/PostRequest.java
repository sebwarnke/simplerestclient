package com.camunda.consulting.simplerestclient.request;

import java.io.Serializable;

import org.json.JSONObject;

import com.camunda.consulting.simplerestclient.exceptions.RestClientException;
import com.camunda.consulting.simplerestclient.model.Filter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;

public class PostRequest extends Request {
	
	private ObjectMapper bodyMapper = new ObjectMapper();
	private final Serializable body;
	
	public PostRequest(Filter filter, String restUri, String endPoint, Serializable body) {
		super(filter, restUri, endPoint);
		this.body = body;
	}
	
	public PostRequest(String restUri, String endPoint, Serializable body) {
		super(null, restUri, endPoint);
		this.body = body;
	}
	
	public void setBodyMapper(ObjectMapper bodyMapper) {
		this.bodyMapper = bodyMapper;
	}

	@Override
	protected HttpRequest createRequest(String requestString) throws RestClientException {
		HttpRequest result = null;
		
		String writeValueAsString;
		try {
			writeValueAsString = bodyMapper.writeValueAsString(body);
			JSONObject jsonObject = new JSONObject(writeValueAsString);
			result = Unirest.post(requestString).body(jsonObject).getHttpRequest();
		} catch (JsonProcessingException e) {
			throw new RestClientException("cannot unmarshall request body", e);
		}
		
		return result;
		
	}
	
}

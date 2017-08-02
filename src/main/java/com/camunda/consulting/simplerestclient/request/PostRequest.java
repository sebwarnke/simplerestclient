package com.camunda.consulting.simplerestclient.request;

import com.camunda.consulting.simplerestclient.model.Filter;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;

public class PostRequest<T> extends Request<T> {
	
	private T body;
	
	public PostRequest(Filter filter, String restUri, String endPoint, T body, Class<T> clazz) {
		super(filter, restUri, endPoint, clazz);
		this.body = body;
	}
	
	public PostRequest(String restUri, String endPoint, T body, Class<T> clazz) {
		super(null, restUri, endPoint, clazz);
		this.body = body;
	}

	@Override
	protected HttpRequest createRequest(String requestString) {
		
		HttpRequest result = null;
		result = Unirest.post(requestString).body(body).getHttpRequest();
		
		return result;
		
	}
	
}

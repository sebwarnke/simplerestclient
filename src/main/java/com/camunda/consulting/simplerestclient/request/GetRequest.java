package com.camunda.consulting.simplerestclient.request;

import com.camunda.consulting.simplerestclient.model.Filter;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;

public class GetRequest<T> extends Request<T> {

	public GetRequest(Filter filter, String restUri, String restEndPoint, Class<T> clazz) {
		super(filter, restUri, restEndPoint, clazz);
	}
	
	public GetRequest(String restUri, String restEndPoint, Class<T> clazz) {
		super(null, restUri, restEndPoint, clazz);
	}

	@Override
	protected HttpRequest createRequest(String requestString) {
		
		HttpRequest result = null;
		result = Unirest.get(requestString);
		
		return result;
	}

}

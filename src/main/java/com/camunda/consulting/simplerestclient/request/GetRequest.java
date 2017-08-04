package com.camunda.consulting.simplerestclient.request;

import com.camunda.consulting.simplerestclient.model.Filter;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;

public class GetRequest extends Request {

	public GetRequest(Filter filter, String restUri, String restEndPoint) {
		super(filter, restUri, restEndPoint);
	}
	
	public GetRequest(String restUri, String restEndPoint) {
		super(null, restUri, restEndPoint);
	}

	@Override
	protected HttpRequest createRequest(String requestString) {
		
		HttpRequest result = null;
		result = Unirest.get(requestString);
		
		return result;
	}

}

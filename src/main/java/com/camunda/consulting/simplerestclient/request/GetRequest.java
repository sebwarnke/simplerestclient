package com.camunda.consulting.simplerestclient.request;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;

public class GetRequest extends Request {

	public GetRequest(String restUri, String restEndPoint) {
		super(restUri, restEndPoint);
	}
	
	public GetRequest(String restUri, String restEndPoint, String resourceIdentifier) {
		super(restUri, restEndPoint, resourceIdentifier);
	}

	@Override
	protected HttpRequest createRequest(String requestString) {

		HttpRequest result = null;
		result = Unirest.get(requestString);

		return result;
	}

}

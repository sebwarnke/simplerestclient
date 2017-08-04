package com.camunda.consulting.simplerestclient.request;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camunda.consulting.simplerestclient.exceptions.RestClientException;
import com.camunda.consulting.simplerestclient.model.Filter;
import com.camunda.consulting.simplerestclient.response.Response;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

public abstract class Request {

	protected static final Logger log = LoggerFactory.getLogger(Request.class);

	protected String restUri;
	protected String endPoint;
	protected Filter filter;

	private Map<String, String> headers = new HashMap<String, String>();

	public Request(Filter filter, String restUri, String restEndPoint) {
		this.filter = filter;
		this.restUri = restUri;
		this.endPoint = restEndPoint;
	}

	protected abstract HttpRequest createRequest(String requestString) throws RestClientException;

	public Response submit() throws RestClientException {

		Response result = null;

		HttpResponse<JsonNode> httpResponse = null;

		String requestString = getRequestString();

		log.info("submitting get request: {}", requestString);

		try {
			HttpRequest request = createRequest(requestString);
			request.headers(headers);
			httpResponse = request.asJson();
			
			result = new Response(httpResponse);

		} catch (UnirestException e) {
			throw new RestClientException("request failed: " + requestString, e);
		}
		
		return result;
	}

	public Request addHeader(String key, String value) {
		headers.put(key, value);
		return this;
	}

	public void removeHeader(String key) {
		headers.remove(key);
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getRequestString() {

		String requestString = restUri + endPoint;

		if (filter == null) {
			// request string stays uri + end point
		} else if (filter.getId() == null || filter.getId().isEmpty()) {
			// assemble filter uri parameters
			requestString += convertMapToFilterString(filter.getParameters());
		} else {
			requestString += "/" + filter.getId();
		}

		return requestString;
	}

	private String convertMapToFilterString(Map<String, String> parameters) {

		String result = "";

		if (parameters.size() > 0) {
			result += "?";

			Set<Entry<String, String>> parameterEntrySet = parameters.entrySet();
			Iterator<Entry<String, String>> parameterIterator = parameterEntrySet.iterator();

			while (parameterIterator.hasNext()) {

				Entry<String, String> mapEntry = parameterIterator.next();
				String filterKeyValue = mapEntry.getKey() + "=" + mapEntry.getValue();
				result += filterKeyValue;
				if (parameterIterator.hasNext()) {
					result += "&";
				}
			}
		} else {
			// parameters.size <= 0 -> no uri parameters
		}

		return result;
	}
}

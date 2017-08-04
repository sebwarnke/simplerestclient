package com.camunda.consulting.simplerestclient.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class Response {

	private static final Logger log = LoggerFactory.getLogger(Response.class);
	
	private final HttpResponse<JsonNode> httpResponse;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public Response(HttpResponse<JsonNode> httpResponse) {
		this.httpResponse = httpResponse;
	}
	
	public <T> List<T> getResults(Class<T> resultType) {
		
		JsonNode body = httpResponse.getBody();
		
		List<T> resultList = new ArrayList<T>();
		
		try {
			resultList = parse(body, resultType);
		} catch (IOException e) {
			log.error("cannot unmarshall response to type <"+ resultType.getSimpleName() +">", e);
		}
		
		return resultList;
	}

	public <T> T getSingleResult(Class<T> resultType) {
		List<T> resultList = getResults(resultType);
		return resultList.get(0);
	}

	public int getStatus() {
		return httpResponse.getStatus();
	}

	public String getStatusText() {
		return httpResponse.getStatusText();
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	private <T> List<T> parse(JsonNode bodyJsonNode, Class<T> returnType) throws IOException {

		List<T> result = new ArrayList<T>();
		
		if (bodyJsonNode.isArray()) {
			JSONArray jsonArray = bodyJsonNode.getArray();
			
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				T resultObject = unmarshall(jsonObject, returnType);
				result.add(resultObject);
			}
			
		} else {
			JSONObject bodyJsonObject = bodyJsonNode.getObject();
			T resultObject = unmarshall(bodyJsonObject, returnType);
			result.add(resultObject);
		}
		
		return result;
	}

	private <T> T unmarshall(JSONObject bodyJsonObject, Class<T> clazz) throws IOException {
		
		T result;
		
		String jsonString = bodyJsonObject.toString();
		result = objectMapper.readValue(jsonString, clazz);
		return result;
	}
}

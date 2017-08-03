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

public class Response<T> {

	private static final Logger log = LoggerFactory.getLogger(Response.class);
	
	private final ObjectMapper objectMapper;
	private final Class<T> clazz;
	
	private List<T> resultList = new ArrayList<T>();

	private final int status;
	private final String statusText;
	
	public Response(HttpResponse<JsonNode> httpResponse, Class<T> clazz) throws IOException {
		objectMapper = new ObjectMapper();
		JsonNode bodyJsonNode = httpResponse.getBody();
		this.clazz = clazz;
		
		this.status = httpResponse.getStatus();
		this.statusText = httpResponse.getStatusText();
		
		resultList = parse(bodyJsonNode);
	}
	
	public Response(HttpResponse<JsonNode> httpResponse, ObjectMapper objectMapper, Class<T> clazz) throws IOException {

		log.info("response will be parsed by custom object mapper");
		this.objectMapper = objectMapper;
		
		JsonNode bodyJsonNode = httpResponse.getBody();
		this.clazz = clazz;
		
		this.status = httpResponse.getStatus();
		this.statusText = httpResponse.getStatusText();
		
		resultList = parse(bodyJsonNode);
	}

	public List<T> getResults() {
		return resultList;
	}

	public T getSingleResult() {
		return resultList.get(0);
	}

	public int getStatus() {
		return status;
	}

	public String getStatusText() {
		return statusText;
	}

	private List<T> parse(JsonNode bodyJsonNode) throws IOException {

		List<T> result = new ArrayList<T>();
		
		if (bodyJsonNode.isArray()) {
			JSONArray jsonArray = bodyJsonNode.getArray();
			
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				T resultObject = unmarshall(jsonObject);
				result.add(resultObject);
			}
			
		} else {
			JSONObject bodyJsonObject = bodyJsonNode.getObject();
			T resultObject = unmarshall(bodyJsonObject);
			result.add(resultObject);
		}
		
		return result;
	}

	private T unmarshall(JSONObject bodyJsonObject) throws IOException {
		
		T result;
		
		String jsonString = bodyJsonObject.toString();
		result = objectMapper.readValue(jsonString, clazz);
		return result;
	}
}

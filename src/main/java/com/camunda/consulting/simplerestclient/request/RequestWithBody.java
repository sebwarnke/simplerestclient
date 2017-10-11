package com.camunda.consulting.simplerestclient.request;

import java.io.Serializable;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestWithBody extends Request {

  private ObjectMapper bodyMapper = new ObjectMapper();
  private final Serializable body;

  public RequestWithBody(String endPoint, Serializable body) {
    super(endPoint);
    this.body = body;
  }
  
  public RequestWithBody(String endpoint, Serializable body, ObjectMapper customMapper) {
    super(endpoint);
    this.body = body;
    this.bodyMapper = customMapper;
  }

  public void setBodyMapper(ObjectMapper bodyMapper) {
    this.bodyMapper = bodyMapper;
  }

  public Serializable getBody() {
    return this.body;
  }
  
  public JSONObject bodyAsJson() throws JsonProcessingException {
    String bodyString = bodyAsString();
    JSONObject jsonObject = new JSONObject(bodyString);
    return jsonObject;
  }
  
  public String bodyAsString() throws JsonProcessingException {
    String bodyString = bodyMapper.writeValueAsString(body);
    return bodyString;
  }
}

package com.camunda.consulting.simplerestclient.response;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camunda.consulting.simplerestclient.exceptions.RestClientException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseWithBody<T extends Serializable> extends Response {

  private static final Logger log = LoggerFactory.getLogger(ResponseWithBody.class);
  
  private final Class<T> clazz;
  private ObjectMapper objectMapper = new ObjectMapper();
  
  protected final String responseString;
  private final JsonEntity jsonEntity;
  
  public ResponseWithBody(javax.ws.rs.core.Response httpResponse, Class<T> clazz) {
    
    super(httpResponse);
    
    this.clazz = clazz;
    this.responseString = httpResponse.readEntity(String.class);
    this.jsonEntity = new JsonEntity(this.responseString);
  }
  
  private List<T> parse(JsonEntity bodyJsonEntity) throws IOException {

    List<T> result = new ArrayList<T>();

    if (bodyJsonEntity.isArray()) {
      JSONArray jsonArray = bodyJsonEntity.getJsonArray();

      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        T resultObject = unmarshall(jsonObject);
        result.add(resultObject);
      }

    } else {
      JSONObject bodyJsonObject = bodyJsonEntity.getJsonObject();
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

  public List<T> getResults() {
  
    List<T> resultList = new ArrayList<T>();
  
    try {
      resultList = parse(jsonEntity);
    } catch (IOException e) {
      log.error("cannot unmarshall response to type <" + this.clazz.getSimpleName() + ">: {}", e.getMessage());
      throw new RestClientException("cannot unmarshall response to type <" + this.clazz.getSimpleName() + ">", e);
    }
  
    return resultList;
  }

  public T getSingleResult() {
    
    T result = null;
    
    List<T> resultList = getResults();
    
    if (resultList.size() > 1) {
      log.warn("returning first element from result list with more than 1 element");
    
    } else if (resultList.size() < 1) {
      log.warn("response is empty");
    
    } else {
      result = resultList.get(0);
    }
    
    return result;
  }
  
  public void setCustomMapper(ObjectMapper customMapper) {
    this.objectMapper = customMapper;
  }
  
  public String getResponseString() {
    return responseString;
  }
}

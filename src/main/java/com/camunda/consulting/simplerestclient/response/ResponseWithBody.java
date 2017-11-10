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
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * This class represents the response to a REST request. The response is
 * expected to contain an entity body able to be unmarshalled.
 * 
 * @author Sebastian Warnke (sebastian.warnke@camunda.com)
 *
 * @param <T>
 *          The type the entity body is to be unmarshalled to.
 */
public class ResponseWithBody<T extends Serializable> extends Response {

  private static final Logger log = LoggerFactory.getLogger(ResponseWithBody.class);

  /**
   * The expected entity type.
   */
  private final JavaType entityType;

  /**
   * The mapper that is used to unmarshall the entity body.
   */
  private ObjectMapper objectMapper = new ObjectMapper();

  /**
   * The response as String
   */
  protected final String responseString;

  /**
   * The entity body.
   */
  private final JsonEntity jsonEntity;

  /**
   * Constructor.
   * 
   * @param httpResponse
   *          HTTP response received from REST API
   * @param entityType
   *          The type the entity body is to be unmarshalled to
   */
  public ResponseWithBody(javax.ws.rs.core.Response httpResponse, Class<T> entityType) {

    super(httpResponse);

    this.entityType = TypeFactory.defaultInstance().constructSimpleType(entityType, null);
    this.responseString = httpResponse.readEntity(String.class);
    this.jsonEntity = new JsonEntity(this.responseString);
  }
  
  /**
   * Constructor.
   * 
   * @param httpResponse
   *          HTTP response received from REST API
   * @param entityType
   *          The type the entity body is to be unmarshalled to
   */
  public ResponseWithBody(javax.ws.rs.core.Response httpResponse, JavaType entityType) {

    super(httpResponse);

    this.entityType = entityType;
    this.responseString = httpResponse.readEntity(String.class);
    this.jsonEntity = new JsonEntity(this.responseString);
  }

  /**
   * This parses a {@link JsonEntity} to a List of {@code <T>}-typed objects.
   * 
   * @param bodyJsonEntity
   *          the entity to be parsed
   * @return In case the entity provided contains a {@link JSONObject}, the list
   *         contains a single element only. If the entity contains
   *         {@link JSONArray} the list can have multiple elements.
   * @throws IOException
   */
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
    result = objectMapper.readValue(jsonString, entityType);
    
    return result;
  }

  /**
   * @return List of {@code <T>}-typed objects representing the response entity. 
   */
  public List<T> getResults() {

    List<T> resultList = new ArrayList<T>();

    try {
      resultList = parse(jsonEntity);
    } catch (IOException e) {
      log.error("cannot unmarshall response to type <" + this.entityType.getTypeName() + ">: {}", e.getMessage());
      throw new RestClientException("cannot unmarshall response to type <" + this.entityType.getTypeName() + ">", e);
    }

    return resultList;
  }

  /**
   * @return First {@code <T>}-typed element of response entity list. 
   */
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

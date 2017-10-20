package com.camunda.consulting.simplerestclient.request;

import java.io.Serializable;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class represents a request with entity body.
 * 
 * @author Sebastian Warnke (sebastian.warnke@camunda.com)
 *
 */
public class RequestWithBody extends Request {

  /**
   * Mapper to be used to marshall {@code body}
   */
  private ObjectMapper bodyMapper = new ObjectMapper();

  /**
   * The entity body.
   */
  private final Serializable body;

  /**
   * Constructor.
   * 
   * @param endPoint
   *          This can be the first path element after the rest uri or the
   *          entire path of the request's destination
   * @param body
   *          the entity body
   */
  public RequestWithBody(String endPoint, Serializable body) {
    super(endPoint);
    this.body = body;
  }

  /**
   * Constructor.
   * 
   * @param endpoint
   *          This can be the first path element after the rest uri or the
   *          entire path of the request's destination
   * @param body
   *          the entity body
   * @param customMapper
   *          the mapper to be used to marshall {@code body}
   */
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

  /**
   * This returns the entity body as {@link JSONObject}
   * @return the entity body
   * @throws JsonProcessingException if body cannot be converted to {@link JSONObject}
   */
  public JSONObject bodyAsJson() throws JsonProcessingException {
    String bodyString = bodyAsString();
    JSONObject jsonObject = new JSONObject(bodyString);
    return jsonObject;
  }

  /**
   * This returns the entity body as JSON string.
   * @return the entity body as JSON string
   * @throws JsonProcessingException if boddy cannot be converted to String
   */
  public String bodyAsString() throws JsonProcessingException {
    String bodyString = bodyMapper.writeValueAsString(body);
    return bodyString;
  }
}

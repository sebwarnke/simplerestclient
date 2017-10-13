package com.camunda.consulting.simplerestclient;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camunda.consulting.simplerestclient.exceptions.RestClientException;
import com.camunda.consulting.simplerestclient.request.Request;
import com.camunda.consulting.simplerestclient.request.RequestWithBody;
import com.camunda.consulting.simplerestclient.request.RequestWithUrlEncodedData;
import com.camunda.consulting.simplerestclient.response.Response;
import com.camunda.consulting.simplerestclient.response.ResponseWithBody;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestClient {

  private static final Logger log = LoggerFactory.getLogger(RestClient.class);

  private final Client client;
  private final String restUri;
  private final WebTarget target;

  private ObjectMapper requestMapper = null;
  private ObjectMapper responseMapper = null;

  private MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();

  public RestClient(String restUri) {

    this.client = ClientBuilder.newBuilder().build();
    this.restUri = restUri;
    this.target = client.target(this.restUri);
  }

  public RestClient header(String key, String value) {
    headers.add(key, value);
    return this;
  }

  public void setHeaders(Map<String, Object> headers) {
    this.headers = new MultivaluedHashMap<>(headers);
  }

  public Request newRequest(String endpoint) {
    Request request = new Request(endpoint);
    request.setHeaders(headers);
    return request;
  }

  public RequestWithBody newRequestWithBody(String endpoint, Serializable body) {
    RequestWithBody request = new RequestWithBody(endpoint, body);
    request.setHeaders(headers);

    if (requestMapper != null) {
      request.setBodyMapper(requestMapper);
    }

    return request;
  }

  public RequestWithUrlEncodedData newRequestWithUrlEncodedData(String endpoint, Serializable dataTemplate) {
    RequestWithUrlEncodedData request = new RequestWithUrlEncodedData(endpoint);
    request.setHeaders(headers);

    Map<String, String> dataMap = convertObjectFieldsToMap(dataTemplate);
    for (Map.Entry<String, String> date : dataMap.entrySet()) {
      request.keyValuePair(date.getKey(), date.getValue());
    }
    
    return request;
  }

  private <T extends Serializable> ResponseWithBody<T> newResponseWithBody(javax.ws.rs.core.Response httpResponse, Class<T> returnType) {
    ResponseWithBody<T> response = new ResponseWithBody<T>(httpResponse, returnType);

    if (responseMapper != null) {
      response.setCustomMapper(responseMapper);
    }

    return response;
  }

  public <T extends Serializable> ResponseWithBody<T> get(Request request, Class<T> returnType) {

    ResponseWithBody<T> response = null;

    log.debug("GET Request: {}{}", restUri, request.getPath());
    log.debug("... with header information: {}", request.getHeaders());

    Builder builder = createInvocationBuilder(request);

    javax.ws.rs.core.Response httpResponse = builder.get();
    response = new ResponseWithBody<T>(httpResponse, returnType);

    if (responseMapper != null) {
      response.setCustomMapper(responseMapper);
    }

    return response;
  }

  public Response post(RequestWithBody request) {
    javax.ws.rs.core.Response httpResponse = invokePost(request);
    Response response = new Response(httpResponse);

    return response;
  }

  public Response post(RequestWithUrlEncodedData request) {
    Builder builder = createInvocationBuilder(request);
    javax.ws.rs.core.Response httpResponse = builder.post(Entity.entity(request.getUrlEncodedData(), MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    Response response = new Response(httpResponse);

    return response;
  }

  public <T extends Serializable> ResponseWithBody<T> post(RequestWithUrlEncodedData request, Class<T> returnType) {
    Builder builder = createInvocationBuilder(request);
    javax.ws.rs.core.Response httpResponse = builder.post(Entity.entity(request.getUrlEncodedData(), MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    ResponseWithBody<T> response = new ResponseWithBody<T>(httpResponse, returnType);

    return response;
  }

  public <T extends Serializable> ResponseWithBody<T> post(RequestWithBody request, Class<T> returnType) {
    javax.ws.rs.core.Response httpResponse = invokePost(request);
    ResponseWithBody<T> response = newResponseWithBody(httpResponse, returnType);

    return response;
  }

  private javax.ws.rs.core.Response invokePost(RequestWithBody request) {

    log.debug("POST Request: {}{}", restUri, request.getPath());
    log.debug("... with header information: {}", request.getHeaders());

    Builder builder = createInvocationBuilder(request);

    String jsonBody = null;
    try {
      jsonBody = request.bodyAsString();
    } catch (JsonProcessingException e) {
      throw new RestClientException("cannot marshall request body", e);
    }

    Entity<String> entity = Entity.json(jsonBody);

    javax.ws.rs.core.Response httpResponse = builder.post(entity);

    return httpResponse;
  }

  public Response put(Request request) {

    Response response = null;

    log.debug("PUT Request: {}{}", restUri, request.getPath());
    log.debug("... with header information: {}", request.getHeaders());

    Builder builder = createInvocationBuilder(request);

    javax.ws.rs.core.Response httpResponse = builder.put(Entity.json(null));
    response = new Response(httpResponse);

    return response;
  }

  public Response put(RequestWithBody request) {

    Response response = null;

    log.debug("PUT Request: {}{}", restUri, request.getPath());
    log.debug("... with header information: {}", request.getHeaders());

    Builder builder = createInvocationBuilder(request);

    String jsonBody = null;
    try {
      jsonBody = request.bodyAsString();
    } catch (JsonProcessingException e) {
      throw new RestClientException("cannot marshall request body", e);
    }

    javax.ws.rs.core.Response httpResponse = builder.put(Entity.json(jsonBody));
    response = new Response(httpResponse);

    return response;
  }

  private Builder createInvocationBuilder(Request request) {
    WebTarget fullTarget = target.path(request.getPath());
    Builder builder = fullTarget.request();

    // we don't use this.headers because header information could have been
    // changed in request.getHeaders()
    builder.headers(request.getHeaders());
    builder.accept(MediaType.APPLICATION_JSON);
    return builder;
  }

  /**
   * Goal of this method is to iterate all attributes of an object to build a
   * map of key and value of the attribute. * It expects {@code object} to have:
   * <ul>
   * <li>a well-named getter for each attribute</li>
   * <li>an annotation of type {@link JsonProperty} for each attribute denoting
   * its JSON name</li>
   * </ul>
   * 
   * @param object
   *          an object of a type as described above
   * @return Map of key-value pairs for each attribute of {@code object} that is
   *         not null. The map uses JsonProperty values as key.<br>
   *         Example:
   * 
   *         <pre>
   * &#64;JsonProperty("my-name")
   * private String myVar = "myValue"; 
   * translates to
   * Map{ ..., key[my-name] => value[myValue], ...}
   *         </pre>
   */
  private Map<String, String> convertObjectFieldsToMap(Object object) {
    Map<String, String> result = new HashMap<String, String>();
    // These are all declared fields/attributes of the class
    Field[] declaredFields = object.getClass().getDeclaredFields();
    // These are all declared methods of the class
    Method[] methods = object.getClass().getMethods();
    for (Field field : declaredFields) {
      String fieldName = field.getName();
      for (Method method : methods) {
        // This tries to find and invoke the getter method for a field
        String methodName = method.getName();
        if (methodName.equalsIgnoreCase("get" + fieldName)) {
          try {
            Object fieldFilterValue = method.invoke(object);
            if (fieldFilterValue == null) {
              // ignore this field
            } else {
              String fieldFilterValueString = fieldFilterValue.toString();
              JsonProperty jsonPropertyAnnotation = field.getAnnotation(JsonProperty.class);
              String jsonName = jsonPropertyAnnotation.value();
              result.put(jsonName, fieldFilterValueString);
            }
          } catch (Exception e) {
            log.warn("exception during invocation of '{}' from object '{}'", fieldName, object, e);
          }
        }
      }
    }
    return result;
  }

  public String getRestUri() {
    return restUri;
  }

  public ObjectMapper getRequestMapper() {
    return requestMapper;
  }

  public void setRequestMapper(ObjectMapper requestMapper) {
    this.requestMapper = requestMapper;
  }

  public ObjectMapper getResponseMapper() {
    return responseMapper;
  }

  public void setResponseMapper(ObjectMapper responseMapper) {
    this.responseMapper = responseMapper;
  }
}

package com.camunda.consulting.simplerestclient;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
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
import com.camunda.consulting.simplerestclient.util.CookieClientRequestFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * This is the Rest Client representation for requests to a certain REST API.
 * 
 * @author Sebastian Warnke (sebastian.warnke@camunda.com)
 */
public class RestClient {

  private static final Logger log = LoggerFactory.getLogger(RestClient.class);

  private final Client client;
  private final WebTarget target;

  /**
   * URI to the location of the REST API
   */
  private final String restUri;

  /**
   * Mapper to marhall requests. If not set a default mapper is being used.
   */
  private ObjectMapper requestMapper = null;

  /**
   * Mapper to unmarshall responses. If not set a default mapper is being used.
   */
  private ObjectMapper responseMapper = null;

  /**
   * Default headers sent during a request.
   */
  private MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();

  /**
   * Cookies sent during a request.
   */
  private List<Cookie> cookies = new ArrayList<Cookie>();

  /**
   * Constructor
   * 
   * @param restUri
   *          URI to the location of the REST API
   */
  public RestClient(String restUri) {

    this.client = ClientBuilder.newBuilder().build();
    this.restUri = restUri;
    this.target = client.target(this.restUri);
  }

  /**
   * This adds an header key value pair to the rest client's default headers.
   * 
   * @param key
   *          header key
   * @param value
   *          header value
   * @return this
   */
  public RestClient header(String key, String value) {
    headers.add(key, value);
    return this;
  }

  /**
   * This adds a cookie to the rest client's cookies.
   * 
   * @param name
   *          the name of the cookie.
   * @param value
   *          the value of the cookie.
   * @param path
   *          the URI path for which the cookie is valid.
   * @param domain
   *          the host domain for which the cookie is valid
   * @return this
   */
  public RestClient cookie(String name, String value, String path, String domain) {
    Cookie cookie = new Cookie(name, value, path, domain);
    cookies.add(cookie);
    return this;
  }

  public void setHeaders(Map<String, Object> headers) {
    this.headers = new MultivaluedHashMap<>(headers);
  }

  /**
   * This creates an instance of {@link Request} and sets the default headers of
   * the rest client.
   * 
   * @param endpoint
   *          the URI path, relative to {@code restUri}, the request is to be
   *          sent to.
   * @return request
   */
  public Request newRequest(String endpoint) {
    Request request = new Request(endpoint);
    request.setHeaders(headers);
    return request;
  }

  /**
   * This creates an instance of {@link RequestWithBody} and sets the default
   * headers of the rest client.
   * 
   * @param endpoint
   *          the URI path, relative to {@code restUri}, the request is to be
   *          sent to.
   * @param body
   *          the body which is to be sent in the request
   * @return request
   */
  public RequestWithBody newRequestWithBody(String endpoint, Serializable body) {
    RequestWithBody request = new RequestWithBody(endpoint, body);
    request.setHeaders(headers);

    if (requestMapper != null) {
      request.setBodyMapper(requestMapper);
    }

    return request;
  }

  /**
   * This creates an instance of {@link RequestWithUrlEncodedData} and sets the
   * default headers of the rest client.
   * 
   * @param endpoint
   *          the URI path, relative to {@code restUri}, the request is to be
   *          sent to.
   * @param dataTemplate
   *          the object which is used to be create URL-encoded data from.
   * @return request
   */
  public RequestWithUrlEncodedData newRequestWithUrlEncodedData(String endpoint, Serializable dataTemplate) {
    RequestWithUrlEncodedData request = new RequestWithUrlEncodedData(endpoint);
    request.setHeaders(headers);

    Map<String, String> dataMap = convertObjectFieldsToMap(dataTemplate);
    for (Map.Entry<String, String> date : dataMap.entrySet()) {
      request.keyValuePair(date.getKey(), date.getValue());
    }

    return request;
  }

  private <T extends Serializable> ResponseWithBody<T> newResponseWithBody(javax.ws.rs.core.Response httpResponse, JavaType returnType) {
    ResponseWithBody<T> response = new ResponseWithBody<T>(httpResponse, returnType);

    if (responseMapper != null) {
      response.setCustomMapper(responseMapper);
    }

    return response;
  }

  /**
   * This sends a GET request to the REST API located at {@code restUri}.
   * 
   * @param request
   *          The request to be sent.
   * @param entityType
   *          The data type of the response's entity.
   * @param <T>
   *          entity class
   * @return a response object containing unmarshalled data
   */
  public <T extends Serializable> ResponseWithBody<T> get(Request request, Class<T> entityType) {

    JavaType javatype = TypeFactory.defaultInstance().constructSimpleType(entityType, null);
    ResponseWithBody<T> response = this.get(request, javatype);

    return response;
  }

  /**
   * This sends a GET request to the REST API located at {@code restUri}.
   * 
   * @param request
   *          The request to be sent.
   * @param entityType
   *          The data type of the response's entity.
   * @param <T>
   *          entity class
   * @return a response object containing unmarshalled data
   */
  public <T extends Serializable> ResponseWithBody<T> get(Request request, JavaType entityType) {

    ResponseWithBody<T> response = null;

    log.debug("GET Request: {}{}{}", restUri, request.getPath(), request.getParameterPreview());
    log.debug("... with header information: {}", request.getHeaders());

    Builder builder = createInvocationBuilder(request);

    javax.ws.rs.core.Response httpResponse = builder.get();
    response = new ResponseWithBody<T>(httpResponse, entityType);

    if (responseMapper != null) {
      response.setCustomMapper(responseMapper);
    }

    return response;
  }

  /**
   * This sends a POST request to the REST API located at {@code restUri}.
   * 
   * @param request
   *          The request to be sent.
   * @return a response object
   */
  public Response post(RequestWithBody request) {
    javax.ws.rs.core.Response httpResponse = invokePost(request);
    Response response = new Response(httpResponse);

    return response;
  }

  /**
   * This sends a POST request to the REST API located at {@code restUri}.
   * 
   * @param request
   *          The request to be sent.
   * @param entityType
   *          The data type of the response's entity.
   * @param <T>
   *          entity class
   * @return a response object containing unmarshalled data
   */
  public <T extends Serializable> ResponseWithBody<T> post(RequestWithBody request, Class<T> entityType) {
    JavaType javatype = TypeFactory.defaultInstance().constructSimpleType(entityType, null);
    ResponseWithBody<T> response = post(request, javatype);

    return response;
  }

  /**
   * This sends a POST request to the REST API located at {@code restUri}.
   * 
   * @param request
   *          The request to be sent.
   * @param entityType
   *          The data type of the response's entity.
   * @param <T>
   *          entity class
   * @return a response object containing unmarshalled data
   */
  public <T extends Serializable> ResponseWithBody<T> post(RequestWithBody request, JavaType entityType) {
    javax.ws.rs.core.Response httpResponse = invokePost(request);
    ResponseWithBody<T> response = newResponseWithBody(httpResponse, entityType);

    return response;
  }

  /**
   * This sends a POST request to the REST API located at {@code restUri}.
   * 
   * @param request
   *          The request to be sent.
   * @return a response object
   */
  public Response post(RequestWithUrlEncodedData request) {
    Builder builder = createInvocationBuilder(request);
    javax.ws.rs.core.Response httpResponse = builder.post(Entity.entity(request.getUrlEncodedData(), MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    Response response = new Response(httpResponse);

    return response;
  }

  /**
   * This sends a POST request to the REST API located at {@code restUri}.
   * 
   * @param request
   *          The request to be sent.
   * @param entityType
   *          The data type of the response's entity.
   * @param <T>
   *          entity class
   * @return a response object containing unmarshalled data
   */
  public <T extends Serializable> ResponseWithBody<T> post(RequestWithUrlEncodedData request, Class<T> entityType) {
    JavaType javatype = TypeFactory.defaultInstance().constructSimpleType(entityType, null);
    ResponseWithBody<T> response = post(request, javatype);

    return response;
  }

  /**
   * This sends a POST request to the REST API located at {@code restUri}.
   * 
   * @param request
   *          The request to be sent.
   * @param entityType
   *          The data type of the response's entity.
   * @param <T>
   *          entity class
   * @return a response object containing unmarshalled data
   */
  public <T extends Serializable> ResponseWithBody<T> post(RequestWithUrlEncodedData request, JavaType entityType) {
    Builder builder = createInvocationBuilder(request);
    javax.ws.rs.core.Response httpResponse = builder.post(Entity.entity(request.getUrlEncodedData(), MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    ResponseWithBody<T> response = new ResponseWithBody<T>(httpResponse, entityType);

    return response;
  }

  private javax.ws.rs.core.Response invokePost(RequestWithBody request) {

    log.debug("POST Request: {}{}{}", restUri, request.getPath(), request.getParameterPreview());
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

  /**
   * This sends a PUT request to the REST API located at {@code restUri}.
   * 
   * @param request
   *          The request to be sent.
   * @return a response object
   */
  public Response put(Request request) {

    Response response = null;

    log.debug("PUT Request: {}{}{}", restUri, request.getPath(), request.getParameterPreview());
    log.debug("... with header information: {}", request.getHeaders());

    Builder builder = createInvocationBuilder(request);

    javax.ws.rs.core.Response httpResponse = builder.put(Entity.json(null));
    response = new Response(httpResponse);

    return response;
  }

  /**
   * This sends a PUT request to the REST API located at {@code restUri}.
   * 
   * @param request
   *          The request to be sent.
   * @return a response object
   */
  public Response put(RequestWithBody request) {

    Response response = null;

    log.debug("PUT Request: {}{}{}", restUri, request.getPath(), request.getParameterPreview());
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
  
  public Response put(RequestWithUrlEncodedData request) {
    Builder builder = createInvocationBuilder(request);
    javax.ws.rs.core.Response httpResponse = builder.put(Entity.entity(request.getUrlEncodedData(), MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    Response response = new Response(httpResponse);

    return response;
  }
  
  public Response delete(Request request) {
   
    Response response = null;
    
    log.debug("DELETE Request: {}{}{}", restUri, request.getPath(), request.getParameterPreview());
    log.debug("... with header information: {}", request.getHeaders());

    Builder builder = createInvocationBuilder(request);

    javax.ws.rs.core.Response httpResponse = builder.delete();
    response = new Response(httpResponse);
    
    return response;
  }

  private Builder createInvocationBuilder(Request request) {

    if (this.cookies.isEmpty() == false) {
      this.client.register(new CookieClientRequestFilter(this.cookies));
    }

    WebTarget fullTarget = target.path(request.getPath());
    
    for (Map.Entry<String, String> parameterEntry : request.getParameters().entrySet()) {
      fullTarget = fullTarget.queryParam(parameterEntry.getKey(), parameterEntry.getValue());
    }
    
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

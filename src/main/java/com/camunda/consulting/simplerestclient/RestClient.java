package com.camunda.consulting.simplerestclient;

import java.io.Serializable;
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
import com.camunda.consulting.simplerestclient.response.Response;
import com.camunda.consulting.simplerestclient.response.ResponseWithBody;
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

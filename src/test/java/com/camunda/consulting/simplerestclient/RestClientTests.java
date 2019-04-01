package com.camunda.consulting.simplerestclient;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.camunda.consulting.simplerestclient.request.Request;
import com.camunda.consulting.simplerestclient.response.Response;

public class RestClientTests {

  @Test
  public void testGetWithClientCertificate() throws Exception {
    
    String keystoreLocation = "/badssl.com-client.p12";
    String keystorePassword = "badssl.com";
    
    RestClient restClient = new RestClient("https://client.badssl.com", keystoreLocation, keystorePassword);
    
    Request request = restClient.newRequest("");
    Response response = restClient.get(request);
    Integer statusCode = response.getStatusCode();
    
    assertThat(statusCode, is(200));   
  }
}

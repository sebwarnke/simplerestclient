package com.camunda.consulting.simplerestclient.request;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class RequestWithUrlEncodedData extends Request {

  private MultivaluedMap<String, String> urlEncodedData;

  public RequestWithUrlEncodedData(String endpoint) {
    super(endpoint);
    this.urlEncodedData = new MultivaluedHashMap<String, String>();
  }
  
  public RequestWithUrlEncodedData keyValuePair(String key, String value) {
    urlEncodedData.add(key, value);
    return this;
  }
  
  public MultivaluedMap<String, String>getUrlEncodedData() {
    return this.urlEncodedData;
  }
}

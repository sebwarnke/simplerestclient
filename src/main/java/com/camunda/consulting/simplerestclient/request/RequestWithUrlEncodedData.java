package com.camunda.consulting.simplerestclient.request;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

/**
 * This class represents a request with url-encoded-form data.
 * 
 * @author Sebastian Warnke (sebastian.warnke@camunda.com)
 *
 */
public class RequestWithUrlEncodedData extends Request {

  /**
   * List of data to be sent as url-encoded-form data
   */
  private MultivaluedMap<String, String> urlEncodedData;

  /**
   * Constructor.
   * 
   * @param endPoint
   *          This can be the first path element after the rest uri or the
   *          entire path of the request's destination
   */
  public RequestWithUrlEncodedData(String endpoint) {
    super(endpoint);
    this.urlEncodedData = new MultivaluedHashMap<String, String>();
  }

  /**
   * This adds a tuple key and value to the list of url-encoded-form data.
   * @param key key of the date
   * @param value value of the date
   * @return this
   */
  public RequestWithUrlEncodedData keyValuePair(String key, String value) {
    urlEncodedData.add(key, value);
    return this;
  }

  public MultivaluedMap<String, String> getUrlEncodedData() {
    return this.urlEncodedData;
  }
}

package com.camunda.consulting.simplerestclient.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a REST request.
 * 
 * @author Sebastian Warnke (sebastian.warnke@camunda.com)
 *
 */
public class Request {

  protected static final Logger log = LoggerFactory.getLogger(Request.class);

  /**
   * resturi-relative path to the requests destination
   */
  protected final List<Path> paths = new ArrayList<Path>();

  /**
   * URI parameters to be used for the request
   */
  protected Map<String, String> parameters = new HashMap<String, String>();

  /**
   * headers to be sent in the request
   */
  private MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();

  /**
   * Constructor.
   * 
   * @param restEndPoint
   *          This can be the first path element after the rest uri or the
   *          entire path of the request's destination
   */
  public Request(String restEndPoint) {
    this.paths.add(new Path(restEndPoint));
  }

  /**
   * This adds a header key value pair.
   * 
   * @param key
   *          key of the header element
   * @param value
   *          value of the header element
   * @return this
   */
  public Request addHeader(String key, String value) {
    headers.add(key, value);
    return this;
  }

  /**
   * This removes a header element from the list of headers.
   * 
   * @param key
   *          key of the header to be removed
   */
  public void removeHeader(String key) {
    headers.remove(key);
  }

  public void setHeaders(Map<String, Object> headers) {
    this.headers = new MultivaluedHashMap<String, Object>(headers);
  }

  public void setHeaders(MultivaluedMap<String, Object> headers) {
    this.headers = headers;
  }

  public MultivaluedMap<String, Object> getHeaders() {
    return headers;
  }

  /**
   * This adds a further path element to the path
   * 
   * @param path
   *          path element to be added
   * @return this
   */
  public Request path(Path path) {
    paths.add(path);
    return this;
  }

  /**
   * This adds a List of path elements to the path.
   * 
   * @param elements
   *          elements to be added
   * @return this
   */
  public Request path(String... elements) {
    Path path = new Path(elements);
    return this.path(path);
  }

  /**
   * This adds an URI parameter to the request's parameters.
   * 
   * @param key
   *          key of the parameter
   * @param value
   *          value of the parameter
   * @return this
   */
  public Request restParameter(String key, String value) {
    this.parameters.put(key, value);
    return this;
  }

  /**
   * This sets the URI parameters to be sent in the request.
   * 
   * @param parameters
   *          URI parameters to be sent
   * @return this
   */
  public Request restParameters(Map<String, String> parameters) {
    this.parameters = new HashMap<String, String>(parameters);
    return this;
  }

  /**
   * Assembles and returns a string representation of the request's path.
   * 
   * @return the path
   */
  public String getPath() {

    String requestString = "";

    for (Path path : paths) {
      requestString += path.toString();
    }

    requestString += assembleParameters(parameters);

    return requestString;
  }

  /**
   * This converts a {@code Map<String, String>} to a url-encoded String
   * according this schema:
   * 
   * <pre>
   * [URI]?{key}={value}&{key}={value}...
   * </pre>
   * 
   * @param parameters parameters to be converted
   * @return url parameter string
   */
  private String assembleParameters(Map<String, String> parameters) {

    String result = "";

    if (parameters.size() > 0) {
      result += "?";

      Set<Entry<String, String>> parameterEntrySet = parameters.entrySet();
      Iterator<Entry<String, String>> parameterIterator = parameterEntrySet.iterator();

      while (parameterIterator.hasNext()) {

        Entry<String, String> mapEntry = parameterIterator.next();
        String parameterKeyValue = mapEntry.getKey() + "=" + mapEntry.getValue();

        try {
          result += URLEncoder.encode(parameterKeyValue, "utf-8");
        } catch (UnsupportedEncodingException e) {
          log.error("cannot url-encode parameter [{}]", parameterKeyValue, e);
        }

        if (parameterIterator.hasNext()) {
          result += "&";
        }
      }
    } else {
      // parameters.size <= 0 -> no uri parameters
    }

    return result;
  }
}

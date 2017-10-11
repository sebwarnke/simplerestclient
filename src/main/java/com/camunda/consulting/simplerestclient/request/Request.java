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

public class Request {

  protected static final Logger log = LoggerFactory.getLogger(Request.class);

  protected final List<Path> paths = new ArrayList<Path>();
  protected Map<String, String> parameters = new HashMap<String, String>();

  private MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();

  // private HttpClient customHttpClient = null;

  public Request(String restEndPoint) {
    this.paths.add(new Path(restEndPoint));
  }

  public Request(String restEndPoint, String resourceIdentifier) {
    this.paths.add(new Path(restEndPoint, resourceIdentifier));
  }

  public Request addHeader(String key, String value) {
    headers.add(key, value);
    return this;
  }

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

  public Request path(Path resource) {
    paths.add(resource);
    return this;
  }
 
  public Request path(String... elements) {
    Path path = new Path(elements);
    return this.path(path);
  }

  public Request restParameter(String key, String value) {
    this.parameters.put(key, value);
    return this;
  }

  public Request restParameters(Map<String, String> parameters) {
    this.parameters = new HashMap<String, String>(parameters);
    return this;
  }

  public String getPath() {

    String requestString = "";

    for (Path path : paths) {
      requestString += path.toString();
    }
    
    requestString += assembleParameters(parameters);

    return requestString;
  }

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

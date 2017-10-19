/* 
 * This implementation is from:
 * https://stackoverflow.com/questions/23358105/how-to-set-cookie-for-requests-built-via-resteasy-client-api
 */
package com.camunda.consulting.simplerestclient.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.ext.Provider;

/**
 * This class is used to get use of cookies working.
 * 
 * @author Sebastian Warnke (sebastian.warnke@camunda.com)
 *
 */
@Provider
public class CookieClientRequestFilter implements ClientRequestFilter {
  private List<Object> cookies;

  public CookieClientRequestFilter(List<Cookie> cookies) {
    super();
    this.cookies = new ArrayList<Object>(cookies);
  }

  @Override
  public void filter(ClientRequestContext clientRequestContext) throws IOException {
    clientRequestContext.getHeaders().put("Cookie", cookies);
  }

}

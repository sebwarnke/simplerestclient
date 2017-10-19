package com.camunda.consulting.simplerestclient.response;

/**
 * This class represents the response to a REST request.
 * @author Sebastian Warnke (sebastian.warnke@camunda.com)
 *
 */
public class Response {

  // private static final Logger log = LoggerFactory.getLogger(Response.class);

  /**
   * JAX-RS HTTP response this Response object is created from.
   */
  protected final javax.ws.rs.core.Response httpResponse;
  
  /**
   * Constructor.
   * @param httpResponse HTTP response received from REST API
   */
  public Response(javax.ws.rs.core.Response httpResponse) {
    this.httpResponse = httpResponse;
  }

  public Integer getStatus() {
    return httpResponse.getStatus();
  }

  public Integer getStatusCode() {
    return httpResponse.getStatusInfo().getStatusCode();
  }

  public String getStatusPhrase() {
    return httpResponse.getStatusInfo().getReasonPhrase();
  }

  /**
   * @return String representation of RAW response body
   */
  public String getRawResponseBody() {
    return httpResponse.readEntity(String.class);
  }

  @Override
  public String toString() {
    return "Response [status=" + getStatus() + ", statusCode=" + getStatusCode() + ", statusPhrase=" + getStatusPhrase() + "]";
  }
}

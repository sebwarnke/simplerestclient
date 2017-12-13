package com.camunda.consulting.simplerestclient.response;

/**
 * This class represents the response to a REST request.
 * @author Sebastian Warnke (sebastian.warnke@camunda.com)
 *
 */
public class Response {

  // private static final Logger log = LoggerFactory.getLogger(Response.class);

  private final Integer status;
  private final Integer statusCode;
  private final String statusPhrase;
  private final String rawResponseEntityString;
  
  /**
   * Constructor.
   * @param httpResponse HTTP response received from REST API
   */
  public Response(javax.ws.rs.core.Response httpResponse) {
    this.status = httpResponse.getStatus();
    this.statusCode = httpResponse.getStatusInfo().getStatusCode();
    this.statusPhrase = httpResponse.getStatusInfo().getReasonPhrase();
    this.rawResponseEntityString = httpResponse.readEntity(String.class);
  }

  public Integer getStatus() {
    return status;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public String getStatusPhrase() {
    return statusPhrase;
  }

  /**
   * @return String representation of RAW response body
   */
  public String getRawResponseEntityString() {
    return rawResponseEntityString;
  }

  @Override
  public String toString() {
    return "Response [status=" + getStatus() + ", statusCode=" + getStatusCode() + ", statusPhrase=" + getStatusPhrase() + "]";
  }
}

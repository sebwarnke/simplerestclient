package com.camunda.consulting.simplerestclient.response;

public class Response {

  // private static final Logger log = LoggerFactory.getLogger(Response.class);

  protected final javax.ws.rs.core.Response httpResponse;
  
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
  
  public String getRawResponseBody() {
    return httpResponse.readEntity(String.class);
  }

  @Override
  public String toString() {
    return "Response [status=" + getStatus() + ", statusCode=" + getStatusCode() + ", statusPhrase=" + getStatusPhrase() + "]";
  }
  
  
}

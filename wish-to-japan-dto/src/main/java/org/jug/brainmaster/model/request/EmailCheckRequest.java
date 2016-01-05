package org.jug.brainmaster.model.request;

/**
 * Created by willy on 1/18/2016.
 */
public class EmailCheckRequest {
  private String message;
  private String sessionId;

  public EmailCheckRequest(String message, String sessionId) {

    this.message = message;
    this.sessionId = sessionId;
  }

  public String getMessage() {
    return message;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  @Override
  public String toString() {
    return "org.jug.brainmaster.model.request.EmailCheckRequest{" +
        "message='" + message + '\'' +
        ", sessionId='" + sessionId + '\'' +
        '}';
  }
}

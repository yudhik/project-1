package org.jug.brainmaster.model.response;

/**
 * Created by willy on 1/18/2016.
 */
public class EmailCheckResponse {
  private boolean valid;
  private String sessionId;

  public EmailCheckResponse(String sessionId, boolean valid) {

    this.sessionId = sessionId;
    this.valid = valid;
  }

  public String getSessionId() {
    return sessionId;
  }

  public boolean isValid() {
    return valid;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public void setValid(boolean valid) {
    this.valid = valid;
  }

  @Override
  public String toString() {
    return "EmailCheckResponse{" +
        "sessionId='" + sessionId + '\'' +
        ", valid=" + valid +
        '}';
  }
}

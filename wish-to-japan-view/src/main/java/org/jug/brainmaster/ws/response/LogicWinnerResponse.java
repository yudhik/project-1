package org.jug.brainmaster.ws.response;

import java.util.List;
import java.util.Map;

public class LogicWinnerResponse {
  private boolean valid;
  private Map<String, List<UserResponse>> winners;

  public LogicWinnerResponse() {
    // TODO Auto-generated constructor stub
  }

  public LogicWinnerResponse(Map<String, List<UserResponse>> winners) {
    super();
    this.winners = winners;
  }

  public Map<String, List<UserResponse>> getWinners() {
    return this.winners;
  }

  public boolean isValid() {
    return this.valid;
  }

  public void setValid(boolean valid) {
    this.valid = valid;
  }

  public void setWinners(Map<String, List<UserResponse>> winners) {
    this.winners = winners;
  }
}

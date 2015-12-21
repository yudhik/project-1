package org.jug.brainmaster.model.response;

import java.util.List;
import java.util.Map;

public class WinnerResponse {

  private boolean isValid;
  private Map<String, List<UserResponse>> winners;

  public WinnerResponse() {
  }

  public WinnerResponse(boolean isValid, Map<String, List<UserResponse>> winners) {

    this.isValid = isValid;
    this.winners = winners;
  }

  public Map<String, List<UserResponse>> getWinners() {
    return winners;
  }

  public boolean isValid() {
    return isValid;
  }

  public void setValid(boolean valid) {
    isValid = valid;
  }

  public void setWinners(Map<String, List<UserResponse>> winners) {
    this.winners = winners;
  }
}

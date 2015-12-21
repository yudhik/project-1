package org.jug.brainmaster.ws.response;

import java.util.List;

public class WinnerResponse {

  private boolean valid;

  private List<UserResponse> japanWinners;
  private List<UserResponse> macbookWinners;
  private List<UserResponse> fujifilmWinners;
  private List<UserResponse> lenovoWinners;
  private List<UserResponse> meizuWinners;

  public WinnerResponse() {}

  public WinnerResponse(boolean valid, List<UserResponse> japanWinners,
      List<UserResponse> macbookWinners, List<UserResponse> fujifilmWinners,
      List<UserResponse> lenovoWinners, List<UserResponse> meizuWinners) {
    super();
    this.valid = valid;
    this.japanWinners = japanWinners;
    this.macbookWinners = macbookWinners;
    this.fujifilmWinners = fujifilmWinners;
    this.lenovoWinners = lenovoWinners;
    this.meizuWinners = meizuWinners;
  }

  public List<UserResponse> getFujifilmWinners() {
    return this.fujifilmWinners;
  }

  public List<UserResponse> getJapanWinners() {
    return this.japanWinners;
  }

  public List<UserResponse> getLenovoWinners() {
    return this.lenovoWinners;
  }

  public List<UserResponse> getMacbookWinners() {
    return this.macbookWinners;
  }

  public List<UserResponse> getMeizuWinners() {
    return this.meizuWinners;
  }

  public boolean isValid() {
    return this.valid;
  }

  public void setFujifilmWinners(List<UserResponse> fujifilmWinners) {
    this.fujifilmWinners = fujifilmWinners;
  }

  public void setJapanWinners(List<UserResponse> japanWinners) {
    this.japanWinners = japanWinners;
  }

  public void setLenovoWinners(List<UserResponse> lenovoWinners) {
    this.lenovoWinners = lenovoWinners;
  }

  public void setMacbookWinners(List<UserResponse> macbookWinners) {
    this.macbookWinners = macbookWinners;
  }

  public void setMeizuWinners(List<UserResponse> meizuWinners) {
    this.meizuWinners = meizuWinners;
  }

  public void setValid(boolean valid) {
    this.valid = valid;
  }


}

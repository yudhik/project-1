package org.jug.brainmaster.model.response;

import java.io.Serializable;
import java.util.List;

public class GameMessage implements Serializable {

  private static final long serialVersionUID = 6906674456702002391L;
  private String name;
  private String voucherCode;
  private GameState gameState;
  private Boolean isTheFirst;
  private List<WinnerResponse> winners;
  private Long timeLeftToClaim;

  public GameMessage() {
  }

  public GameMessage(String name, String voucherCode, GameState gameState, boolean isTheFirst,
      Long timeLeftToClaim) {
    this.name = name;
    this.voucherCode = voucherCode;
    this.gameState = gameState;
    this.isTheFirst = isTheFirst;
    this.timeLeftToClaim = timeLeftToClaim;
  }

  public GameState getGameState() {
    return gameState;
  }

  public String getName() {
    return name;
  }

  public Boolean getTheFirst() {
    return isTheFirst;
  }

  public Long getTimeLeftToClaim() {
    return timeLeftToClaim;
  }

  public String getVoucherCode() {
    return voucherCode;
  }

  public List<WinnerResponse> getWinners() {
    return winners;
  }

  public Boolean isTheFirst() {
    return isTheFirst;
  }

  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setTheFirst(Boolean theFirst) {
    isTheFirst = theFirst;
  }

  public void setTheFirst(boolean theFirst) {
    isTheFirst = theFirst;
  }

  public void setTimeLeftToClaim(Long timeLeftToClaim) {
    this.timeLeftToClaim = timeLeftToClaim;
  }

  public void setVoucherCode(String voucherCode) {
    this.voucherCode = voucherCode;
  }

  public void setWinners(List<WinnerResponse> winners) {
    this.winners = winners;
  }

  public String toJSON() {
    final StringBuilder jsonStringBuilder = new StringBuilder();
    jsonStringBuilder.append("{\"name\":\"").append(name).append("\"");
    jsonStringBuilder.append(", \"voucherCode\":\"").append(voucherCode).append("\"");
    jsonStringBuilder.append(", \"gameState\":\"").append(gameState).append("\"");
    jsonStringBuilder.append(", \"isTheFirst\":\"").append(isTheFirst).append("\"");
    jsonStringBuilder.append(", \"timeLeftToClaim\":").append(timeLeftToClaim);
    jsonStringBuilder.append(", \"winners\":[");
    if (winners != null) {
      for (WinnerResponse gameMessage : winners) {
        jsonStringBuilder.append("{");
        jsonStringBuilder.append("\"name\":\"").append(gameMessage.getName()).append("\"");
        jsonStringBuilder.append(", \"voucherCode\":\"").append(gameMessage.getVoucherCode()).append("\"");
        jsonStringBuilder.append(", \"grandPrize\":\"").append(gameMessage.isGrandPrize()).append("\"");
        jsonStringBuilder.append(", \"prizeName\":\"").append(gameMessage.getPrizeName()).append("\"");
        jsonStringBuilder.append("}");
        if (winners.indexOf(gameMessage) != winners.size() - 1) {
          jsonStringBuilder.append(",");
        }
      }
    }
    jsonStringBuilder.append("]}");
    return jsonStringBuilder.toString();
  }

  @Override
  public String toString() {
    return "GameMessage{" +
        "gameState=" + gameState +
        ", name='" + name + '\'' +
        ", voucherCode='" + voucherCode + '\'' +
        ", isTheFirst=" + isTheFirst +
        ", timeLeftToClaim=" + timeLeftToClaim +
        '}';
  }

}

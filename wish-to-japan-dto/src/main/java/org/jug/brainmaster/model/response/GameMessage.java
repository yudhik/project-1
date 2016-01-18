package org.jug.brainmaster.model.response;

import java.io.Serializable;

public class GameMessage implements Serializable {

  private static final long serialVersionUID = 6906674456702002391L;
  private String name;
  private String voucherCode;
  private GameState gameState;
  private Boolean isTheFirst;
  private int regionCounter;

  public GameMessage(String name, String voucherCode, GameState gameState, boolean isTheFirst,
      int regionCounter) {
    this.name = name;
    this.voucherCode = voucherCode;
    this.gameState = gameState;
    this.isTheFirst = isTheFirst;
    this.regionCounter = regionCounter;
  }

  public GameState getGameState() {
    return gameState;
  }

  public String getName() {
    return name;
  }

  public int getRegionCounter() {
    return regionCounter;
  }

  public String getVoucherCode() {
    return voucherCode;
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

  public void setRegionCounter(int regionCounter) {
    this.regionCounter = regionCounter;
  }

  public void setTheFirst(boolean theFirst) {
    isTheFirst = theFirst;
  }

  public void setVoucherCode(String voucherCode) {
    this.voucherCode = voucherCode;
  }

  public String toJSON() {
    return "{\"name\":\"" + name + "\",\"voucherCode\":\"" + voucherCode + "\", \"gameState\":\""
        + gameState + "\",\"isTheFirst\":\"" + isTheFirst + "\",\"regionCounter\":\""
        + regionCounter + "\"}";
  }

  @Override
  public String toString() {
    return "GameMessage [name=" + name + ", voucherCode=" + voucherCode + ", gameState=" + gameState
        + ", isTheFirst=" + isTheFirst + ", regionCounter=" + regionCounter + "]";
  }

}

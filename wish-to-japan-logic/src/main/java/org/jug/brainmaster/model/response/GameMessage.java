package org.jug.brainmaster.model.response;

import java.io.Serializable;

public class GameMessage implements Serializable {

  private static final long serialVersionUID = 6906674456702002391L;
  private String name;
  private String voucherCode;
  private GameState gameState;

  public GameMessage(String name, String voucherCode, GameState gameState) {
    this.name = name;
    this.voucherCode = voucherCode;
    this.gameState = gameState;
  }

  public GameState getGameState() {
    return gameState;
  }

  public String getName() {
    return name;
  }

  public String getVoucherCode() {
    return voucherCode;
  }

  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setVoucherCode(String voucherCode) {
    this.voucherCode = voucherCode;
  }

  public String toJSON() {
    return "{\"name\":\"" + name + "\",\"voucherCode\":\"" + voucherCode + "\", \"gameState\":\""
        + gameState + "\"}";


  }

  @Override
  public String toString() {
    return "GameMessage [name=" + name + ", voucherCode=" + voucherCode + ", gameState=" + gameState
        + "]";
  }

}

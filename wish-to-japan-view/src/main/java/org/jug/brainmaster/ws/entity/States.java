package org.jug.brainmaster.ws.entity;

public enum States {
  PROMO_ON("ON"),
  PROMO_OFF("OFF"),
  RAFFLE_RUNNING("RUNNING"),
  RAFFLE_CLAIMING("CLAIMING"),
  RAFFLE_OTHERS_RUNNING("OTHERS_RUNNING"),
  RAFFLE_OTHERS_ENDING("OTHERS_ENDING");

  private String value;

  States(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}

package org.jug.brainmaster.model.response;


import java.io.Serializable;

public class WinnerResponse implements Serializable {
  private String prizeName;
  private String voucherCode;
  private String name;
  private boolean grandPrize;

  public WinnerResponse(boolean grandPrize, String name, String prizeName, String voucherCode) {

    this.grandPrize = grandPrize;
    this.name = name;
    this.prizeName = prizeName;
    this.voucherCode = voucherCode;
  }

  public WinnerResponse() {

  }

  public String getName() {
    return name;
  }

  public String getPrizeName() {
    return prizeName;
  }

  public String getVoucherCode() {
    return voucherCode;
  }

  public boolean isGrandPrize() {
    return grandPrize;
  }

  public void setGrandPrize(boolean grandPrize) {
    this.grandPrize = grandPrize;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPrizeName(String prizeName) {
    this.prizeName = prizeName;
  }

  public void setVoucherCode(String voucherCode) {
    this.voucherCode = voucherCode;
  }
}

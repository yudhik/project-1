package org.jug.brainmaster.ws.response;

/**
 * Created by willy on 12/7/2015.
 */
public class UserResponse {
  private String name;
  private String voucherCode;

  public UserResponse() {}

  public UserResponse(String name, String voucherCode) {
    this.name = name;
    this.voucherCode = voucherCode;
  }

  public String getName() {
    return this.name;
  }

  public String getVoucherCode() {
    return this.voucherCode;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setVoucherCode(String voucherCode) {
    this.voucherCode = voucherCode;
  }
}

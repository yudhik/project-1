package org.jug.brainmaster.model.response;

public class UserResponse {
  private String name;
  private String voucherCode;

  public UserResponse() {
  }

  public String getName() {

    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVoucherCode() {
    return voucherCode;
  }

  public void setVoucherCode(String voucherCode) {
    voucherCode.substring(0, voucherCode.length() - 3);
    this.voucherCode = voucherCode + "***";
  }

  public UserResponse(String name, String voucherCode) {
    this.name = name;
    this.voucherCode = voucherCode.substring(0, voucherCode.length() - 3) + "***";
  }

  @Override
  public String toString() {
    return "UserResponse{" +
        "name='" + name + '\'' +
        ", voucherCode='" + voucherCode + '\'' +
        '}';
  }
}

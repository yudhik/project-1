package org.jug.brainmaster.model.response;


import java.io.Serializable;

public class WinnerResponse implements Serializable {

  private static final long serialVersionUID = 6365328157064999390L;
  private String prizeName;
  private String voucherCode;
  private String name;
  private boolean grandPrize;

  public WinnerResponse() {

  }

  public WinnerResponse(boolean grandPrize, String name, String prizeName, String voucherCode) {

    this.grandPrize = grandPrize;
    this.name = name;
    this.prizeName = prizeName;
    this.voucherCode = voucherCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    WinnerResponse other = (WinnerResponse) obj;
    if (grandPrize != other.grandPrize)
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (prizeName == null) {
      if (other.prizeName != null)
        return false;
    } else if (!prizeName.equals(other.prizeName))
      return false;
    if (voucherCode == null) {
      if (other.voucherCode != null)
        return false;
    } else if (!voucherCode.equals(other.voucherCode))
      return false;
    return true;
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (grandPrize ? 1231 : 1237);
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((prizeName == null) ? 0 : prizeName.hashCode());
    result = prime * result + ((voucherCode == null) ? 0 : voucherCode.hashCode());
    return result;
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

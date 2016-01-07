package org.jug.brainmaster.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "registrant",
    indexes = {@Index(name = "email_idx", columnList = Registrant.EMAIL_ADDRESS_COLUMN)})
public class Registrant implements Serializable {

  // SELECT ev.voucher_codex, eo.order_idx, eo.email_addressx, eo.first_namex, eo.last_namex,
  // eo.total_product, eo.created_date

  public static final String ID_COLUMN = "id";

  public static final String CREATED_DATE_COLUMN = "created_date";

  public static final String TOTAL_PRODUCT_COLUMN = "total_product";

  public static final String ORDER_ID_COLUMN = "order_id";

  public static final String LAST_NAME_COLUMN = "last_name";

  public static final String FIRST_NAME_COLUMN = "first_name";

  public static final String EMAIL_ADDRESS_COLUMN = "email_address";

  private static final long serialVersionUID = -1561019595650735825L;

  @Id
  @Column(name = ID_COLUMN)
  private String voucherCode;

  @Column(name = CREATED_DATE_COLUMN)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @Email
  @NotBlank
  @Column(name = EMAIL_ADDRESS_COLUMN, nullable = false)
  private String emailAddress;

  @Column(name = FIRST_NAME_COLUMN)
  private String firstName;

  @Column(name = LAST_NAME_COLUMN)
  private String lastName;

  @Column(name = ORDER_ID_COLUMN)
  private String orderId;

  @Column(name = TOTAL_PRODUCT_COLUMN)
  private Double totalProduct;

  @Version
  @Column(name = "version")
  private int version;

  public Registrant() {
    // Nothing to do here
  }

  public Registrant(String voucherCode, String firstName, String lastName, String emailAddress,
      String orderId) {
    this.voucherCode = voucherCode;
    this.firstName = firstName;
    this.lastName = lastName;
    this.emailAddress = emailAddress;
    this.orderId = orderId;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Registrant other = (Registrant) obj;
    if (voucherCode == null) {
      if (other.voucherCode != null)
        return false;
    } else if (!voucherCode.equals(other.voucherCode))
      return false;
    return true;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getOrderId() {
    return orderId;
  }

  public Double getTotalProduct() {

    return totalProduct;
  }

  public int getVersion() {
    return this.version;
  }

  public String getVoucherCode() {
    return voucherCode;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((voucherCode == null) ? 0 : voucherCode.hashCode());
    return result;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public void setTotalProduct(Double totalProduct) {
    this.totalProduct = totalProduct;
  }

  public void setVersion(final int version) {
    this.version = version;
  }

  public void setVoucherCode(String voucherCode) {
    this.voucherCode = voucherCode;
  }

  @Override
  public String toString() {
    return "Registrant [createdDate=" + createdDate + ", emailAddress=" + emailAddress
        + ", firstName=" + firstName + ", voucherCode=" + voucherCode + ", lastName=" + lastName
        + ", orderId=" + orderId + ", totalProduct=" + totalProduct + ", version=" + version + "]";
  }
}

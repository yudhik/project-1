package org.jug.brainmaster.model.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

public class ClaimRequest implements Serializable {

  private static final long serialVersionUID = -6814328241733081974L;

  @Email
  @NotNull
  private String emailAddress;

  public ClaimRequest(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

}

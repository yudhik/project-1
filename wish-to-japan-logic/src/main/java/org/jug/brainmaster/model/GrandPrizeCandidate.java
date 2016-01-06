package org.jug.brainmaster.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "grand_prize_candidates")
public class GrandPrizeCandidate implements Serializable {

  private static final long serialVersionUID = 5231362627404666741L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "registrant_id")
  private Registrant registrant;

  @Column(name = "email_address")
  private String emailAddress;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "prizelist_id")
  private PrizeList prizeList;

  @Column
  private boolean current = false;

  @Column
  private boolean claimed = false;

  public GrandPrizeCandidate() {
    // Nothing to do here
  }

  public GrandPrizeCandidate(Registrant registrant, PrizeList prizeList) {
    this.registrant = registrant;
    this.prizeList = prizeList;
    if (registrant != null && registrant.getEmailAddress() != null) {
      this.emailAddress = registrant.getEmailAddress();
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GrandPrizeCandidate other = (GrandPrizeCandidate) obj;
    if (id != other.id)
      return false;
    return true;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public Long getId() {
    return id;
  }

  public PrizeList getPrizeList() {
    return prizeList;
  }

  public Registrant getRegistrant() {
    return registrant;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (id ^ (id >>> 32));
    return result;
  }

  public boolean isClaimed() {
    return claimed;
  }

  public boolean isCurrent() {
    return current;
  }

  public void setClaimed(boolean claimed) {
    this.claimed = claimed;
  }

  public void setCurrent(boolean current) {
    this.current = current;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPrizeList(PrizeList prizeList) {
    this.prizeList = prizeList;
  }

  public void setRegistrant(Registrant registrant) {
    this.registrant = registrant;
  }

  @Override
  public String toString() {
    return "WinnerCandidate [id=" + id + ", registrant=" + registrant + ", prizeList=" + prizeList
        + ", current=" + current + ", claimed=" + claimed + "]";
  }

}

package org.jug.brainmaster.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class PrizeListCandidate {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @ManyToOne(cascade = CascadeType.MERGE)
  private Winners prizeListWinner;

  @ManyToOne(cascade = CascadeType.MERGE)
  private Registrant candidate;

  @Column
  private Integer showOrder;

  public PrizeListCandidate() {
  }

  public PrizeListCandidate(Registrant candidate, Integer showOrder,
      Winners prizeListWinner) {

    this.candidate = candidate;
    this.showOrder = showOrder;
    this.prizeListWinner = prizeListWinner;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    PrizeListCandidate that = (PrizeListCandidate) o;

    if (prizeListWinner != null ? !prizeListWinner
        .equals(that.prizeListWinner) : that.prizeListWinner != null)
      return false;
    if (candidate != null
        ? !candidate.equals(that.candidate)
            : that.candidate != null)
      return false;
    return !(showOrder != null
        ? !showOrder.equals(that.showOrder)
            : that.showOrder != null);

  }

  public Registrant getCandidate() {

    return candidate;
  }

  public Long getId() {
    return id;
  }

  public Winners getPrizeListWinner() {
    return prizeListWinner;
  }

  public Integer getShowOrder() {
    return showOrder;
  }

  @Override
  public int hashCode() {
    int result = prizeListWinner != null ? prizeListWinner.hashCode() : 0;
    result = 31 * result + (candidate != null ? candidate.hashCode() : 0);
    result = 31 * result + (showOrder != null ? showOrder.hashCode() : 0);
    return result;
  }

  public void setCandidate(Registrant candidate) {
    this.candidate = candidate;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPrizeListWinner(Winners prizeListWinner) {
    this.prizeListWinner = prizeListWinner;
  }

  public void setShowOrder(Integer showOrder) {
    this.showOrder = showOrder;
  }

  @Override
  public String toString() {
    return candidate + " | " + showOrder + " | " + prizeListWinner.toString();
  }
}

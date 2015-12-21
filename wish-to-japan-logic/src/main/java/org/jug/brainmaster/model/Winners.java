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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "winners_list", uniqueConstraints = {@UniqueConstraint(
    columnNames = {Winners.COLUMN_PRIZE_ID, Winners.COLUMN_REGISTRANT_ID})})
public class Winners implements Serializable {

  private static final long serialVersionUID = -280867261110554136L;

  public static final String COLUMN_PRIZE_ID = "prize_id";
  public static final String COLUMN_REGISTRANT_ID = "registrant_id";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "prize_id")
  private PrizeList prize;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "registrant_id")
  private Registrant registrant;

  public Winners() {
    // Nothing to do here
  }

  public Winners(PrizeList prize, Registrant registrant) {
    this.prize = prize;
    this.registrant = registrant;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Winners other = (Winners) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  public Long getId() {
    return id;
  }

  public PrizeList getPrize() {
    return prize;
  }

  public Registrant getRegistrant() {
    return registrant;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPrize(PrizeList prize) {
    this.prize = prize;
  }

  public void setRegistrant(Registrant registrant) {
    this.registrant = registrant;
  }

}

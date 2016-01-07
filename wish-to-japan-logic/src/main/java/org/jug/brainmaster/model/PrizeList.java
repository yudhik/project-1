package org.jug.brainmaster.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "prize_list")
public class PrizeList implements Serializable {
  public static final String IS_GRAND_PRIZE_COLUMN = "is_grand_prize";

  private static final long serialVersionUID = -280867261110554136L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Long id;

  @Column(unique = true)
  private String name;

  @Column(name = IS_GRAND_PRIZE_COLUMN, nullable = false)
  private Boolean grandPrize = true;

  public PrizeList() {}

  public PrizeList(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PrizeList other = (PrizeList) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  public Boolean getGrandPrize() {
    return grandPrize;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  public void setGrandPrize(Boolean grandPrize) {
    this.grandPrize = grandPrize;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "PrizeList [id=" + id + ", name=" + name + ", grand prize=" + grandPrize + "]";
  }
}

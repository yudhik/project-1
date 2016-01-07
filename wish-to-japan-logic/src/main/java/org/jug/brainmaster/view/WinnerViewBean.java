package org.jug.brainmaster.view;

import org.jug.brainmaster.ejb.PrizeListServiceBean;
import org.jug.brainmaster.ejb.WinnerServiceBean;
import org.jug.brainmaster.model.PrizeList;
import org.jug.brainmaster.model.Registrant;
import org.jug.brainmaster.model.Winners;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean
@RequestScoped
public class WinnerViewBean implements Serializable {

  private static final long serialVersionUID = 1656516951748383395L;

  @Inject
  private WinnerServiceBean winnerServiceBean;

  @Inject
  private PrizeListServiceBean prizeListServiceBean;

  @Inject
  private Logger log;
  private Long id;
  private Winners winner;
  private Registrant registrant;
  private Integer winnerSize;
  private PrizeList prizeList;

  public String create() {
    return "create?faces-redirect=true";
  }

  public String delete() {
    try {
      winnerServiceBean.delete(winner);
      return "search?faces-redirect=true";
    } catch (Exception e) {
      log.log(Level.SEVERE, "can not save winner : " + winner, e);
    }
    return null;
  }

  public String generate() {
    return "generate?faces-redirect=true";
  }

  public String generateRandomWinner() {
    try {
      winnerServiceBean.generateRandomWinner(prizeList, winnerSize);
      return "search?faces-redirect=true";
    } catch (RuntimeException e) {
      log.log(Level.SEVERE, "could not generate winners", e);
    }
    return null;
  }

  public List<PrizeList> getAllPrize() {
    return prizeListServiceBean.findAllNonGrandPrize();
  }

  public PrizeList getPrizeList() {
    return prizeList;
  }

  public Converter getPrizeListConverter() {
    return new Converter() {

      @Override
      public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return prizeListServiceBean.findByName(value);
      }

      @Override
      public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
          return "";
        }
        return ((PrizeList) value).getName();
      }
    };
  }

  public Registrant getRegistrant() {
    return registrant;
  }

  public Winners getWinner() {
    return winner;
  }

  public Integer getWinnerSize() {
    return winnerSize;
  }

  public List<Winners> getWinners() {
    return winnerServiceBean.getAllWinners();
  }

  public String saveOrUpdate() {
    try {
      if (winner == null) {
        winner = new Winners();
      }
      if (id != null) {
        winner = winnerServiceBean.findById(id);
      }
      winner.setPrize(prizeList);
      winner.setRegistrant(registrant);
      winnerServiceBean.saveOrUpdate(winner);
      return "search?faces-redirect=true";
    } catch (Exception e) {
      log.log(Level.SEVERE, "can not save candidate : " + winner, e);
    }
    return null;
  }

  public void setPrizeList(PrizeList prizeList) {
    this.prizeList = prizeList;
  }

  public void setRegistrant(Registrant registrant) {
    this.registrant = registrant;
  }

  public void setWinner(Winners winner) {
    this.winner = winner;
  }

  public void setWinnerSize(Integer winnerSize) {
    this.winnerSize = winnerSize;
  }
}

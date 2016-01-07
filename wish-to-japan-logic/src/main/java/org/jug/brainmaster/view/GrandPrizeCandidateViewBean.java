package org.jug.brainmaster.view;

import org.jug.brainmaster.ejb.GrandPrizeCandidateServiceBean;
import org.jug.brainmaster.ejb.PrizeListServiceBean;
import org.jug.brainmaster.ejb.RegistrantServiceBean;
import org.jug.brainmaster.model.GrandPrizeCandidate;
import org.jug.brainmaster.model.PrizeList;
import org.jug.brainmaster.model.Registrant;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean
@RequestScoped
public class GrandPrizeCandidateViewBean implements Serializable {

  private static final long serialVersionUID = 6639274139742549413L;

  @Inject
  private GrandPrizeCandidateServiceBean grandPrizeCandidateService;

  @Inject
  private RegistrantServiceBean registrantServiceBean;

  @Inject
  private PrizeListServiceBean prizeListServiceBean;

  @Inject
  private Logger log;

  private GrandPrizeCandidate candidate;
  private Registrant registrant;
  private PrizeList prizeList;

  private Long id;

  private int rowSize = 25;

  private int pageNumber = 1;

  public String create() {
    return "create?faces-redirect=true";
  }

  public String delete() {
    try {
      grandPrizeCandidateService.delete(candidate);
      return "search?faces-redirect=true";
    } catch (Exception e) {
      log.log(Level.SEVERE, "can not save candidate : " + candidate, e);
    }
    return null;
  }

  public List<PrizeList> getAllPrize() {
    return prizeListServiceBean.findAll();
  }

  public GrandPrizeCandidate getCandidate() {
    return candidate;
  }

  public List<GrandPrizeCandidate> getCandidates() {
    List<GrandPrizeCandidate> viewCandidates = new ArrayList<GrandPrizeCandidate>();
    grandPrizeCandidateService.getAllCandidateWithPagination(rowSize, pageNumber)
        .forEach(viewCandidate -> {
          viewCandidates.add(grandPrizeCandidateService.findById(viewCandidate.getId()));
        });
    return viewCandidates;
  }

  public Long getId() {
    return id;
  }

  public int getPageNumber() {
    return pageNumber;
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

  public Converter getRegistrantConverter() {
    return new Converter() {

      @Override
      public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return registrantServiceBean.findById(value);
      }

      @Override
      public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
          return "";
        }
        return ((Registrant) value).getEmailAddress();
      }

    };
  }

  public int getRowSize() {
    return rowSize;
  }

  public void retrieve() {
    if (id != null) {
      log.log(Level.INFO, "get existing candidate");
      candidate = grandPrizeCandidateService.findById(id);
    } else {
      log.log(Level.INFO, "create new candidate");
      candidate = new GrandPrizeCandidate();
      registrant = new Registrant();
      prizeList = new PrizeList();
    }
  }

  public String saveOrUpdate() {
    try {
      if (candidate == null) {
        candidate = new GrandPrizeCandidate();
      }
      if (id != null) {
        candidate = grandPrizeCandidateService.findById(id);
      }
      candidate.setPrizeList(prizeList);
      candidate.setEmailAddress(registrant.getEmailAddress());
      candidate.setRegistrant(registrant);
      grandPrizeCandidateService.saveOrUpdate(candidate);
      return "search?faces-redirect=true";
    } catch (Exception e) {
      log.log(Level.SEVERE, "can not save candidate : " + candidate, e);
    }
    return null;
  }

  public void setCandidate(GrandPrizeCandidate candidate) {
    this.candidate = candidate;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  public void setPrizeList(PrizeList prizeList) {
    this.prizeList = prizeList;
  }

  public void setRegistrant(Registrant registrant) {
    this.registrant = registrant;
  }

  public void setRowSize(int rowSize) {
    this.rowSize = rowSize;
  }

}

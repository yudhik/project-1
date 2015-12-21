package org.jug.brainmaster.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ConversationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;

import org.jug.brainmaster.ejb.GrandPrizeCandidateServiceBean;
import org.jug.brainmaster.ejb.PrizeListServiceBean;
import org.jug.brainmaster.ejb.RegistrantServiceBean;
import org.jug.brainmaster.model.GrandPrizeCandidate;
import org.jug.brainmaster.model.PrizeList;
import org.jug.brainmaster.model.Registrant;

@ManagedBean
@ConversationScoped
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

  private Long id;
  private int rowSize = 25;
  private int pageNumber = 1;

  public String delete() {
    try {
      grandPrizeCandidateService.delete(candidate);
      return "search?faces-redirect=true";
    } catch (Exception e) {
      log.log(Level.SEVERE, "can not save candidate : "+ candidate, e);
    }
    return null;
  }

  public GrandPrizeCandidate getCandidate() {
    return candidate;
  }

  public List<GrandPrizeCandidate> getCandidates() {
    List<GrandPrizeCandidate> viewCandidates = new ArrayList<GrandPrizeCandidate>();
    grandPrizeCandidateService.getAllCandidateWithPagination(rowSize, pageNumber).parallelStream().forEach(viewCandidate ->  {
      viewCandidates.add(grandPrizeCandidateService.findById(viewCandidate.getId()));
    });
    return viewCandidates;
  }

  public long getId() {
    return id;
  }

  public int getPageNumber() {
    return pageNumber;
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

  public Converter getRegistrantConverter() {
    return new Converter() {

      @Override
      public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return registrantServiceBean.findByEmailAddress(value);
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

  public GrandPrizeCandidate retrieve() {
    if(id != null) {
      return grandPrizeCandidateService.findById(id);
    } else {
      return new GrandPrizeCandidate();
    }
  }

  public String saveOrUpdate() {
    try {
      grandPrizeCandidateService.saveOrUpdate(candidate);
      return "search?faces-redirect=true";
    } catch (Exception e) {
      log.log(Level.SEVERE, "can not save candidate : "+ candidate, e);
    }
    return null;
  }

  public void setCandidate(GrandPrizeCandidate candidate) {
    this.candidate = candidate;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  public void setRowSize(int rowSize) {
    this.rowSize = rowSize;
  }

}

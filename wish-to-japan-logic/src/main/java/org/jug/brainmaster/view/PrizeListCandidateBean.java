package org.jug.brainmaster.view;

import org.jug.brainmaster.model.PrizeListCandidate;
import org.jug.brainmaster.model.Registrant;
import org.jug.brainmaster.model.Winners;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Backing bean for PrizeListCandidate entities.
 * <p/>
 * This class provides CRUD functionality for all PrizeListCandidate entities. It focuses purely on
 * Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for searches) rather than
 * introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PrizeListCandidateBean implements Serializable {

  private static final long serialVersionUID = 1L;

  /*
   * Support creating and retrieving PrizeListCandidate entities
   */

  private Long id;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  private PrizeListCandidate prizeListCandidate;

  public PrizeListCandidate getPrizeListCandidate() {
    return this.prizeListCandidate;
  }

  public void setPrizeListCandidate(PrizeListCandidate prizeListCandidate) {
    this.prizeListCandidate = prizeListCandidate;
  }

  @Inject
  private Conversation conversation;

  @PersistenceContext(unitName = "wish-to-paris-persistence-unit",
      type = PersistenceContextType.EXTENDED)
  private EntityManager entityManager;

  public String create() {

    this.conversation.begin();
    this.conversation.setTimeout(1800000L);
    return "create?faces-redirect=true";
  }

  public void retrieve() {

    if (FacesContext.getCurrentInstance().isPostback()) {
      return;
    }

    if (this.conversation.isTransient()) {
      this.conversation.begin();
      this.conversation.setTimeout(1800000L);
    }

    if (this.id == null) {
      this.prizeListCandidate = this.example;
    } else {
      this.prizeListCandidate = findById(getId());
    }
  }

  public PrizeListCandidate findById(Long id) {

    return this.entityManager.find(PrizeListCandidate.class, id);
  }

  /*
   * Support updating and deleting PrizeListCandidate entities
   */

  public String update() {
    this.conversation.end();

    try {
      if (this.id == null) {
        this.entityManager.persist(this.prizeListCandidate);
        return "search?faces-redirect=true";
      } else {
        this.entityManager.persist(this.prizeListCandidate);
        return "view?faces-redirect=true&id=" + this.prizeListCandidate.getId();
      }
    } catch (Exception e) {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
      return null;
    }
  }

  public String delete() {
    this.conversation.end();

    try {
      PrizeListCandidate deletableEntity = findById(getId());

      this.entityManager.remove(deletableEntity);
      this.entityManager.flush();
      return "search?faces-redirect=true";
    } catch (Exception e) {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
      return null;
    }
  }

  /*
   * Support searching PrizeListCandidate entities with pagination
   */

  private int page;
  private long count;
  private List<PrizeListCandidate> pageItems;

  private PrizeListCandidate example = new PrizeListCandidate();

  public int getPage() {
    return this.page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getPageSize() {
    return 10;
  }

  public PrizeListCandidate getExample() {
    return this.example;
  }

  public void setExample(PrizeListCandidate example) {
    this.example = example;
  }

  public String search() {
    this.page = 0;
    return null;
  }

  public void paginate() {

    CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

    // Populate this.count

    CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
    Root<PrizeListCandidate> root = countCriteria.from(PrizeListCandidate.class);
    countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
    this.count = this.entityManager.createQuery(countCriteria).getSingleResult();

    // Populate this.pageItems

    CriteriaQuery<PrizeListCandidate> criteria = builder.createQuery(PrizeListCandidate.class);
    root = criteria.from(PrizeListCandidate.class);
    TypedQuery<PrizeListCandidate> query =
        this.entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
    query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
    this.pageItems = query.getResultList();
  }

  private Predicate[] getSearchPredicates(Root<PrizeListCandidate> root) {

    CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
    List<Predicate> predicatesList = new ArrayList<Predicate>();

    Registrant candidate = this.example.getCandidate();
    if (candidate != null) {
      predicatesList.add(builder.equal(root.get("candidate"), candidate));
    }
    Integer showOrder = this.example.getShowOrder();
    if (showOrder != null && showOrder.intValue() != 0) {
      predicatesList.add(builder.equal(root.get("showOrder"), showOrder));
    }
    Winners prizeListWinner = this.example.getPrizeListWinner();
    if (prizeListWinner != null) {
      predicatesList.add(builder.equal(root.get("prizeListWinner"), prizeListWinner));
    }

    return predicatesList.toArray(new Predicate[predicatesList.size()]);
  }

  public List<PrizeListCandidate> getPageItems() {
    return this.pageItems;
  }

  public long getCount() {
    return this.count;
  }

  /*
   * Support listing and POSTing back PrizeListCandidate entities (e.g. from inside an
   * HtmlSelectOneMenu)
   */

  public List<PrizeListCandidate> getAll() {

    CriteriaQuery<PrizeListCandidate> criteria =
        this.entityManager.getCriteriaBuilder().createQuery(PrizeListCandidate.class);
    return this.entityManager.createQuery(criteria.select(criteria.from(PrizeListCandidate.class)))
        .getResultList();
  }

  @Resource
  private SessionContext sessionContext;

  public Converter getConverter() {

    final PrizeListCandidateBean ejbProxy =
        this.sessionContext.getBusinessObject(PrizeListCandidateBean.class);

    return new Converter() {

      @Override
      public Object getAsObject(FacesContext context, UIComponent component, String value) {

        return ejbProxy.findById(Long.valueOf(value));
      }

      @Override
      public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (value == null) {
          return "";
        }

        return String.valueOf(((PrizeListCandidate) value).getId());
      }
    };
  }

  /*
   * Support adding children to bidirectional, one-to-many tables
   */

  private PrizeListCandidate add = new PrizeListCandidate();

  public PrizeListCandidate getAdd() {
    return this.add;
  }

  public PrizeListCandidate getAdded() {
    PrizeListCandidate added = this.add;
    this.add = new PrizeListCandidate();
    return added;
  }
}

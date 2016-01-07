package org.jug.brainmaster.view;

import org.jug.brainmaster.model.PrizeList;

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
 * Backing bean for PrizeList entities.
 * <p>
 * This class provides CRUD functionality for all PrizeList entities. It focuses purely on Java EE 6
 * standards (e.g. <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for searches) rather than
 * introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PrizeListBean implements Serializable {

  private static final long serialVersionUID = 1L;

  /*
   * Support creating and retrieving PrizeList entities
   */

  private Long id;

  private PrizeList prizeList;

  @Inject
  private Conversation conversation;

  @Inject
  private PrizeListWinnerBean prizeListWinnerBean;

  private boolean grandPrizeOptions[] = {true, false};

  @PersistenceContext(unitName = "wish-to-paris-persistence-unit",
      type = PersistenceContextType.EXTENDED)
  private EntityManager entityManager;

  private int page;

  private long count;

  private List<PrizeList> pageItems;

  private PrizeList example = new PrizeList();

  @Resource
  private SessionContext sessionContext;

  private PrizeList add = new PrizeList();

  public String create() {

    this.conversation.begin();
    this.conversation.setTimeout(1800000L);
    return "create?faces-redirect=true";
  }

  public String delete() {
    this.conversation.end();

    try {
      PrizeList deletableEntity = findById(getId());

      this.entityManager.remove(deletableEntity);
      this.entityManager.flush();
      return "search?faces-redirect=true";
    } catch (Exception e) {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
      return null;
    }
  }

  public PrizeList findById(Long id) {

    return this.entityManager.find(PrizeList.class, id);
  }

  /*
   * Support updating and deleting PrizeList entities
   */

  public void generateWinnerListFromPrizeList() {
    this.conversation.end();
    // TODO: 12/7/2015
  }

  public PrizeList getAdd() {
    return this.add;
  }

  /*
   * Support searching PrizeList entities with pagination
   */

  public PrizeList getAdded() {
    PrizeList added = this.add;
    this.add = new PrizeList();
    return added;
  }

  public List<PrizeList> getAll() {

    CriteriaQuery<PrizeList> criteria =
        this.entityManager.getCriteriaBuilder().createQuery(PrizeList.class);
    return this.entityManager.createQuery(criteria.select(criteria.from(PrizeList.class)))
        .getResultList();
  }

  public Converter getConverter() {

    final PrizeListBean ejbProxy = this.sessionContext.getBusinessObject(PrizeListBean.class);

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

        return String.valueOf(((PrizeList) value).getId());
      }
    };
  }

  public long getCount() {
    return this.count;
  }

  public PrizeList getExample() {
    return this.example;
  }

  public boolean[] getGrandPrizeOptions() {
    return grandPrizeOptions;
  }

  public Long getId() {
    return this.id;
  }

  public int getPage() {
    return this.page;
  }

  public List<PrizeList> getPageItems() {
    return this.pageItems;
  }

  public int getPageSize() {
    return 10;
  }

  public PrizeList getPrizeList() {
    return this.prizeList;
  }

  private Predicate[] getSearchPredicates(Root<PrizeList> root) {

    CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
    List<Predicate> predicatesList = new ArrayList<Predicate>();

    String name = this.example.getName();
    if (name != null && !"".equals(name)) {
      predicatesList.add(
          builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
    }

    return predicatesList.toArray(new Predicate[predicatesList.size()]);
  }

  public void paginate() {

    CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

    // Populate this.count

    CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
    Root<PrizeList> root = countCriteria.from(PrizeList.class);
    countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
    this.count = this.entityManager.createQuery(countCriteria).getSingleResult();

    // Populate this.pageItems

    CriteriaQuery<PrizeList> criteria = builder.createQuery(PrizeList.class);
    root = criteria.from(PrizeList.class);
    TypedQuery<PrizeList> query =
        this.entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
    query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
    this.pageItems = query.getResultList();
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
      this.prizeList = this.example;
    } else {
      this.prizeList = findById(getId());
    }
  }

  /*
   * Support listing and POSTing back PrizeList entities (e.g. from inside an HtmlSelectOneMenu)
   */

  public String search() {
    this.page = 0;
    return null;
  }

  public void setExample(PrizeList example) {
    this.example = example;
  }

  public void setGrandPrizeOptions(boolean grandPrizeOptions[]) {
    this.grandPrizeOptions = grandPrizeOptions;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /*
   * Support adding children to bidirectional, one-to-many tables
   */

  public void setPage(int page) {
    this.page = page;
  }

  public void setPrizeList(PrizeList prizeList) {
    this.prizeList = prizeList;
  }

  public String update() {
    this.conversation.end();

    try {
      if (this.id == null) {
        this.entityManager.persist(this.prizeList);
        return "search?faces-redirect=true";
      } else {
        this.entityManager.persist(this.prizeList);
        return "view?faces-redirect=true&id=" + this.prizeList.getId();
      }
    } catch (Exception e) {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
      return null;
    }
  }
}

package org.jug.brainmaster.view;

import org.jug.brainmaster.ejb.WinnerServiceBean;
import org.jug.brainmaster.model.PrizeList;
import org.jug.brainmaster.model.Registrant;
import org.jug.brainmaster.model.Winners;

import javax.annotation.Resource;
import javax.ejb.EJB;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Backing bean for PrizeListWinner entities.
 * <p>
 * This class provides CRUD functionality for all PrizeListWinner entities. It focuses purely on
 * Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for searches) rather than
 * introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PrizeListWinnerBean implements Serializable {

  private static final long serialVersionUID = 1L;

  /*
   * Support creating and retrieving PrizeListWinner entities
   */

  private Long id;

  private Winners prizeListWinner;
  private PrizeList prizeList;
  private Registrant registrant;

  @Inject
  private Conversation conversation;

  @PersistenceContext(unitName = "wish-to-paris-persistence-unit",
      type = PersistenceContextType.EXTENDED)
  private EntityManager entityManager;

  private int page;

  private long count;

  private List<Winners> pageItems;

  private Winners example = new Winners();

  @Resource
  private SessionContext sessionContext;

  @EJB
  private WinnerServiceBean winnerServiceBean;

  @Inject
  private Logger log;


  private Winners add = new Winners();

  public String create() {

    this.conversation.begin();
    this.conversation.setTimeout(1800000L);
    return "create?faces-redirect=true";
  }

  /*
   * Support updating and deleting PrizeListWinner entities
   */

  public String delete() {
    this.conversation.end();

    try {
      Winners deletableEntity = findById(getId());

      this.entityManager.remove(deletableEntity);
      this.entityManager.flush();
      return "search?faces-redirect=true";
    } catch (Exception e) {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
      return null;
    }
  }

  public Winners findById(Long id) {

    return this.entityManager.find(Winners.class, id);
  }

  /*
   * Support searching PrizeListWinner entities with pagination
   */

  public Winners getAdd() {
    return this.add;
  }

  public Winners getAdded() {
    Winners added = this.add;
    this.add = new Winners();
    return added;
  }

  public List<Winners> getAll() {

    CriteriaQuery<Winners> criteria =
        this.entityManager.getCriteriaBuilder().createQuery(Winners.class);
    return this.entityManager.createQuery(criteria.select(criteria.from(Winners.class)))
        .getResultList();
  }

  public Converter getConverter() {

    final PrizeListWinnerBean ejbProxy =
        this.sessionContext.getBusinessObject(PrizeListWinnerBean.class);

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

        return String.valueOf(((Winners) value).getId());
      }
    };
  }

  public long getCount() {
    return this.count;
  }

  public Winners getExample() {
    return this.example;
  }

  public Long getId() {
    return this.id;
  }

  public int getPage() {
    return this.page;
  }

  public List<Winners> getPageItems() {
    return this.pageItems;
  }

  public int getPageSize() {
    return 10;
  }

  public Winners getPrizeListWinner() {
    return this.prizeListWinner;
  }

  private Predicate[] getSearchPredicates(Root<Winners> root) {

    CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
    List<Predicate> predicatesList = new ArrayList<Predicate>();

    PrizeList prize = this.example.getPrize();
    if (prize != null) {
      predicatesList.add(builder.equal(root.get("prize"), prize));
    }

    return predicatesList.toArray(new Predicate[predicatesList.size()]);
  }

  public void paginate() {

    CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

    // Populate this.count

    CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
    Root<Winners> root = countCriteria.from(Winners.class);
    countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
    this.count = this.entityManager.createQuery(countCriteria).getSingleResult();

    // Populate this.pageItems

    CriteriaQuery<Winners> criteria = builder.createQuery(Winners.class);
    root = criteria.from(Winners.class);
    TypedQuery<Winners> query =
        this.entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
    query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
    this.pageItems = query.getResultList();
  }

  public void retrieve() {

    if (id != null) {
      log.log(Level.FINER, "get existing candidate");
      prizeListWinner = winnerServiceBean.findById(id);
    } else {
      log.log(Level.FINER, "create new candidate");
      prizeListWinner = new Winners();
      registrant = new Registrant();
      prizeList = new PrizeList();
    }
  }

  /*
   * Support listing and POSTing back PrizeListWinner entities (e.g. from inside an
   * HtmlSelectOneMenu)
   */

  public String search() {
    this.page = 0;
    return null;
  }

  public void setExample(Winners example) {
    this.example = example;
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

  public void setPrizeListWinner(Winners prizeListWinner) {
    this.prizeListWinner = prizeListWinner;
  }

  public void setWinnersRandomlyBySpecificPrizeList() {

  }


  public String update() {
    this.conversation.end();

    try {
      if (this.id == null) {
        this.entityManager.persist(this.prizeListWinner);
        return "search?faces-redirect=true";
      } else {
        this.entityManager.persist(this.prizeListWinner);
        return "view?faces-redirect=true&id=" + this.prizeListWinner.getId();
      }
    } catch (Exception e) {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
      return null;
    }
  }
}

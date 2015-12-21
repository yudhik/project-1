package org.jug.brainmaster.view;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
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
import javax.servlet.http.Part;
import javax.transaction.Transactional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jug.brainmaster.model.Registrant;

/**
 * Backing bean for Registrant entities.
 * <p>
 * This class provides CRUD functionality for all Registrant entities. It focuses purely on Java EE
 * 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for searches) rather than
 * introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class RegistrantBean implements Serializable {

  private static final long serialVersionUID = 1L;
  private final static Logger LOGGER = Logger.getLogger(RegistrantBean.class.getName());


  private Registrant add = new Registrant();

  @Inject
  private Conversation conversation;

  private long count;

  @PersistenceContext(unitName = "wish-to-paris-persistence-unit",
      type = PersistenceContextType.EXTENDED)
  private EntityManager entityManager;

  private Registrant example = new Registrant();

  private String voucherCode;

  private int page;

  private List<Registrant> pageItems;

  private Registrant registrant;

  @Resource
  private SessionContext sessionContext;

  private Part file;

  public String create() {
    this.conversation.begin();
    this.conversation.setTimeout(1800000L);
    return "create?faces-redirect=true";
  }

  /*
   * Support updating and deleting Registrant entities
   */
  public String delete() {
    this.conversation.end();
    try {
      Registrant deletableEntity = findById(getVoucherCode());
      this.entityManager.remove(deletableEntity);
      this.entityManager.flush();
      return "search?faces-redirect=true";
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "something wrong with delete", e);
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
      return null;
    }
  }

  public Registrant findById(String id) {
    return this.entityManager.find(Registrant.class, id);
  }

  /*
   * Support searching Registrant entities with pagination
   */

  public Registrant getAdd() {
    return this.add;
  }

  public Registrant getAdded() {
    Registrant added = this.add;
    this.add = new Registrant();
    return added;
  }

  public List<Registrant> getAll() {
    CriteriaQuery<Registrant> criteria =
        this.entityManager.getCriteriaBuilder().createQuery(Registrant.class);
    return this.entityManager.createQuery(criteria.select(criteria.from(Registrant.class)))
        .getResultList();
  }

  public Converter getConverter() {
    final RegistrantBean ejbProxy = this.sessionContext.getBusinessObject(RegistrantBean.class);
    return new Converter() {
      @Override
      public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return ejbProxy.findById(String.valueOf(value));
      }

      @Override
      public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
          return "";
        }
        return String.valueOf(((Registrant) value).getEmailAddress());
      }
    };
  }

  public long getCount() {
    return this.count;
  }

  public Registrant getExample() {
    return this.example;
  }

  public Part getFile() {
    return file;
  }

  public int getPage() {
    return this.page;
  }

  public List<Registrant> getPageItems() {
    return this.pageItems;
  }

  public int getPageSize() {
    return 10;
  }

  public List<Registrant> getRandom() {
    CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

    CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
    Root<Registrant> root = countCriteria.from(Registrant.class);
    countCriteria = countCriteria.select(builder.count(root));
    Long totalRecord = this.entityManager.createQuery(countCriteria).getSingleResult();
    //get count
    Double startIndex = 0.0;
    if(totalRecord > 2000) {
      startIndex = Math.floor(Math.random() * (totalRecord-2000));
    } else {
      startIndex = 0.0;
    }

    CriteriaQuery<Registrant> criteria = builder.createQuery(Registrant.class);
    TypedQuery<Registrant> query =
        this.entityManager.createQuery(criteria.select(root)).setFirstResult(startIndex.intValue()).setMaxResults(2000);
    return query.getResultList();
  }

  public Registrant getRegistrant() {
    return this.registrant;
  }

  private Predicate[] getSearchPredicates(Root<Registrant> root) {
    CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
    List<Predicate> predicatesList = new ArrayList<Predicate>();

    String emailAddress = this.example.getEmailAddress();
    if (emailAddress != null && !"".equals(emailAddress)) {
      predicatesList.add(builder.like(builder.lower(root.<String>get("emailAddress")),
          '%' + emailAddress.toLowerCase() + '%'));
    }
    String firstName = this.example.getFirstName();
    if (firstName != null && !"".equals(firstName)) {
      predicatesList.add(builder
          .like(builder.lower(root.<String>get("firstName")), '%' + firstName.toLowerCase() + '%'));
    }
    String voucherCode = this.example.getVoucherCode();
    if (voucherCode != null && !"".equals(voucherCode)) {
      predicatesList.add(builder.like(builder.lower(root.<String>get("voucherCode")),
          '%' + voucherCode.toLowerCase() + '%'));
    }
    String lastName = this.example.getLastName();
    if (lastName != null && !"".equals(lastName)) {
      predicatesList.add(builder
          .like(builder.lower(root.<String>get("lastName")), '%' + lastName.toLowerCase() + '%'));
    }
    String orderId = this.example.getOrderId();
    if (orderId != null && !"".equals(orderId)) {
      predicatesList.add(builder
          .like(builder.lower(root.<String>get("orderId")), '%' + orderId.toLowerCase() + '%'));
    }
    Double totalProduct = this.example.getTotalProduct();
    if (totalProduct != null) {
      predicatesList.add(builder.equal(root.get("totalProduct"), totalProduct));
    }
    return predicatesList.toArray(new Predicate[predicatesList.size()]);
  }

  public String getVoucherCode() {
    return this.voucherCode;
  }

  public void paginate() {
    CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
    // Populate this.count
    CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
    Root<Registrant> root = countCriteria.from(Registrant.class);
    countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
    this.count = this.entityManager.createQuery(countCriteria).getSingleResult();
    // Populate this.pageItems
    CriteriaQuery<Registrant> criteria = builder.createQuery(Registrant.class);
    root = criteria.from(Registrant.class);
    TypedQuery<Registrant> query =
        this.entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
    query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
    this.pageItems = query.getResultList();
  }

  /*
   * Support listing and POSTing back Registrant entities (e.g. from inside an HtmlSelectOneMenu)
   */

  @Asynchronous
  @Transactional
  public void parseAndSaveRegistrant(InputStream inputStream) {
    try {
      //SELECT ev.voucher_codex, eo.order_idx, eo.email_addressx, eo.first_namex, eo.last_namex, eo.total_product, eo.time_placed

      final Reader reader = new InputStreamReader(inputStream);
      final Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader(
          new String[] {"voucher_code", "order_id", "email_address", "first_name", "last_name",
              "total_product", "time_placed", "play_session"}).withSkipHeaderRecord()
          .withDelimiter(';').parse(reader);
      for (final CSVRecord csvRecord : records) {
        if (findById(csvRecord.get("voucher_code")) == null) {
          Registrant registrant = new Registrant();
          registrant.setVoucherCode(csvRecord.get("voucher_code"));
          registrant.setEmailAddress(csvRecord.get("email_address"));
          registrant.setFirstName(csvRecord.get("first_name"));
          registrant.setLastName(csvRecord.get("last_name"));
          registrant.setOrderId(csvRecord.get("order_id"));
          registrant.setTotalProduct(Double.valueOf(csvRecord.get("total_product")));
          registrant.setCreatedDate(new Date());
          entityManager.persist(registrant);
        }
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(),e);
      throw new RuntimeException(e);
    }
  }

  public void retrieve() {
    if (FacesContext.getCurrentInstance().isPostback()) {
      return;
    }
    if (this.conversation.isTransient()) {
      this.conversation.begin();
      this.conversation.setTimeout(1800000L);
    }
    if (this.voucherCode == null) {
      this.registrant = this.example;
    } else {
      this.registrant = findById(getVoucherCode());
    }
  }

  public String search() {
    this.page = 0;
    return null;
  }

  public void setExample(Registrant example) {
    this.example = example;
  }

  public void setFile(Part file) {
    this.file = file;
  }

  /*
   * Support adding children to bidirectional, one-to-many tables
   */
  public void setPage(int page) {
    this.page = page;
  }

  public void setRegistrant(Registrant registrant) {
    this.registrant = registrant;
  }

  public void setVoucherCode(String voucherCode) {
    this.voucherCode = voucherCode;
  }

  public String update() {
    this.conversation.end();
    try {
      if (this.voucherCode == null) {
        this.entityManager.persist(this.registrant);
        return "search?faces-redirect=true";
      } else {
        this.entityManager.persist(this.registrant);
        return "view?faces-redirect=true&id=" + this.registrant.getVoucherCode();
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "something wrong with update", e);
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
      return null;
    }
  }

  public String upload() {
    try {
      parseAndSaveRegistrant(file.getInputStream());
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "something wrong with upload", e);
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
      return null;
    }
    return "search?faces-redirect=true";
  }

  public String uploadFile() {
    return "upload?faces-redirect=true";
  }
}

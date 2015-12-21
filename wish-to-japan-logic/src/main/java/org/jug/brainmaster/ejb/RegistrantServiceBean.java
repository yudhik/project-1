package org.jug.brainmaster.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jug.brainmaster.model.Registrant;

@Stateless
@LocalBean
public class RegistrantServiceBean {

  private static final String SELECT_FROM_REGISTRANT_EMAIL_ADDRESS = "from Registrant registrant where registrant.emailAddress = :emailAddress";

  @PersistenceContext
  private EntityManager em;

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public Registrant findByEmailAddress(String emailAddress) {
    return em.createQuery(SELECT_FROM_REGISTRANT_EMAIL_ADDRESS, Registrant.class).setParameter("emailAddress", emailAddress).getResultList().get(0);
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public Registrant findById(String voucherCode) {
    return em.find(Registrant.class, voucherCode);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void saveOrUpdate(Registrant registrant) {
    if(registrant != null) {
      Registrant existingRegistrant = null;
      if (registrant.getVoucherCode() != null) {
        existingRegistrant = findById(registrant.getVoucherCode());
      } else {
        existingRegistrant = findByEmailAddress(registrant.getEmailAddress());
      }
      if(existingRegistrant != null) {
        existingRegistrant.setEmailAddress(registrant.getEmailAddress());
        existingRegistrant.setFirstName(registrant.getFirstName());
        existingRegistrant.setLastName(registrant.getLastName());
        existingRegistrant.setOrderId(registrant.getOrderId());
        existingRegistrant.setTotalProduct(registrant.getTotalProduct());
      } else {
        existingRegistrant = registrant;
      }
      em.persist(existingRegistrant);
      em.flush();
    }
  }

}

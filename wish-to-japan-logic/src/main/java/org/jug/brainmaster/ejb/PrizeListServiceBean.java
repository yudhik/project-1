package org.jug.brainmaster.ejb;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jug.brainmaster.model.PrizeList;

@Stateless
@LocalBean
public class PrizeListServiceBean {

  @PersistenceContext
  private EntityManager em;

  public List<PrizeList> findAll() {
    return em.createQuery("from PrizeList", PrizeList.class).getResultList();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public List<PrizeList> findAllGrandPrize() {
    return em.createQuery("from PrizeList pl where pl.grandPrize = true", PrizeList.class)
        .getResultList();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public List<PrizeList> findAllNonGrandPrize() {
    return em.createQuery("from PrizeList pl where pl.grandPrize = false", PrizeList.class)
        .getResultList();
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public PrizeList findById(Long id) {
    return em.find(PrizeList.class, id);
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public PrizeList findByName(String name) {
    return em.createQuery("from PrizeList prizeList where prizeList.name = :name", PrizeList.class)
        .setParameter("name", name).getSingleResult();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void saveOrUpdate(PrizeList prizeList) {
    if (prizeList != null) {
      PrizeList existingPrizeList = null;
      if (prizeList.getId() != null) {
        existingPrizeList = findById(prizeList.getId());
      }
      if (existingPrizeList != null) {
        existingPrizeList.setGrandPrize(prizeList.getGrandPrize());
        existingPrizeList.setName(prizeList.getName());
      } else {
        existingPrizeList = prizeList;
      }
      em.persist(existingPrizeList);
      em.flush();
    }
  }

}

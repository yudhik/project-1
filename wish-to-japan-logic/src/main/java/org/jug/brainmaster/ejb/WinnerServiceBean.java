package org.jug.brainmaster.ejb;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.jug.brainmaster.model.PrizeList;
import org.jug.brainmaster.model.Registrant;
import org.jug.brainmaster.model.Winners;

@Stateless
@LocalBean
public class WinnerServiceBean {

  private static Random RANDOM = new Random();

  @PersistenceContext
  private EntityManager em;

  @Inject
  private Logger log;

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(Winners winners) {
    if (winners != null) {
      Winners existingWinners = em.find(Winners.class, winners.getId());
      if (existingWinners != null) {
        em.remove(existingWinners);
      }
    }
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public Winners findById(Long id) {
    Winners winners = em.find(Winners.class, id);
    if (winners != null) {
      Hibernate.initialize(winners.getPrize());
      Hibernate.initialize(winners.getRegistrant());
    }
    return winners;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public List<Winners> findByPrize(PrizeList prizeList) {
    return em.createQuery("from Winners winners where winners.prize = :prizeList", Winners.class)
        .setParameter("prizeList", prizeList).getResultList();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public List<Winners> findByRegistrant(Registrant registrant) {
    return em
        .createQuery("from Winners winners where winners.registrant = :registrant", Winners.class)
        .setParameter("registrant", registrant).getResultList();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void generateRandomWinner(PrizeList prizeList, Integer winnerSize) {
    Long totalRegistrant = em
        .createQuery("select count(registrant.voucherCode) from Registrant registrant", Long.class)
        .getSingleResult();
    // random.setSeed(totalRegistrant);
    log.log(Level.FINER, "total registrant : " + totalRegistrant);
    while (winnerSize > 0) {
      PrizeList existingPrizeList = em.getReference(PrizeList.class, prizeList.getId());
      Registrant registrant = null;
      while (registrant == null) {
        int position = Math.abs(RANDOM.nextInt(totalRegistrant.intValue()));
        log.log(Level.FINER, "position : " + position);
        registrant = em.createQuery("from Registrant registrant", Registrant.class)
            .setFirstResult(position).setMaxResults(1).getSingleResult();
        if (findByRegistrant(registrant).size() > 0) {
          registrant = null;
        }
      }
      Winners winners = new Winners(existingPrizeList, registrant);
      em.persist(winners);
      winnerSize--;
    }
    em.flush();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public List<Winners> getAllWinners() {
    List<Winners> result = em.createQuery("from Winners winners", Winners.class).getResultList();
    if (result != null) {
      for (Winners winner : result) {
        Hibernate.initialize(winner.getPrize());
        Hibernate.initialize(winner.getRegistrant());
      }
    }
    return result;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void saveOrUpdate(Winners winners) {
    if (winners != null) {
      Winners existingWinners = null;
      if (winners.getId() == null) {
        log.log(Level.FINEST, "Create new Winners");
        existingWinners = new Winners();
      } else {
        log.log(Level.FINEST, "Update Winners");
        existingWinners = em.find(Winners.class, winners.getId());
      }
      PrizeList prizeList = em.getReference(PrizeList.class, winners.getPrize().getId());
      Registrant registrant =
          em.getReference(Registrant.class, winners.getRegistrant().getVoucherCode());
      existingWinners.setPrize(prizeList);
      existingWinners.setRegistrant(registrant);
      em.persist(existingWinners);
      em.flush();
    }
  }
}

package org.jug.brainmaster.ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.jug.brainmaster.model.GrandPrizeCandidate;
import org.jug.brainmaster.model.PrizeList;
import org.jug.brainmaster.model.Registrant;
import org.jug.brainmaster.model.Winners;

@Stateless
@LocalBean
public class GrandPrizeCandidateServiceBean {

  private static final String SELECT_CANDIDATE_WITH_MAIL =
      "from GrandPrizeCandidate candidate where candidate.emailAddress = :emailAddress";

  private static final String SELECT_CURRENT_CANDIDATE_WITH_MAIL =
      SELECT_CANDIDATE_WITH_MAIL + " and candidate.current = true";

  @PersistenceContext
  private EntityManager em;

  @Inject
  private Logger log;

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public boolean claimPrize(String emailAddress) {
    GrandPrizeCandidate grandPrizeWinner = null;
    long start = System.nanoTime();
    try {
      grandPrizeWinner =
          em.createQuery(SELECT_CURRENT_CANDIDATE_WITH_MAIL, GrandPrizeCandidate.class)
              .setParameter("emailAddress", emailAddress).getSingleResult();
    } catch (NoResultException e) {
      log.log(Level.FINE, "select NO_RESULT candidate with mail : " + emailAddress + ", for "
          + (System.nanoTime() - start) / 1000000 + " millis");
      return false;
    }
    if (grandPrizeWinner == null) {
      log.log(Level.FINE, "select NULL candidate with mail : " + emailAddress + ", for "
          + (System.nanoTime() - start) / 1000000 + " millis");
      return false;
    }
    try {
      log.log(Level.FINE,
          "email address : " + emailAddress + " is valid try to save it as claimed");
      Hibernate.initialize(grandPrizeWinner.getPrizeList());
      Hibernate.initialize(grandPrizeWinner.getRegistrant());
      Winners winner = new Winners();
      winner.setPrize(grandPrizeWinner.getPrizeList());
      winner.setRegistrant(grandPrizeWinner.getRegistrant());
      em.persist(winner);

      grandPrizeWinner.setClaimed(true);
      grandPrizeWinner.setCurrent(false);
      saveOrUpdate(grandPrizeWinner);
      log.log(Level.FINE, "select VALID candidate with mail : " + emailAddress + ", for "
          + (System.nanoTime() - start) / 1000000 + " millis");
    } catch (Exception e) {
      log.log(Level.WARNING,
          "something wrong when claim process try to update value for email address : "
              + emailAddress,
          e);
      log.log(Level.SEVERE, "show stack trace to analyze error", e);
    }
    return true;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public int countAllCandidate() {
    return em.createQuery("select count(candidate.id) from GrandPrizeCandidate candidate")
        .getFirstResult();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(GrandPrizeCandidate candidate) {
    GrandPrizeCandidate existingCandidate = findById(candidate.getId());
    if (existingCandidate != null) {
      em.remove(existingCandidate);
      em.flush();
    }
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public GrandPrizeCandidate findByEmailAddress(String emailAddress) {
    GrandPrizeCandidate existingCandidate =
        em.createQuery(SELECT_CANDIDATE_WITH_MAIL, GrandPrizeCandidate.class)
            .setParameter("emailAddress", emailAddress).getSingleResult();
    // GrandPrizeCandidate resultCandidate = new GrandPrizeCandidate();
    if (existingCandidate != null) {
      Hibernate.initialize(existingCandidate.getRegistrant());
      Hibernate.initialize(existingCandidate.getPrizeList());
      // resultCandidate.setClaimed(existingCandidate.isClaimed());
      // resultCandidate.setCurrent(existingCandidate.isCurrent());
      // resultCandidate.setEmailAddress(existingCandidate.getEmailAddress());
      // resultCandidate.setId(existingCandidate.getId());
      // resultCandidate.setPrizeList(existingCandidate.getPrizeList());
      // resultCandidate.setRegistrant(existingCandidate.getRegistrant());
      // } else {
      // return null;
    }
    // return resultCandidate;
    return existingCandidate;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public GrandPrizeCandidate findById(Long id) {
    GrandPrizeCandidate candidate = em.find(GrandPrizeCandidate.class, id);
    if (candidate != null) {
      Hibernate.initialize(candidate.getPrizeList());
      Hibernate.initialize(candidate.getRegistrant());
    }
    return candidate;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public List<GrandPrizeCandidate> getAllCandidateWithPagination(int rowSize, int pageNumber) {
    List<GrandPrizeCandidate> candidates = new ArrayList<GrandPrizeCandidate>();
    int startPosition = (pageNumber - 1) * rowSize;
    candidates = em
        .createQuery("from GrandPrizeCandidate candidate ORDER BY candidate.id",
            GrandPrizeCandidate.class)
        .setFirstResult(startPosition).setMaxResults(rowSize).getResultList();
    return candidates;
  }


  public List<GrandPrizeCandidate> getCandidateForGrandPrize(PrizeList prizeList) {
    PrizeList existingPrizeList = em.find(PrizeList.class, prizeList.getId());
    return em.createQuery(
        "from GrandPrizeCandidate candidate where candidate.prizeList = :prizeList  ORDER BY candidate.id",
        GrandPrizeCandidate.class).setParameter("prizeList", existingPrizeList).getResultList();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void putCurrent(GrandPrizeCandidate candidate) {
    List<GrandPrizeCandidate> candidates =
        em.createQuery("from GrandPrizeCandidate", GrandPrizeCandidate.class).getResultList();
    for (GrandPrizeCandidate grandPrizeCandidate : candidates) {
      grandPrizeCandidate.setCurrent(false);
      em.persist(grandPrizeCandidate);
    }
    em.flush();
    GrandPrizeCandidate existingCandidate = findById(candidate.getId());
    if (existingCandidate != null) {
      existingCandidate.setCurrent(true);
      em.persist(existingCandidate);
      em.flush();
    }
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void saveOrUpdate(GrandPrizeCandidate candidate) throws Exception {
    if (candidate.getId() != null && candidate.getRegistrant() != null
        && candidate.getPrizeList() != null) {
      GrandPrizeCandidate existingCandidate = em.find(GrandPrizeCandidate.class, candidate.getId());
      Registrant registrant =
          em.getReference(Registrant.class, candidate.getRegistrant().getVoucherCode());
      existingCandidate.setRegistrant(registrant);
      PrizeList prizeList = em.getReference(PrizeList.class, candidate.getPrizeList().getId());
      existingCandidate.setPrizeList(prizeList);
      existingCandidate.setCurrent(candidate.isCurrent());
      existingCandidate.setClaimed(candidate.isClaimed());
      existingCandidate.setEmailAddress(candidate.getEmailAddress());
      candidate = existingCandidate;
    }
    em.persist(candidate);
  }

}

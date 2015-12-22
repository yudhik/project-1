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
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.jug.brainmaster.model.GrandPrizeCandidate;
import org.jug.brainmaster.model.PrizeList;
import org.jug.brainmaster.model.Registrant;

@Stateless
@LocalBean
public class GrandPrizeCandidateServiceBean {

  private static final String SELECT_CURRENT_CANDIDATE_WITH_MAIL =
      "from GrandPrizeCandidate candidate where candidate.emailAddress = :emailAddress and candidate.current = true";

  @PersistenceContext
  private EntityManager em;

  @Inject
  private Logger log;

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public boolean claimPrize(String emailAddress) {
    GrandPrizeCandidate winner =
        em.createQuery(SELECT_CURRENT_CANDIDATE_WITH_MAIL, GrandPrizeCandidate.class)
        .setParameter("emailAddress", emailAddress).getSingleResult();
    if (winner == null) {
      return false;
    }
    try {
      winner.setClaimed(true);
      log.log(Level.INFO,
          "email address : " + emailAddress + " is valid try to save it as claimed");
      saveOrUpdate(winner);
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
    return em.createQuery("select count(candidate.id) from GrandPrizeCandidate candidate").getFirstResult();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(GrandPrizeCandidate candidate) {
    GrandPrizeCandidate existingCandidate = findById(candidate.getId());
    em.remove(existingCandidate);
    em.flush();
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
    candidates = em.createQuery("from GrandPrizeCandidate candidate", GrandPrizeCandidate.class).setFirstResult(startPosition).setMaxResults(rowSize).getResultList();
    return candidates;
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
    em.flush();
  }

}

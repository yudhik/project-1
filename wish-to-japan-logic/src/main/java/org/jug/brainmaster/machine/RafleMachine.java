package org.jug.brainmaster.machine;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jug.brainmaster.ejb.GrandPrizeCandidateServiceBean;
import org.jug.brainmaster.ejb.PrizeListServiceBean;
import org.jug.brainmaster.ejb.RegistrantServiceBean;
import org.jug.brainmaster.ejb.WinnerServiceBean;
import org.jug.brainmaster.model.GrandPrizeCandidate;
import org.jug.brainmaster.model.PrizeList;
import org.jug.brainmaster.model.Registrant;
import org.jug.brainmaster.model.Winners;
import org.jug.brainmaster.model.response.GameMessage;
import org.jug.brainmaster.model.response.GameState;

@Stateless
@LocalBean
public class RafleMachine {

  private static final String JBOSS_CONFIG_DIRECTORY_KEY = "jboss.server.config.dir";
  private static final String APPLICATION_CONFIG_FILENAME = "wish-to-japan-config.properties";
  private static final String START_DATE_CONFIG_KEY = "wish.to.japan.start.date";
  private static final String DATE_FORMAT_CONFIG_KEY = "wish.to.japan.date.format";
  private static final String WAIT_FOR_WINNER_TIMEOUT_CONFIG_KEY = "wish.to.japan.winner.timeout";
  private static final String FAKE_RAFLE_TIMEOUT_CONFIG_KEY = "wish.to.japan.rafle.timeout";
  private static final String MAX_ALLOWED_CLAIM_COUNT_CONFIG_KEY =
      "wish.to.japan.max.allowed.claim.count";
  private static final String REGISTRANT_COUNT_BEFORE_SHOW_GRAND_PRIZE_CANDIDATE =
      "wish.to.japan.registrant.count.before.grand.prize.candidate";
  private static final Random RANDOM = new Random();

  @Inject
  private GrandPrizeCandidateServiceBean grandPrizeCandidateServiceBean;

  @Inject
  private PrizeListServiceBean prizeListServiceBean;

  @Inject
  private RegistrantServiceBean registrantServiceBean;

  @Inject
  private WinnerServiceBean winnerServiceBean;

  @Inject
  private Event<GameMessage> gameEvent;

  @Inject
  private Logger log;

  public RafleMachine() throws Exception {
  }

  private String combineName(String firstName, String lastName) {
    return firstName + " " + lastName;
  }

  private void doShowRandomValue(int registrantCountBeforeShowCandidate) {
    try {
      List<Registrant> registrants = registrantServiceBean.findFakeRegistrantToShow(RANDOM,
          registrantCountBeforeShowCandidate);
      for (Registrant registrant : registrants) {
        GameMessage message =
            new GameMessage(combineName(registrant.getFirstName(), registrant.getLastName()),
                maskingVoucher(registrant.getVoucherCode()), GameState.RUNNING);
        log.log(Level.FINER, "send message to view : " + message);
        gameEvent.fire(message);
        Thread.sleep(200);
      }
    } catch (InterruptedException e) {
      log.log(Level.SEVERE, "something bad happened at doShowRandomValue method", e);
    }
  }

  private List<PrizeList> getGrandAllGrandPrize() {
    return prizeListServiceBean.findAllGrandPrize();
  }

  private Map<PrizeList, GrandPrizeCandidate> getGrandPrizeWinnerData(
      List<PrizeList> allGrandPrizes) {
    Map<PrizeList, GrandPrizeCandidate> data = new HashMap<PrizeList, GrandPrizeCandidate>();
    for(PrizeList prizeList : allGrandPrizes) {
      List<Winners> winnerList = winnerServiceBean.findByPrize(prizeList);
      if(winnerList != null && winnerList.size() == 1) {
        Winners winners = winnerServiceBean.findById(winnerList.get(0).getId());
        data.put(prizeList, new GrandPrizeCandidate(winners.getRegistrant(), winners.getPrize()));
      }
    }
    return data;
  }

  private GrandPrizeCandidate getRegionWinner(GrandPrizeCandidate winners,
      long waitForWinnerTimeout) {
    long waitStartTime = System.nanoTime();
    boolean claimed = false;
    while (((System.nanoTime() - waitStartTime) / 1000000000) < waitForWinnerTimeout) {
      log.log(Level.FINER, "get winner from database");
      try {
        GrandPrizeCandidate winnerToWait = grandPrizeCandidateServiceBean.findById(winners.getId());
        gameEvent.fire(new GameMessage(
            combineName(winnerToWait.getRegistrant().getFirstName(),
                winnerToWait.getRegistrant().getLastName()),
            maskingVoucher(winnerToWait.getRegistrant().getVoucherCode()), GameState.WAITING));
        claimed = winnerToWait.isClaimed();
        if (claimed) {
          log.log(Level.FINER, "we got winner who claimed here : " + winners.getEmailAddress());
          return winnerToWait;
        }
        Thread.sleep(200);
      } catch (InterruptedException e) {
        log.log(Level.SEVERE, "get region winner get exception", e);
      }
    }
    log.log(Level.FINER, "no body claim the prize");
    return null;
  }

  private Properties loadConfigurationFile() throws Exception {
    Properties applicationProperties = new Properties();
    FileReader reader = new FileReader(System.getProperty(JBOSS_CONFIG_DIRECTORY_KEY)
        + File.separator + APPLICATION_CONFIG_FILENAME);
    applicationProperties.load(reader);
    return applicationProperties;
  }

  private String maskingVoucher(String voucherCode) {
    return voucherCode.substring(0, voucherCode.lastIndexOf("-") + 1).concat("***")
        .concat(voucherCode.substring(voucherCode.length() - 2));
  }

  private Map<PrizeList, List<GrandPrizeCandidate>> populateGrandPrizeCandidate(
      List<PrizeList> allGrandPrizes) {
    Map<PrizeList, List<GrandPrizeCandidate>> regionWinnerCandidateMapping =
        new HashMap<PrizeList, List<GrandPrizeCandidate>>();
    for (PrizeList prizeList : allGrandPrizes) {
      regionWinnerCandidateMapping.put(prizeList,
          grandPrizeCandidateServiceBean.getCandidateForGrandPrize(prizeList));
    }
    return regionWinnerCandidateMapping;
  }

  private void rafleFakeCandidate(long fakeRafleTimeoutInSecond,
      int registrantCountBeforeShowCandidate) {
    long rafleStartTime = System.nanoTime();
    while (((System.nanoTime() - rafleStartTime) / 1000000000) < fakeRafleTimeoutInSecond) {
      doShowRandomValue(registrantCountBeforeShowCandidate);
    }
  }

  @Asynchronous
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void start() throws Exception {
    log.log(Level.FINER, "starting rafle machine");
    Properties prop = loadConfigurationFile();
    SimpleDateFormat dateFormat = new SimpleDateFormat(prop.getProperty(DATE_FORMAT_CONFIG_KEY));
    Date startDate = dateFormat.parse(prop.getProperty(START_DATE_CONFIG_KEY));
    long maximumAllowedClaimCount =
        Long.parseLong(prop.getProperty(MAX_ALLOWED_CLAIM_COUNT_CONFIG_KEY));
    long fakeRafleTimeout = Long.parseLong(prop.getProperty(FAKE_RAFLE_TIMEOUT_CONFIG_KEY));
    long waitForWinnerTimeout =
        Long.parseLong(prop.getProperty(WAIT_FOR_WINNER_TIMEOUT_CONFIG_KEY));
    int registrantCountBeforeShowCandidate =
        Integer.parseInt(prop.getProperty(REGISTRANT_COUNT_BEFORE_SHOW_GRAND_PRIZE_CANDIDATE));

    List<PrizeList> allGrandPrizes = getGrandAllGrandPrize();
    Map<PrizeList, List<GrandPrizeCandidate>> regionWinnerCandidateMapping =
        populateGrandPrizeCandidate(allGrandPrizes);
    Map<PrizeList, GrandPrizeCandidate> regionWinner = getGrandPrizeWinnerData(allGrandPrizes);

    log.log(Level.FINER, "waiting time to start or winner to claim, except all region winner is settle the game will not start");
    while (new Date().after(startDate) && regionWinner.size() < allGrandPrizes.size()) {
      for (PrizeList prizeList : allGrandPrizes) {
        int counter = 0;
        GrandPrizeCandidate grandPrizeWinner = null;
        while (grandPrizeWinner == null) {
          if (counter < maximumAllowedClaimCount) {
            log.log(Level.FINER, "try to rafle fake candidate");
            rafleFakeCandidate(fakeRafleTimeout, registrantCountBeforeShowCandidate);
            log.log(Level.FINER, "put current state for : " + regionWinnerCandidateMapping.get(prizeList).get(counter).getEmailAddress());
            grandPrizeCandidateServiceBean.putCurrent(regionWinnerCandidateMapping.get(prizeList).get(counter));
            log.log(Level.FINER, "wait for winner to clain");
            grandPrizeWinner = getRegionWinner(regionWinnerCandidateMapping.get(prizeList).get(counter),
                waitForWinnerTimeout);
            counter++;
          } else {
            grandPrizeWinner = regionWinnerCandidateMapping.get(prizeList).get(0);
            grandPrizeCandidateServiceBean.claimPrize(grandPrizeWinner.getEmailAddress());
            System.out.println(
                "No body claim after MAX CANDIDATE COUNTER, put default winner value for prize : "
                    + prizeList.getName());
          }
        }
        regionWinner.put(prizeList, grandPrizeWinner);
        log.log(Level.FINER, "put grandprize for "+ prizeList.getName() + " to " + grandPrizeWinner.getEmailAddress());
      }
    }
  }

}

package org.jug.brainmaster.machine;

import org.joda.time.DateTime;
import org.jug.brainmaster.model.GrandPrizeCandidate;
import org.jug.brainmaster.model.RegionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RafleMachinePrototype {

  private static final long WAIT_FOR_WINNER_TIMEOUT = 20;
  private static final long FAKE_RAFLE_TIMEOUT = 10;
  private static final Random RANDOM = new Random();
  private static final int MAX_WINNER_CANDIDATE_COUNT = 2;

  private Map<RegionType, List<GrandPrizeCandidate>> regionWinnerCandidateMapping;
  private Map<RegionType, GrandPrizeCandidate> regionWinner =
      new HashMap<RegionType, GrandPrizeCandidate>();

  public RafleMachinePrototype() {
    Date startDate = new DateTime(2015, 12, 19, 15, 0, 0).toDate();
    getCandidateWinner();
    while (new Date().after(startDate) && regionWinner.size() < 3) {
      for (RegionType regionType : RegionType.values()) {
        int counter = 0;
        GrandPrizeCandidate winners = null;
        while (winners == null) {
          if (counter < MAX_WINNER_CANDIDATE_COUNT) {
            System.out.println("try to rafle fake candidate");
            rafleFakeCandidate();
            System.out.println("wait for winner to clain");
            winners = getRegionWinner(regionWinnerCandidateMapping.get(regionType).get(counter));
            counter++;
          } else {
            winners = regionWinnerCandidateMapping.get(regionType).get(0);
            System.out.println(
                "No body claim after MAX CANDIDATE COUNTER, put default winner value for region : "
                    + regionType.getName());
          }
        }
        regionWinner.put(regionType, winners);
        System.out.println("Winner for " + regionType.getName() + " is : "
            + regionWinner.get(regionType).getRegistrant().getFirstName());
      }
    }

  }

  private void doShowRandomValue() {
    try {
      Thread.sleep(500);
      System.out.println("random value : " + RANDOM.nextInt(1000));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void getCandidateWinner() {
    regionWinnerCandidateMapping = new HashMap<RegionType, List<GrandPrizeCandidate>>();
    List<GrandPrizeCandidate> region1WinnerCandidate = new ArrayList<GrandPrizeCandidate>();
    // for(int i = 1 ; i <=5 ; i++) {
    // region1WinnerCandidate.add(new GrandPrizeCandidate(new Registrant("voucher" + i, "firstName"+
    // i, "lastName"+i, "region1mail"+i+"@woo.com", ""+i), RegionType.REGION_1));
    // }
    // regionWinnerCandidateMapping.put(RegionType.REGION_1, region1WinnerCandidate);
    //
    // List<GrandPrizeCandidate> region2WinnerCandidate = new ArrayList<GrandPrizeCandidate>();
    // for(int i = 1 ; i <=5 ; i++) {
    // region2WinnerCandidate.add(new GrandPrizeCandidate(new Registrant("voucher" + i, "firstName"+
    // i, "lastName"+i, "region2mail"+i+"@woo.com", ""+i), RegionType.REGION_2));
    // }
    // regionWinnerCandidateMapping.put(RegionType.REGION_2, region2WinnerCandidate);
    //
    // List<GrandPrizeCandidate> region3WinnerCandidate = new ArrayList<GrandPrizeCandidate>();
    // for(int i = 1 ; i <=5 ; i++) {
    // region3WinnerCandidate.add(new GrandPrizeCandidate(new Registrant("voucher" + i, "firstName"+
    // i, "lastName"+i, "region3mail"+i+"@woo.com", ""+i), RegionType.REGION_3));
    // }
    // regionWinnerCandidateMapping.put(RegionType.REGION_3, region3WinnerCandidate);
  }

  private GrandPrizeCandidate getRegionWinner(GrandPrizeCandidate winners) {
    long waitStartTime = System.nanoTime();
    while (((System.nanoTime() - waitStartTime) / 1000000000) < WAIT_FOR_WINNER_TIMEOUT) {//
      System.out.println("get winner from database");
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println("no body claim the prize");
    return null;
  }

  private void rafleFakeCandidate() {
    long rafleStartTime = System.nanoTime();
    while (((System.nanoTime() - rafleStartTime) / 1000000000) < FAKE_RAFLE_TIMEOUT) {
      doShowRandomValue();
    }
  }

}

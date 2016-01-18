//package org.jug.brainmaster.ws.startup;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.ejb.Schedule;
//import javax.ejb.Singleton;
//import javax.ejb.Startup;
//import javax.inject.Inject;
//
//import org.jug.brainmaster.ws.entity.States;
//import org.jug.brainmaster.ws.response.StatusResponse;
//import org.jug.brainmaster.ws.response.UserResponse;
//import org.jug.brainmaster.ws.ws.DataSubject;
//
//@Singleton
//public class MockServerSocketListener {
//  @Inject
//  DataSubject dataSubject;
//
//  private static List<String> voucherCodes = new ArrayList<String>();
//  private static List<String> names = new ArrayList<String>();
//  private UserResponse candidate = new UserResponse();
//  private StatusResponse status = new StatusResponse();
//
//  static {
//    MockServerSocketListener.voucherCodes.add("JPN-S1027-73397");
//    MockServerSocketListener.voucherCodes.add("JPNU-O0923-09521");
//    MockServerSocketListener.voucherCodes.add("JPN-F1251-09925");
//    MockServerSocketListener.voucherCodes.add("JPNU-E5123-71728");
//    MockServerSocketListener.voucherCodes.add("JPN-T0018-65243");
//    MockServerSocketListener.voucherCodes.add("JPN-N1309-09529");
//
//    MockServerSocketListener.names.add("Sven Seventies");
//    MockServerSocketListener.names.add("Onedrous Ontario");
//    MockServerSocketListener.names.add("Fivery Fivety");
//    MockServerSocketListener.names.add("Eighteen Eightingale");
//    MockServerSocketListener.names.add("Threeacherous Treedom");
//    MockServerSocketListener.names.add("Nineious Niness");
//  }
//
//  // ubah sesuai kebutuhan
//  public void mockIt() {
//    try {
//      Date now = null;
//      Date oneMinutesLoopUser = null;
//      Date fiveMinutesLoopUser = null;
//      Date threeMinutesLoopUser = null;
//
//      if (System.getProperty("isMock").equalsIgnoreCase("true")) {
//
//        now = new Date();
//        Date twentySecondLoop =
//            new Date(now.getTime() + TimeUnit.MILLISECONDS.convert(20L, TimeUnit.SECONDS));
//        while (twentySecondLoop.after(new Date())) {
//          Thread.sleep(1000);
//        }
//
//        this.status.setPromoState(States.PROMO_ON.getValue());
//        this.status.setRaffleState(States.RAFFLE_RUNNING.getValue());
//        now = new Date();
//        oneMinutesLoopUser =
//            new Date(now.getTime() + TimeUnit.MILLISECONDS.convert(10L, TimeUnit.SECONDS));
//
//        // First Run
//        while (oneMinutesLoopUser.after(new Date())) {
//          randomize();
//
//          this.status.setRaffleState(States.RAFFLE_RUNNING.getValue());
//          this.status.setSequence(1);
//          this.status.setCandidateSequence(1);
//          this.status.setCurrentParticipant(this.candidate);
//
//          this.dataSubject.setStatusResponse(this.status);
//          Thread.sleep(500);
//        }
//
//        // Show user 5min but no claim
//        now = new Date();
//        fiveMinutesLoopUser =
//            new Date(now.getTime() + TimeUnit.MILLISECONDS.convert(20L, TimeUnit.SECONDS));
//        while (fiveMinutesLoopUser.after(new Date())) {
//          this.status.setRaffleState(States.RAFFLE_CLAIMING.getValue());
//
//          Long timer = (fiveMinutesLoopUser.getTime() - new Date().getTime()) / 1000;
//          this.status.setTimer(timer.intValue());
//
//          this.dataSubject.setStatusResponse(this.status);
//          Thread.sleep(500);
//        }
//
//        // loop again for 1 minute
//        now = new Date();
//        oneMinutesLoopUser =
//            new Date(now.getTime() + TimeUnit.MILLISECONDS.convert(10L, TimeUnit.SECONDS));
//        while (oneMinutesLoopUser.after(new Date())) {
//          randomize();
//
//          this.status.setRaffleState(States.RAFFLE_RUNNING.getValue());
//          this.status.setCandidateSequence(2);
//
//          this.dataSubject.setStatusResponse(this.status);
//          Thread.sleep(500);
//        }
//
//        // Show user AND CLAIMED AT THE 3RD MIN
//        now = new Date();
//        fiveMinutesLoopUser =
//            new Date(now.getTime() + TimeUnit.MILLISECONDS.convert(20L, TimeUnit.SECONDS));
//        boolean claimed = false;
//        while (fiveMinutesLoopUser.after(new Date()) && !claimed) {
//          this.status.setRaffleState(States.RAFFLE_CLAIMING.getValue());
//
//          Long timer = (fiveMinutesLoopUser.getTime() - new Date().getTime()) / 1000;
//
//          if (timer == 5) {
//            claimed = true;
//          }
//
//          this.status.setTimer(timer.intValue());
//
//          this.dataSubject.setStatusResponse(this.status);
//          Thread.sleep(500);
//        }
//
//        // Second raffle. 1 min
//        now = new Date();
//        oneMinutesLoopUser =
//            new Date(now.getTime() + TimeUnit.MILLISECONDS.convert(10L, TimeUnit.SECONDS));
//        while (oneMinutesLoopUser.after(new Date())) {
//          randomize();
//          this.status.setSequence(2);
//          this.status.setCandidateSequence(1);
//          this.status.setRaffleState(States.RAFFLE_RUNNING.getValue());
//
//          this.dataSubject.setStatusResponse(this.status);
//          Thread.sleep(500);
//        }
//
//        // Second raffle. CLAIMING, no claim
//        now = new Date();
//        fiveMinutesLoopUser =
//            new Date(now.getTime() + TimeUnit.MILLISECONDS.convert(20L, TimeUnit.SECONDS));
//        while (fiveMinutesLoopUser.after(new Date())) {
//          this.status.setRaffleState(States.RAFFLE_CLAIMING.getValue());
//
//          Long timer = (fiveMinutesLoopUser.getTime() - new Date().getTime()) / 1000;
//          this.status.setTimer(timer.intValue());
//
//          this.dataSubject.setStatusResponse(this.status);
//          Thread.sleep(500);
//        }
//
//        // Second raffle. RUNNING (again), 1min
//        now = new Date();
//        oneMinutesLoopUser =
//            new Date(now.getTime() + TimeUnit.MILLISECONDS.convert(20L, TimeUnit.SECONDS));
//        while (oneMinutesLoopUser.after(new Date())) {
//          randomize();
//          this.status.setCandidateSequence(2);
//          this.status.setRaffleState(States.RAFFLE_RUNNING.getValue());
//
//          this.dataSubject.setStatusResponse(this.status);
//          Thread.sleep(500);
//        }
//
//        // Second raffle. CLAIMING, claimed
//        now = new Date();
//        fiveMinutesLoopUser =
//            new Date(now.getTime() + TimeUnit.MILLISECONDS.convert(20L, TimeUnit.SECONDS));
//        claimed = false;
//        while (fiveMinutesLoopUser.after(new Date()) && !claimed) {
//          this.status.setRaffleState(States.RAFFLE_CLAIMING.getValue());
//
//          Long timer = (fiveMinutesLoopUser.getTime() - new Date().getTime()) / 1000;
//          this.status.setTimer(timer.intValue());
//
//          if (timer == 5) {
//            claimed = true;
//          }
//
//          this.dataSubject.setStatusResponse(this.status);
//          Thread.sleep(500);
//        }
//
//        // Show others for 5 min
//        now = new Date();
//        fiveMinutesLoopUser =
//            new Date(now.getTime() + TimeUnit.MILLISECONDS.convert(20L, TimeUnit.SECONDS));
//        while (fiveMinutesLoopUser.after(new Date())) {
//          randomize();
//          this.status.setRaffleState(States.RAFFLE_OTHERS_RUNNING.getValue());
//
//          this.dataSubject.setStatusResponse(this.status);
//          Thread.sleep(500);
//        }
//
//        // loop again for 1 minute
//        now = new Date();
//        oneMinutesLoopUser =
//            new Date(now.getTime() + TimeUnit.MILLISECONDS.convert(10L, TimeUnit.SECONDS));
//        while (oneMinutesLoopUser.after(new Date())) {
//          this.status.setRaffleState(States.RAFFLE_OTHERS_ENDING.getValue());
//
//          this.dataSubject.setStatusResponse(this.status);
//          Thread.sleep(500);
//        }
//
//        this.status.setPromoState(States.PROMO_OFF.getValue());
//        this.dataSubject.setStatusResponse(this.status);
//      }
//    } catch (Exception e) {
//      // tralala
//    }
//  }
//
//  private void randomize() {
//    int x = (int) (Math.random() * MockServerSocketListener.voucherCodes.size());
//
//    this.candidate.setName(MockServerSocketListener.names.get(x));
//    this.candidate.setVoucherCode(MockServerSocketListener.voucherCodes.get(x));
//
//    Logger.getLogger(MockServerSocketListener.class.getName()).log(Level.INFO,
//        "randomizing: " + this.candidate.getName() + " " + this.candidate.getVoucherCode());
//  }
//}

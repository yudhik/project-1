package org.jug.brainmaster.model.response;

// function onMessage(promoState, raffleState, isGrandPrize, sequence, timer, voucherCode, name)
public class StatusResponse {
  private UserResponse currentParticipant;

  private String promoState;

  private String raffleState;
  // RAFFLE_RUNNING, RAFFLE_CLAIMING, RAFFLE_OTHERS_RUNNING, RAFFLE_OTHERS_CONGRATULATIONS

  private Integer sequence; // URUTAN HADIAH

  private Integer candidateSequence; // URUTAN PEMENANG

  private Integer timer;

  public StatusResponse() {

  }

  public StatusResponse(String promoState, String raffleState, Integer sequence,
      Integer candidateSequence, Integer timer, UserResponse currentParticipant) {
    this.promoState = promoState;
    this.raffleState = raffleState;
    this.candidateSequence = candidateSequence;
    this.sequence = sequence;
    this.timer = timer;
    this.currentParticipant = currentParticipant;
  }

  public Integer getCandidateSequence() {
    return candidateSequence;
  }

  public UserResponse getCurrentParticipant() {
    return currentParticipant;
  }

  public String getPromoState() {
    return promoState;
  }

  public String getRaffleState() {
    return raffleState;
  }

  public Integer getSequence() {
    return sequence;
  }

  public Integer getTimer() {
    return timer;
  }

  public void setCandidateSequence(Integer candidateSequence) {
    this.candidateSequence = candidateSequence;
  }

  public void setCurrentParticipant(UserResponse currentParticipant) {
    this.currentParticipant = currentParticipant;
  }

  public void setPromoState(String promoState) {
    this.promoState = promoState;
  }

  public void setRaffleState(String raffleState) {
    this.raffleState = raffleState;
  }

  public void setSequence(Integer sequence) {
    this.sequence = sequence;
  }

  public void setTimer(Integer timer) {
    this.timer = timer;
  }

  @Override
  public String toString() {
    return "StatusResponse{" + "currentParticipant=" + currentParticipant + ", promoState='"
        + promoState + '\'' + ", raffleState='" + raffleState + '\'' + ", sequence=" + sequence
        + ", candidateSequence=" + candidateSequence + ", timer=" + timer + '}';
  }
}

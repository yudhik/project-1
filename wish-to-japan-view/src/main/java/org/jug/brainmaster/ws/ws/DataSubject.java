package org.jug.brainmaster.ws.ws;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.jug.brainmaster.ws.response.StatusResponse;


@Startup
@Singleton
public class DataSubject {
  @Inject
  private Logger log;

  private StatusResponse statusResponse;

  private List<Observer> observers = new ArrayList<Observer>();

  public void attach(Observer observer) {
    this.observers.add(observer);
  }

  public void detach(Observer observer) {
    this.observers.remove(observer);
  }

  public StatusResponse getStatusResponse() {
    return this.statusResponse;
  }

  public void notifyAllObservers() {
    for (Observer observer : this.observers) {
      log.info("OBSERVER UPDATE CALLED");
      observer.update();
    }
  }

  @PostConstruct
  public void onStartup() {
    System.out.println("Initialization success.");
  }

  public void setStatusResponse(StatusResponse statusResponse) {
    this.statusResponse = statusResponse;
    notifyAllObservers();
  }
}

package org.jug.brainmaster.ws.ws;


import org.jug.brainmaster.model.response.GameMessage;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Singleton
@LocalBean
public class DataSubject {
  @Inject
  private Logger log;

  private GameMessage statusResponse;

  private List<Observer> observers = new ArrayList<Observer>();

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void attach(Observer observer) {
    this.observers.add(observer);
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void detach(Observer observer) {
    this.observers.remove(observer);
  }

  public GameMessage getStatusResponse() {
    return this.statusResponse;
  }

  public void notifyAllObservers() {
    for (Observer observer : this.observers) {
      observer.update();
    }
  }

  @PostConstruct
  public void onStartup() {
    System.out.println("Initialization success.");
  }

  public void setStatusResponse(GameMessage statusResponse) {
    this.statusResponse = statusResponse;
    notifyAllObservers();
  }
}

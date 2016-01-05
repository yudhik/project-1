package org.jug.brainmaster.ws.ws;

import org.jug.brainmaster.model.response.EmailCheckResponse;

import javax.websocket.Session;

public abstract class Observer {
  protected DataSubject subject;
  protected Session session;

  public abstract void update();

  public abstract void detach();

}


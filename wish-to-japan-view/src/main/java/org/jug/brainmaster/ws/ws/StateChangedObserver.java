package org.jug.brainmaster.ws.ws;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.Session;

public class StateChangedObserver extends Observer {
  private Gson gson;

  public StateChangedObserver(DataSubject subject, Session session) {
    this.subject = subject;
    this.subject.attach(this);
    GsonBuilder builder = new GsonBuilder();
    gson = builder.create();
    this.session = session;
  }

  @Override
  public void detach() {
    this.subject.detach(this);
  }

  @Override
  public void update() {
    try {
      session.getBasicRemote().sendText(gson.toJson(subject.getStatusResponse()));
    } catch (Exception e) {
      e.getStackTrace();
    }
  }
}

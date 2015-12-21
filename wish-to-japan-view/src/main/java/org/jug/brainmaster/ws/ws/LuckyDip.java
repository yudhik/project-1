package org.jug.brainmaster.ws.ws;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/luckyDip")
public class LuckyDip implements Serializable {

  @Inject
  DataSubject dataSubject;

  private StateChangedObserver stateChangedObserver;

  private static final Logger LOGGER = Logger.getLogger(LuckyDip.class.getName());

  @OnClose
  public void destroy(Session session) throws Exception {
    if (session.isOpen()) {
      session.close();
    }
    stateChangedObserver.detach();
  }

  @OnOpen
  public void monitorLuckyDip(Session session) throws Exception {
    LOGGER.severe("A client connected :" + session.getId());
    stateChangedObserver = new StateChangedObserver(dataSubject, session);
  }

  @OnError
  public void errorHandler(Session session, Throwable e) throws Exception{
    LOGGER.log(Level.SEVERE, "Error happened! " + e.getStackTrace(), e);
    session.close();
    stateChangedObserver.detach();
  }
}

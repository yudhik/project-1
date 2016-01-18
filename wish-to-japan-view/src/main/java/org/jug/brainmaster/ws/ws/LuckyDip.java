package org.jug.brainmaster.ws.ws;

import org.jug.brainmaster.mdb.SessionObserver;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpoint;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/luckyDip")
public class LuckyDip{

  @Inject
  SessionObserver sessionObserver;

  private static final Logger LOGGER = Logger.getLogger(LuckyDip.class.getName());
  @Inject
  DataSubject dataSubject;
  private StateChangedObserver stateChangedObserver;

  @OnClose
  public void destroy(Session session) throws Exception {
    if (session.isOpen()) {
      session.close();
    }
    stateChangedObserver.detach();
  }

  @OnError
  public void errorHandler(Session session, Throwable e) throws Exception {
    LOGGER.log(Level.SEVERE, "Error happened! " + e.getStackTrace(), e);
    session.close();
    stateChangedObserver.detach();
  }

  @OnOpen
  public void monitorLuckyDip(Session session) throws Exception {
    LOGGER.severe("A client connected :" + session.getId());
    stateChangedObserver = new StateChangedObserver(dataSubject, session);
  }

  @OnMessage
  public void receiveClaim(String email, Session session) throws Exception {
    // TODO: 1/18/2016
    sessionObserver.checkEmail(email, session);
    //session.getBasicRemote().sendText(gson.toJson(subject.getStatusResponse()));
  }
}

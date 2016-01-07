package org.jug.brainmaster.ws.ws;

import org.jug.brainmaster.mdb.SessionObserver;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/luckyDip")
public class LuckyDip {

  @Inject
  GameMessageListenerServiceBean gameMessageListenerServiceBean;
  @Inject
  SessionObserver sessionObserver;
  @Inject
  DataSubject dataSubject;
  @Inject
  private Logger log;

  @OnClose
  public void destroy(Session session) throws Exception {
    if (session.isOpen()) {
      log.log(Level.FINER, "Session :" + session.getId() + " opens a closing connection");
      session.close();
    }
    log.log(Level.FINER, "Detaching session :" + session.getId());
    gameMessageListenerServiceBean.detachSession(session);
  }

  @OnError
  public void errorHandler(Session session, Throwable e) throws Exception {
    log.log(Level.SEVERE, "Error happened on Session: " + session.getId(), e);
    session.close();
    log.log(Level.FINER, "Detaching session: " + session.getId());
    gameMessageListenerServiceBean.detachSession(session);
  }

  @OnOpen
  public void monitorLuckyDip(Session session) throws Exception {
    log.log(Level.FINER, "A client connected :" + session.getId() + " and register it as listener");
    gameMessageListenerServiceBean.attachSession(session);
  }

  @OnMessage
  public void receiveClaim(String email) throws Exception {
    sessionObserver.checkEmail(email);
  }
}

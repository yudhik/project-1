package org.jug.brainmaster.websocket;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.jug.brainmaster.ejb.GameMessageListenerServiceBean;

@ServerEndpoint("/gameMachineConnector")
public class GameMachineConnector {

  @Inject
  private Logger log;

  @Inject
  private GameMessageListenerServiceBean gameMessageListenerServiceBean;


  @OnClose
  public void destroy(Session session) throws Exception {
    if (session.isOpen()) {
      session.close();
      gameMessageListenerServiceBean.removeListener(session);
    }
  }

  @OnOpen
  public void monitorLuckyDip(Session session) throws Exception {
    log.info("A client connected :" + session.getId() + ", register it as a game listener");
    gameMessageListenerServiceBean.addListener(session);
  }

  @OnError
  public void onError(Session session, Throwable throwable) throws Exception {
    gameMessageListenerServiceBean.removeListener(session);
  }

  @OnMessage
  public void onMessage(String message) throws Exception {
    //TODO : encryptMessageFromView
  }


}

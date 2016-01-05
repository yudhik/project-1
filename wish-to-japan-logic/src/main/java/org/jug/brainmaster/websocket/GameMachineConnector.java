package org.jug.brainmaster.websocket;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.jug.brainmaster.ejb.GameMessageListenerServiceBean;
import org.jug.brainmaster.ejb.GrandPrizeCandidateServiceBean;
import org.jug.brainmaster.model.request.ClaimRequest;
import org.jug.brainmaster.model.response.GameMessage;
import org.jug.brainmaster.model.response.GameState;

@ServerEndpoint(value = "/gameMachineConnector")
public class GameMachineConnector {

  @Inject
  private Boolean isEnded;

  @Inject
  private Logger log;

  @Inject
  private GameMessageListenerServiceBean gameMessageListenerServiceBean;

  @Inject
  private GrandPrizeCandidateServiceBean grandPrizeCandidateServiceBean;

  @OnClose
  public void destroy(Session session) throws Exception {
    if (session.isOpen()) {
      log.log(Level.FINER, "session :" + session.getId() + ", is open closing connection");
      session.close();
    }
    log.log(Level.FINER, "removing session :" + session.getId() + " from collections");
    gameMessageListenerServiceBean.removeListener(session);
  }

  @OnOpen
  public void monitorLuckyDip(Session session) throws Exception {
    log.log(Level.FINER, "A client connected :" + session.getId() + ", register it as a game listener");
    try {
      if (isEnded != null) {
        session.getBasicRemote().sendText(new GameMessage(null, null, GameState.END, false, -1L).toJSON());
        session.setMaxIdleTimeout(100);
        session.close();
      } else {
        gameMessageListenerServiceBean.addListener(session);
      }
    }catch (Exception e) {
      log.log(Level.SEVERE, "exception when opening connection from client", e);
    }
  }

  @OnError
  public void onError(Session session, Throwable throwable) throws Exception {
    gameMessageListenerServiceBean.removeListener(session);
  }

  @OnMessage
  public void onMessage(String message) throws Exception {
    ClaimRequest request = new ClaimRequest(message);
    grandPrizeCandidateServiceBean.claimPrize(request.getEmailAddress());
  }
}

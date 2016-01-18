package org.jug.brainmaster.websocket;

import com.google.gson.Gson;
import org.jug.brainmaster.ejb.GameMessageListenerServiceBean;
import org.jug.brainmaster.ejb.GrandPrizeCandidateServiceBean;
import org.jug.brainmaster.model.request.ClaimRequest;
import org.jug.brainmaster.model.request.EmailCheckRequest;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/gameMachineConnector")
public class GameMachineConnector {
  @Inject
  private Gson gson;

  @Inject
  private Logger log;

  @Inject
  private GameMessageListenerServiceBean gameMessageListenerServiceBean;

  @Inject
  private GrandPrizeCandidateServiceBean grandPrizeCandidateServiceBean;


  @OnClose
  public void destroy(Session session) throws Exception {
    if (session.isOpen()) {
      session.close();
      gameMessageListenerServiceBean.removeListener(session);
    }
  }

  @OnOpen
  public void monitorLuckyDip(Session session) throws Exception {
    log.log(Level.FINER,
        "A client connected :" + session.getId() + ", register it as a game listener");
    gameMessageListenerServiceBean.addListener(session);
  }

  @OnError
  public void onError(Session session, Throwable throwable) throws Exception {
    gameMessageListenerServiceBean.removeListener(session);
  }

  @OnMessage
  public void onMessage(String message, Session session) throws Exception {
    EmailCheckRequest emailCheckRequest = gson.fromJson(message, EmailCheckRequest.class);
    ClaimRequest request = new ClaimRequest(message);
    grandPrizeCandidateServiceBean.claimPrize(request.getEmailAddress());
  }
}

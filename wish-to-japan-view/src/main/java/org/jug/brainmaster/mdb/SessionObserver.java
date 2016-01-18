package org.jug.brainmaster.mdb;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.jug.brainmaster.model.request.EmailCheckRequest;
import org.jug.brainmaster.model.response.EmailCheckResponse;
import org.jug.brainmaster.model.response.GameMessage;
import org.jug.brainmaster.ws.response.StatusResponse;
import org.jug.brainmaster.ws.ws.DataSubject;

import com.google.gson.Gson;

@Singleton
@ClientEndpoint
public class SessionObserver {

  private static final long SESSION_CHECKING_INTERVAL = 5000L;

  private static final String host = System.getProperty("logicHost");

  @Inject
  DataSubject dataSubject;

  @Inject
  private Gson gson;

  @Inject
  private Logger log;

  @Resource
  private TimerService timerService;

  private Session serverSession;

  public void checkEmail(String email, Session session) {
    try {
      if (serverSession != null) {
        EmailCheckRequest emailCheckRequest = new EmailCheckRequest(email, session.getId());
        serverSession.getBasicRemote().sendText(gson.toJson(emailCheckRequest));
      }
    } catch (Exception e) {
      log.log(Level.SEVERE, "Something bad happen! : " + e.getStackTrace(), e);
    }
  }

  @Timeout
  public void checkServerSession(Timer timer) {
    log.info("invoking timeout");
    getServerSession();
  }

  @OnError
  public void errHandler(Session session, Throwable err) throws Exception {
    log.log(Level.WARNING, "OnError invoked : " + err.getStackTrace(), err);
    serverSession = null;
    getServerSession();
  }

  public Session getServerSession() {
    if (this.serverSession == null) {
      try {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = new URI("ws://" + SessionObserver.host + "/gameMachineConnector");
        this.serverSession = container.connectToServer(this, uri);
        this.timerService.createSingleActionTimer(SessionObserver.SESSION_CHECKING_INTERVAL,
            new TimerConfig("schedule next checking interval", false));
        this.log.info("connected to server session");
      } catch (Exception e) {
        this.timerService.createSingleActionTimer(SessionObserver.SESSION_CHECKING_INTERVAL,
            new TimerConfig("schedule next checking interval", false));
      }
    }
    return this.serverSession;
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    log.info("Session close invoked, reason : " + closeReason.getReasonPhrase());
    timerService.createSingleActionTimer(SESSION_CHECKING_INTERVAL,
        new TimerConfig("schedule next checking interval", false));
    serverSession = null;
  }

  @OnMessage
  public void onMessage(String text) {
    try {
      //todo no Gson
      GameMessage sr = gson.fromJson(text, GameMessage.class);
      this.dataSubject.setStatusResponse(sr);
    } catch (Exception e) {
      log.log(Level.SEVERE, "Something bad happen! : " + e.getStackTrace(), e);
    }
  }

  public void setServerSession(Session serverSession) {
    this.serverSession = serverSession;
  }

}

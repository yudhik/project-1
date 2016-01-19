package org.jug.brainmaster.mdb;

import com.google.gson.Gson;
import org.jug.brainmaster.model.request.EmailCheckRequest;
import org.jug.brainmaster.model.response.GameMessage;
import org.jug.brainmaster.model.response.GameState;
import org.jug.brainmaster.ws.ws.DataSubject;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@ClientEndpoint
public class SessionObserver {

  private static final long SESSION_CHECKING_INTERVAL = 5000L;
  private static final long WEBSOCKET_RESPONSE_TIMEOUT = 200L;

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

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void checkEmail(String email) {
    try {
      if (serverSession != null) {
        serverSession.getBasicRemote().sendText(email);
      }
    } catch (Exception e) {
      log.log(Level.SEVERE, "Something bad happen! : " + e.getStackTrace(), e);
    }
  }

  @Timeout
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void checkServerSession(Timer timer) {
    log.info("invoking timeout");
    getServerSession();
  }

  @OnError
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void errHandler(Session session, Throwable err) throws Exception {
    log.log(Level.WARNING, "OnError invoked : " + err.getStackTrace(), err);
    serverSession = null;
    getServerSession();
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public Session getServerSession() {
    if (this.serverSession == null) {
      try {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setAsyncSendTimeout(WEBSOCKET_RESPONSE_TIMEOUT);
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
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void onClose(Session session, CloseReason closeReason) {
    log.info("Session close invoked, reason : " + closeReason.getReasonPhrase());
    timerService.createSingleActionTimer(SESSION_CHECKING_INTERVAL,
        new TimerConfig("schedule next checking interval", false));
    serverSession = null;
  }

  @OnMessage
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void onMessage(String text) {
    try {
      //todo no Gson
      GameMessage sr = gson.fromJson(text, GameMessage.class);
      this.dataSubject.setStatusResponse(sr);

      //TODO filter list of winners
    } catch (Exception e) {
      log.log(Level.SEVERE, "Something bad happen! : " + e.getStackTrace(), e);
    }
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void setServerSession(Session serverSession) {
    this.serverSession = serverSession;
  }

}

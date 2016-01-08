package org.jug.brainmaster.ws.ws;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.jug.brainmaster.model.response.WinnerResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@ClientEndpoint
public class GetWinnersWebsocket {
  private static final Logger LOG = Logger.getLogger(GetWinnersWebsocket.class.getName());

  private static final WebSocketContainer CONTAINER = ContainerProvider.getWebSocketContainer();
  private String logicHost;
  private Gson gson = new Gson();
  private Session session;
  private boolean getMessage = false;
  private List<WinnerResponse> winners = null;

  public GetWinnersWebsocket(String logicHost) {
    this.logicHost = logicHost;
  }

  public void connectToServer() throws Exception {
    LOG.log(Level.FINE, "Trying to open GetWinnersWebsocket!");
    CONTAINER.setAsyncSendTimeout(200L);
    CONTAINER.setDefaultMaxSessionIdleTimeout(1000L);
    URI uri = new URI("ws://" + logicHost + "/getWinners");
    if(session == null || !session.isOpen()) {
      session = CONTAINER.connectToServer(this, uri);
    }
  }

  public void forceClose() throws Exception {
    if (session.isOpen())
      session.close();
  }

  public Session getSession() {
    return session;
  }

  public boolean getState() {
    if (session == null) {
      return false;
    }
    return session.isOpen();
  }

  public List<WinnerResponse> getWinners() {
    return winners;
  }

  public boolean isGetMessage() {
    return getMessage;
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason) throws Exception {
    if (session.isOpen())
      session.close();
  }

  @OnMessage
  public void onMessage(String text) {
    getMessage = true;
    Type listOfWinnerType = new TypeToken<List<WinnerResponse>>() {}.getType();
    LOG.log(Level.FINE, "receive message from server : " + text);
    this.winners = gson.fromJson(text, listOfWinnerType);
  }

  public void setGetMessage(boolean getMessage) {
    this.getMessage = getMessage;
  }
}

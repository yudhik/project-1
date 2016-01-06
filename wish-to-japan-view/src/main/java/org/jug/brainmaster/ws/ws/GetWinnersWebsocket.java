package org.jug.brainmaster.ws.ws;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jug.brainmaster.model.response.WinnerResponse;
import org.jug.brainmaster.ws.startup.ApplicationConfig;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ClientEndpoint
public class GetWinnersWebsocket {
  private ApplicationConfig applicationConfig;

  public GetWinnersWebsocket(ApplicationConfig applicationConfig) {
    this.applicationConfig = applicationConfig;
  }

  private Gson gson = new Gson();
  private Logger log = Logger.getLogger(GetWinnersWebsocket.class.getName());
  private Session session;
  private boolean getMessage = false;

  private List<WinnerResponse> winners = new ArrayList<>();

  public void connectToServer() throws Exception{
    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    container.setAsyncSendTimeout(200L);
    URI uri = new URI("ws://" + applicationConfig.getLogicHost() + "/getWinners");
    session = container.connectToServer(this, uri);
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

  @OnClose
  public void onClose(Session session, CloseReason closeReason) throws Exception {
    if(session.isOpen()) session.close();
  }

  public void forceClose() throws Exception {
    if(session.isOpen()) session.close();
  }

  @OnMessage
  public void onMessage(String text) {
    getMessage = true;
    Type listOfWinnerType = new TypeToken<List<WinnerResponse>>() {
    }.getType();
    this.winners = gson.fromJson(text, listOfWinnerType);
  }

  public boolean isGetMessage() {
    return getMessage;
  }

  public void setGetMessage(boolean getMessage) {
    this.getMessage = getMessage;
  }
}

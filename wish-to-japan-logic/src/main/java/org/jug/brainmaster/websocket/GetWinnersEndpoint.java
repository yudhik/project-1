package org.jug.brainmaster.websocket;

import org.jug.brainmaster.model.response.WinnerResponse;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/getWinners")
public class GetWinnersEndpoint {

  @Inject
  private List<WinnerResponse> winners;

  @Inject
  private Logger log;

  @OnOpen
  public void connecting(Session session) throws Exception {
    log.log(Level.FINER, "A client connected :" + session.getId() + ", to request winners");
    session.getBasicRemote().sendText(generateJsonFromWinners(winners));
    session.close();
  }

  @OnClose
  public void destroy(Session session) throws Exception {
    if (session.isOpen()) {
      session.close();
    }
  }

  private String generateJsonFromWinners(List<WinnerResponse> listOfWinner) {
    if (listOfWinner == null) {
      return "[]";
    }
    StringBuilder jsonWinnerBuilder = new StringBuilder();
    jsonWinnerBuilder.append("[");
    for (WinnerResponse winner : listOfWinner) {
      jsonWinnerBuilder.append("{");
      jsonWinnerBuilder.append("\"name\":\"").append(winner.getName()).append("\"");
      jsonWinnerBuilder.append(", \"voucherCode\":\"").append(winner.getVoucherCode()).append("\"");
      jsonWinnerBuilder.append(", \"grandPrize\":\"").append(winner.isGrandPrize()).append("\"");
      jsonWinnerBuilder.append(", \"prizeName\":\"").append(winner.getPrizeName()).append("\"");
      jsonWinnerBuilder.append("}");
      if (listOfWinner.indexOf(winner) != listOfWinner.size() - 1) {
        jsonWinnerBuilder.append(",");
      }
    }
    jsonWinnerBuilder.append("]");
    return jsonWinnerBuilder.toString();
  }

  @OnError
  public void onError(Session session, Throwable throwable) throws Exception {
    log.log(Level.SEVERE, "something not good at get winners endpoind", throwable);
  }

}

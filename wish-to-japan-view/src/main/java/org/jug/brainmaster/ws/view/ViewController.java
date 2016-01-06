package org.jug.brainmaster.ws.view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import org.joda.time.DateTime;
import org.jug.brainmaster.mdb.SessionObserver;
import org.jug.brainmaster.model.response.WinnerResponse;
import org.jug.brainmaster.ws.startup.ApplicationConfig;
import org.jug.brainmaster.ws.ws.GetWinnersWebsocket;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "viewController", urlPatterns = "/pages/*")
public class ViewController extends HttpServlet {
  private static final Logger LOGGER = Logger.getLogger(ViewController.class.getName());
  private static final String COUNTDOWN_PAGE = "countdown";
  private static final String RAFFLE_PAGE = "raffle";
  private static final String WINNER_PAGE = "winner";
  private static final String FUJIFILM_WINNERS_KEY_PREFIX = "FUJIFILM_";
  private static final String JAPAN_WINNERS_KEY_PREFIX = "JPN_TICKET_";
  private static final String LENOVO_WINNERS_KEY_PREFIX = "LENOVO_";
  private static final String MACBOOK_WINNERS_KEY_PREFIX = "MACBOOK_";
  private static final String MEIZU_WINNERS_KEY_PREFIX = "MEIZU_";

  @Inject
  ApplicationConfig applicationConfig;
  @Inject
  Gson gson;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String viewHandler = "";
    try {
      //Check winners, create websocket
      //check if there are GPs

      Date startDate = applicationConfig.getStartDate();
      Date now = new Date();
      if (now.before(startDate)) {
        viewHandler = COUNTDOWN_PAGE;
        request.setAttribute("countdownTime", (startDate.getTime() - now.getTime()) / 1000);
      } else {
        GetWinnersWebsocket gww = new GetWinnersWebsocket(applicationConfig);
        List<WinnerResponse> winnerResponses = new ArrayList<>();
        boolean connectFlag = false;
        long timeoutConnectWS = (System.nanoTime() / 1000000000) + 2;
        do {
          if(!connectFlag){
            gww.connectToServer();
            connectFlag = true;
          }
          winnerResponses = gww.getWinners();
          if(winnerResponses.size() > 0) break;
        } while((System.nanoTime() / 1000000000) < timeoutConnectWS);
        gww.forceClose();

        if(winnerResponses.size() > 0) {
          //transfer to winner page

          List<WinnerResponse> japan = new ArrayList<>();
          List<WinnerResponse> meizu = new ArrayList<>();
          List<WinnerResponse> macbook = new ArrayList<>();
          List<WinnerResponse> phabplus = new ArrayList<>();
          List<WinnerResponse> fujifilm = new ArrayList<>();

          for (WinnerResponse prize : winnerResponses) {
            if(prize.getPrizeName().contains(MEIZU_WINNERS_KEY_PREFIX)) {
              meizu.add(prize);
            } else if(prize.getPrizeName().contains(MACBOOK_WINNERS_KEY_PREFIX)) {
              macbook.add(prize);
            } else if(prize.getPrizeName().contains(LENOVO_WINNERS_KEY_PREFIX)) {
              phabplus.add(prize);
            } else if(prize.getPrizeName().contains(FUJIFILM_WINNERS_KEY_PREFIX)) {
              fujifilm.add(prize);
            } else if(prize.getPrizeName().contains(JAPAN_WINNERS_KEY_PREFIX)) {
              japan.add(prize);
            }
          }
          request.setAttribute("japan", japan);
          request.setAttribute("meizu", meizu);
          request.setAttribute("macbook", macbook);
          request.setAttribute("fujifilm", fujifilm);
          request.setAttribute("phabplus", phabplus);

          LOGGER.info("Show winner page");
          viewHandler = WINNER_PAGE;
        } else {
          viewHandler = RAFFLE_PAGE;
          LOGGER.finer("Send user to Raffling page");
        }
      }
      request.setAttribute("viewPage", viewHandler);
      request.getRequestDispatcher("/WEB-INF/scaffold.jsp").forward(request, response);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE,
          "Error happened when accessing method: index on class: IndexViewController: " + e
              .getMessage(), e);
    }

    request.getRequestDispatcher("/WEB-INF/standby.jsp");
  }
}

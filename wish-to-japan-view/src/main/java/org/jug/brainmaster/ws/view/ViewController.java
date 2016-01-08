package org.jug.brainmaster.ws.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jug.brainmaster.model.response.WinnerResponse;
import org.jug.brainmaster.ws.startup.ApplicationConfig;

import com.google.gson.Gson;

@WebServlet(name = "viewController", urlPatterns = "/pages/*")
public class ViewController extends HttpServlet {
  private static final long serialVersionUID = 1253118814719512793L;

  private static final String COUNTDOWN_PAGE = "countdown";

  private static final String RAFFLE_PAGE = "raffle";
  private static final String WINNER_PAGE = "winner";
  private static final String WINNER_1_PAGE = "winner1";
  private static final String FUJIFILM_WINNERS_KEY_PREFIX = "FUJIFILM_";
  private static final String JAPAN_WINNERS_KEY_PREFIX = "JPN_TICKET_";
  private static final String LENOVO_WINNERS_KEY_PREFIX = "LENOVO_";
  private static final String MACBOOK_WINNERS_KEY_PREFIX = "MACBOOK_";
  private static final String MEIZU_WINNERS_KEY_PREFIX = "MEIZU_";

  @Inject
  private Logger log;

  @Inject
  private ApplicationConfig applicationConfig;

  @Inject
  private Gson gson;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String viewHandler = "";
    try {
      Date startDate = applicationConfig.getStartDate();
      Date now = new Date();
      if (now.before(startDate)) {
        viewHandler = COUNTDOWN_PAGE;
        request.setAttribute("countdownTime", (startDate.getTime() - now.getTime()) / 1000);
      } else {
        List<WinnerResponse> winnerResponses = applicationConfig.getWinnerResponses();
        if (winnerResponses == null) {
          log.log(Level.FINER, "Send user to Raffling page");
          viewHandler = RAFFLE_PAGE;
        } else {
          if (winnerResponses.size() > 0) {
            List<WinnerResponse> japan = new ArrayList<>();
            List<WinnerResponse> meizu = new ArrayList<>();
            List<WinnerResponse> macbook = new ArrayList<>();
            List<WinnerResponse> phabplus = new ArrayList<>();
            List<WinnerResponse> fujifilm = new ArrayList<>();

            for (WinnerResponse prize : winnerResponses) {
              if (prize.getPrizeName().contains(MEIZU_WINNERS_KEY_PREFIX)) {
                meizu.add(prize);
              } else if (prize.getPrizeName().contains(MACBOOK_WINNERS_KEY_PREFIX)) {
                macbook.add(prize);
              } else if (prize.getPrizeName().contains(LENOVO_WINNERS_KEY_PREFIX)) {
                phabplus.add(prize);
              } else if (prize.getPrizeName().contains(FUJIFILM_WINNERS_KEY_PREFIX)) {
                fujifilm.add(prize);
              } else if (prize.getPrizeName().contains(JAPAN_WINNERS_KEY_PREFIX)) {
                japan.add(prize);
              }
            }
            request.setAttribute("japan", japan);
            request.setAttribute("meizu", meizu);
            request.setAttribute("macbook", macbook);
            request.setAttribute("fujifilm", fujifilm);
            request.setAttribute("phabplus", phabplus);

            log.log(Level.FINER, "Show winner page");
            viewHandler = WINNER_PAGE;
          }
        }
      }
      request.setAttribute("viewPage", viewHandler);
      request.getRequestDispatcher("/WEB-INF/scaffold.jsp").forward(request, response);

    } catch (Exception e) {
      log.log(Level.SEVERE,
          "Error happened when accessing method: index on class: IndexViewController: "
              + e.getMessage(),
              e);
    }
  }
}

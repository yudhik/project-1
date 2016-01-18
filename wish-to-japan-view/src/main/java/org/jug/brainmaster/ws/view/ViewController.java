package org.jug.brainmaster.ws.view;

import org.joda.time.DateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "viewController", urlPatterns = "/pages/*")
public class ViewController extends HttpServlet {
  private static final Logger LOGGER = Logger.getLogger(ViewController.class.getName());

  private static final String COUNTDOWN_PAGE = "countdown";
  private static final String RAFFLE_PAGE = "raffle";
  private static final String WINNER_PAGE = "winner";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String viewHandler = "";
    try {
      Date startDate = new DateTime(2016, 1, 1, 11, 0, 0).toDate();
      Date now = new Date();
      if (now.before(startDate)) {
        viewHandler = COUNTDOWN_PAGE;
        request.setAttribute("countdownTime", (startDate.getTime() - now.getTime()) / 1000);
        LOGGER.finer("Jam: " + request.getAttribute("countdownTime"));
      } else {
        viewHandler = RAFFLE_PAGE;
        LOGGER.finer("Send user to Raffling page");
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

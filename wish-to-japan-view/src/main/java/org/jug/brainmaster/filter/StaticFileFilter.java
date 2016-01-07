package org.jug.brainmaster.filter;

import org.jug.brainmaster.ws.startup.ApplicationConfig;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StaticFileFilter implements Filter {
  private RequestDispatcher defaultRequestDispatcher;
  @Inject
  private Logger log;

  @Inject
  private ApplicationConfig applicationConfig;

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    String path = req.getRequestURI().substring(req.getContextPath().length());

    if (path.startsWith("/_asset")) {
      log.info("do nothing because path:" + path.toString());
      //do nothing, let it resolve itself
    } else {
      if(path.startsWith("/pemenang-periode-1")) {
        request.setAttribute("REQUEST_WINNER_1_PAGE", true);
      }
      log.log(Level.INFO, "MASUK PAGE");
      Boolean useWss = applicationConfig.useWss();
      String viewHost = applicationConfig.getViewHost();
      String logicHost = applicationConfig.getLogicHost();
      int winnerClaimTimeout = applicationConfig.getWinnerTimeout();
      request.setAttribute("viewHost", viewHost);
      request.setAttribute("logicHost", logicHost);
      request.setAttribute("useWss", useWss);
      request.setAttribute("winnerClaimTimeout", winnerClaimTimeout);
      request.getRequestDispatcher("/pages" + path).forward(request, response);
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
//    this.defaultRequestDispatcher =
//        filterConfig.getServletContext().getNamedDispatcher("staticFileFilter");
  }
}  

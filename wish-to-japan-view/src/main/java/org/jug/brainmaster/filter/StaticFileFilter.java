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

  @SuppressWarnings("unused")
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
    log.log(Level.FINEST, "Path:" + path);
    if (path.equals("/")) {
      log.log(Level.FINEST, "MASUK PAGE");
      Boolean useWss = applicationConfig.useWss();
      String viewHost = applicationConfig.getViewHost();
      String logicHost = applicationConfig.getLogicHost();
      int winnerClaimTimeout = applicationConfig.getWinnerTimeout();
      request.setAttribute("viewHost", viewHost);
      request.setAttribute("logicHost", logicHost);
      request.setAttribute("useWss", useWss);
      request.setAttribute("winnerClaimTimeout", winnerClaimTimeout);
      request.getRequestDispatcher("/pages/").forward(request, response);
    } else if (path.equals("/pemenang-periode-1")) {
      log.log(Level.FINEST, "MASUK PAGE PERIODE 1");
      request.getRequestDispatcher("/static/").forward(request, response);
    } else {
      chain.doFilter(request, response);
    }
  }


  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    this.defaultRequestDispatcher =
        filterConfig.getServletContext().getNamedDispatcher("staticFileFilter");
  }
}

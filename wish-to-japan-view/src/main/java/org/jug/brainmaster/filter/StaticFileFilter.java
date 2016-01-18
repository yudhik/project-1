package org.jug.brainmaster.filter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class StaticFileFilter implements Filter {
  private RequestDispatcher defaultRequestDispatcher;
  @Inject
  private Logger log;

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
      log.log(Level.INFO, "MASUK PAGE");
      String useWss = System.getProperty("useWss", "true");
      String viewHost = System.getProperty("viewHost", "localhost:8080");
      String logicHost = System.getProperty("logicHost", "localhost:8181");
      request.setAttribute("viewHost", viewHost);
      request.setAttribute("logicHost", logicHost);
      request.setAttribute("useWss", useWss);
      request.getRequestDispatcher("/pages" + path).forward(request, response);
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
//    this.defaultRequestDispatcher =
//        filterConfig.getServletContext().getNamedDispatcher("staticFileFilter");
  }
}  

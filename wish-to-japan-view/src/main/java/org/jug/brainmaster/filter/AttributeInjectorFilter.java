package org.jug.brainmaster.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AttributeInjectorFilter implements Filter{
  private static final Logger LOGGER = Logger.getLogger(AttributeInjectorFilter.class.getName());

  @Override
  public void destroy() {

  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    String useWss = System.getProperty("useWss", "true");
    String viewHost = System.getProperty("viewHost", "localhost:8080");
    String logicHost = System.getProperty("logicHost", "localhost:8181");
    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    httpServletRequest.setAttribute("viewHost", viewHost);
    httpServletRequest.setAttribute("logicHost", logicHost);
    httpServletRequest.setAttribute("useWss", useWss);
    filterChain.doFilter(servletRequest, servletResponse);
  }
}

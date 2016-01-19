package org.jug.brainmaster.websocket;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.ServletContext;

@ApplicationScoped
public class ApplicationResources {
  private ServletContext servletContext;

  public void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  @Produces
  public Boolean isEnded() {
    return (Boolean) servletContext.getAttribute("isEnded");
  }
}

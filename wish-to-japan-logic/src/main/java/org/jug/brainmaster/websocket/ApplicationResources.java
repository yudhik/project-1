package org.jug.brainmaster.websocket;

import org.jug.brainmaster.model.response.WinnerResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.ServletContext;
import java.util.List;

@ApplicationScoped
public class ApplicationResources {

  private ServletContext servletContext;

  @Produces
  @SuppressWarnings("unchecked")
  public List<WinnerResponse> getAllWinners() {
    return (List<WinnerResponse>) servletContext.getAttribute("winners");
  }

  @Produces
  public Boolean isEnded() {
    return (Boolean) servletContext.getAttribute("isEnded");
  }

  public void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
  }
}

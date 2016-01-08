package org.jug.brainmaster.websocket;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.ServletContext;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.jug.brainmaster.model.response.WinnerResponse;

@ApplicationScoped
public class ApplicationResources {

  private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
  private ServletContext servletContext;

  @Produces
  @SuppressWarnings("unchecked")
  public List<WinnerResponse> getAllWinners() {
    return (List<WinnerResponse>) servletContext.getAttribute("winners");
  }

  @Produces
  public Validator getValidator() {
    return VALIDATOR_FACTORY.getValidator();
  }

  @Produces
  public Boolean isEnded() {
    return (Boolean) servletContext.getAttribute("isEnded");
  }

  public void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
  }
}

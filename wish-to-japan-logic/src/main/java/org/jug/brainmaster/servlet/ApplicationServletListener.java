package org.jug.brainmaster.servlet;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jug.brainmaster.machine.RafleMachine;
import org.jug.brainmaster.websocket.ApplicationResources;

public class ApplicationServletListener implements ServletContextListener {

  @Inject
  private Logger log;

  @Inject
  private RafleMachine rafleMachine;

  @Inject
  private ApplicationResources applicationResources;

  @Override
  public void contextDestroyed(ServletContextEvent sce) {

  }

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    applicationResources.setServletContext(sce.getServletContext());
    sce.getServletContext().setAttribute("waitingToStart",true);
    try {
      rafleMachine.start(sce.getServletContext());
    } catch (Exception e) {
      log.log(Level.SEVERE, "can not initialize machine", e);
    }
  }

}

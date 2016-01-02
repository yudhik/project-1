package org.jug.brainmaster.servlet;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jug.brainmaster.machine.RafleMachine;

public class ApplicationServletListener implements ServletContextListener {

  @Inject
  private Logger log;

  @Inject
  private RafleMachine rafleMachine;

  @Override
  public void contextDestroyed(ServletContextEvent sce) {

  }

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    try {
      rafleMachine.start();
    } catch (Exception e) {
      log.log(Level.SEVERE, "can not initialize machine", e);
    }
  }

}

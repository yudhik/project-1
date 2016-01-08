package org.jug.brainmaster.ws.startup;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jug.brainmaster.mdb.SessionObserver;

public class ServerSocketListener implements ServletContextListener {

  @Inject
  private Logger log;
  @Inject
  private SessionObserver sessionObserver;

  @Inject
  ApplicationConfig applicationConfig;

  @Override
  public void contextDestroyed(ServletContextEvent arg0) {
    // TODO Auto-generated method stub
  }

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    createSession();
  }

  private void createSession() {
    log.info("trying to establish session to server");
    this.sessionObserver.getServerSession();
  }

}

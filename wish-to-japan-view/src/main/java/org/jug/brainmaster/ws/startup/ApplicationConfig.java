package org.jug.brainmaster.ws.startup;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;

//TODO apaan lagi ini :( stress ya si willy
@ManagedBean(eager = true)
@ApplicationScoped
@Named
public class ApplicationConfig {
  private static final String JBOSS_CONFIG_DIRECTORY_KEY = "jboss.server.config.dir";
  private static final String APPLICATION_CONFIG_FILENAME = "wish-to-japan-config.properties";
  private Properties properties;

  @Inject
  private Logger log;

  private final String START_DATE = "wish.to.japan.start.date";
  private final String DATE_FORMAT = "wish.to.japan.date.format";
  private final String WINNER_TIMEOUT = "wish.to.japan.winner.timeout";
  private final String USE_WSS = "wish.to.japan.use.wss";
  private final String VIEW_HOST = "wish.to.japan.view.host.url";
  private final String LOGIC_HOST = "wish.to.japan.logic.host.url";

  public String getLogicHost() {
    return properties.getProperty(LOGIC_HOST);
  }

  public Date getStartDate() {
    Date startDate = new Date();
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat(properties.getProperty(DATE_FORMAT));
      startDate = dateFormat.parse(properties.getProperty(START_DATE));
    } catch (Exception e) {
      log.log(Level.SEVERE, "Error when parsing date from config", e);
    }
    return startDate;
  }

  public String getViewHost() {
    return properties.getProperty(VIEW_HOST);
  }

  public int getWinnerTimeout() {
    return Integer.parseInt(properties.getProperty(WINNER_TIMEOUT));
  }

  @PostConstruct
  private void postConstruct () {
    try {
      properties = new Properties();
      FileReader reader = new FileReader(
          System.getProperty(JBOSS_CONFIG_DIRECTORY_KEY) + File.separator
          + APPLICATION_CONFIG_FILENAME);
      properties.load(reader);
      log.log(Level.SEVERE, "" + properties.size());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  public boolean useWss() {
    return Boolean.parseBoolean(properties.getProperty(USE_WSS));
  }
}

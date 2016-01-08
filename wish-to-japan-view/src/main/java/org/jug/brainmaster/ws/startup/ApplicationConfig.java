package org.jug.brainmaster.ws.startup;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jug.brainmaster.model.response.WinnerResponse;
import org.jug.brainmaster.ws.ws.GetWinnersWebsocket;

@Named
@ApplicationScoped
public class ApplicationConfig {
  private static final String JBOSS_CONFIG_DIRECTORY_KEY = "jboss.server.config.dir";
  private static final String APPLICATION_CONFIG_FILENAME = "wish-to-japan-config.properties";
  private static final String START_DATE = "wish.to.japan.start.date";
  private static final String DATE_FORMAT = "wish.to.japan.date.format";
  private static final String WINNER_TIMEOUT = "wish.to.japan.winner.timeout";
  private static final String USE_WSS = "wish.to.japan.use.wss";
  private static final String VIEW_HOST = "wish.to.japan.view.host.url";
  private static final String LOGIC_HOST = "wish.to.japan.logic.host.url";

  private Properties properties;
  private List<WinnerResponse> winnerResponses;

  @Inject
  private Logger log;

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

  public List<WinnerResponse> getWinnerResponses() {
    if(winnerResponses == null || winnerResponses.size() == 0) {
      GetWinnersWebsocket winnerWebSocketClient = new GetWinnersWebsocket(getLogicHost());
      long timeoutConnectWS = 300000000L;
      try {
        winnerWebSocketClient.connectToServer();
      } catch (Exception e) {
        log.log(Level.SEVERE, "can not connect to winner server, logic host : "+ getLogicHost(), e);
        return null;
      }
      long waitingForResponseStart = System.nanoTime();
      while ((System.nanoTime() - waitingForResponseStart) < timeoutConnectWS && winnerResponses == null) {
        List<WinnerResponse> result = winnerWebSocketClient.getWinners();
        log.log(Level.INFO, "" + winnerWebSocketClient.getWinners());
        if(result != null && result.size() > 0) {
          winnerResponses = result;
        } else {
          winnerResponses = null;
        }
      }
    }
    if(winnerResponses != null) {
      log.log(Level.FINER, "winner size : "+ winnerResponses.size());
    } else {
      log.log(Level.FINER, "winner is null ");
    }
    return winnerResponses;
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

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
  private GetWinnersWebsocket winnerWebSocketClient;
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
    log.log(Level.FINER, "try get winner from response");
    if (winnerResponses == null || winnerResponses.size() == 0) {
      log.log(Level.FINER, "winner still null");
      long timeoutConnectWS = 10000000L;
      try {
        if (winnerWebSocketClient.getSession() == null || !winnerWebSocketClient.getSession()
            .isOpen()) {
          winnerWebSocketClient.connectToServer();
        }
      } catch (Exception e) {
        log.log(Level.SEVERE, "can not connect to winner server, logic host : " + getLogicHost(),
            e);
        return null;
      }
      long waitingForResponseStart = System.nanoTime();
      while ((System.nanoTime() - waitingForResponseStart) < timeoutConnectWS
          && winnerResponses == null) {
        log.log(Level.FINER, "getting result from socket");
        List<WinnerResponse> result = winnerWebSocketClient.getWinners();
        if (result != null && result.size() > 0) {
          winnerResponses = result;
        } else {
          winnerResponses = null;
        }
      }
    }
    if (winnerResponses != null) {
      log.log(Level.FINER, "winner size : " + winnerResponses.size());
    } else {
      log.log(Level.FINER, "winner is null ");
    }
    return winnerResponses;
  }

  public int getWinnerTimeout() {
    return Integer.parseInt(properties.getProperty(WINNER_TIMEOUT));
  }

  //  public GetWinnersWebsocket getWinnerWebSocketClient() {
  //    return winnerWebSocketClient;
  //  }

  @PostConstruct
  private void postConstruct() {
    try {
      properties = new Properties();
      FileReader reader = new FileReader(
          System.getProperty(JBOSS_CONFIG_DIRECTORY_KEY) + File.separator
              + APPLICATION_CONFIG_FILENAME);
      properties.load(reader);
      winnerWebSocketClient = new GetWinnersWebsocket(getLogicHost());
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

package org.jug.brainmaster.ws.ws;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ejb.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jug.brainmaster.ws.response.LogicWinnerResponse;
import org.jug.brainmaster.ws.response.UserResponse;
import org.jug.brainmaster.ws.response.WinnerResponse;

//import com.google.gson.Gson;
//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.JsonNode;
//import com.mashape.unirest.http.Unirest;
//import com.mashape.unirest.request.HttpRequest;

@Singleton
public class WinnersData {
  private WinnerResponse winnerResponse;
  private long lastUpdatedAt;
  private String host = System.getProperty("logicHost");

  private Integer session = 1; // HARDCODED
  private static final String FUJIFILM_WINNERS_KEY_PREFIX = "FUJIFILM_";
  private static final String JAPAN_WINNERS_KEY_PREFIX = "JPN_TICKET_";
  private static final String LENOVO_WINNERS_KEY_PREFIX = "LENOVO_";
  private static final String MACBOOK_WINNERS_KEY_PREFIX = "MACBOOK_";

  private static final String MEIZU_WINNERS_KEY_PREFIX = "MEIZU_";

  private void assignWinners(LogicWinnerResponse response) {
    if (this.winnerResponse == null) {
      this.winnerResponse = new WinnerResponse();
    }

    if (response.isValid()) {
      Map<String, List<UserResponse>> mapOfWinners = response.getWinners();

      List<UserResponse> fujifilmWinners =
          mapOfWinners.get(WinnersData.FUJIFILM_WINNERS_KEY_PREFIX + this.session);
      List<UserResponse> japanWinners =
          mapOfWinners.get(WinnersData.JAPAN_WINNERS_KEY_PREFIX + this.session);
      List<UserResponse> lenovoWinners =
          mapOfWinners.get(WinnersData.LENOVO_WINNERS_KEY_PREFIX + this.session);
      List<UserResponse> macbookWinners =
          mapOfWinners.get(WinnersData.MACBOOK_WINNERS_KEY_PREFIX + this.session);
      List<UserResponse> meizuWinners =
          mapOfWinners.get(WinnersData.MEIZU_WINNERS_KEY_PREFIX + this.session);

      this.winnerResponse.setFujifilmWinners(fujifilmWinners);
      this.winnerResponse.setJapanWinners(japanWinners);
      this.winnerResponse.setLenovoWinners(lenovoWinners);
      this.winnerResponse.setMacbookWinners(macbookWinners);
      this.winnerResponse.setMeizuWinners(meizuWinners);
    } else {
      this.winnerResponse.setValid(false);
    }
  }

  @Produces(MediaType.APPLICATION_JSON)
  public WinnerResponse getAllWinners() throws Exception {
    Date aDay = new Date(this.lastUpdatedAt + TimeUnit.MILLISECONDS.convert(1L, TimeUnit.DAYS));

    if (new Date().after(aDay)) {
//      HttpRequest request = Unirest.get("//" + this.host + "/show-winners");
//      HttpResponse<JsonNode> jsonResponse = request.asJson();
//      Gson gson = new Gson();
//      String responseJsonString = jsonResponse.getBody().toString();
//
//      assignWinners(gson.fromJson(responseJsonString, LogicWinnerResponse.class));
//      this.lastUpdatedAt = new Date().getTime();
    }
    return this.winnerResponse;
  }
}

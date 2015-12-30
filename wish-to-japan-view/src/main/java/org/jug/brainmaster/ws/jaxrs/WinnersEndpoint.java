package org.jug.brainmaster.ws.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jug.brainmaster.ws.response.WinnerResponse;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;

public class WinnersEndpoint {
  private String host = System.getProperty("logicHost");

  @GET
  @Produces("application/json")
  public WinnerResponse getMethod() throws Exception {
    HttpRequest request = Unirest.get("http://" + this.host + "/api/show-winners");
    HttpResponse<JsonNode> jsonResponse = request.asJson();
    Gson gson = new Gson();

    return gson.fromJson(jsonResponse.getBody().toString(), WinnerResponse.class);
  }
}

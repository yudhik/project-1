//package org.jug.brainmaster.ws.jaxrs;
//
//import javax.ws.rs.FormParam;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
//import javax.ws.rs.core.MediaType;
//
//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.Unirest;
//import com.mashape.unirest.request.HttpRequest;
//
//import java.util.logging.Logger;
//
//public class ClaimPrize {
//  private static final Logger LOGGER = Logger.getLogger(ClaimPrize.class.getName());
//
//  private String host = System.getProperty("logicHost");
//
//  @POST
//  @Produces(MediaType.TEXT_PLAIN)
//  public boolean claimPrize(@FormParam("e") String email) {
//    HttpRequest request =
//        Unirest.get("http://" + this.host + "/api/claim-winner").queryString("email", email);
//
//    try {
//      HttpResponse<String> response = request.asString();
////      LOGGER.info(response.toString());
//      if (response.getBody().equals("true")) {
//        return true;
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    return false;
//  }
//}

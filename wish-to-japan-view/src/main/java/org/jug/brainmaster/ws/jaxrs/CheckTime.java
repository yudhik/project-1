package org.jug.brainmaster.ws.jaxrs;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/check-time")
public class CheckTime {
  private static final Date SESSION_START_TIME;


  static {
    // REMINDER: MONTH IS 0-based.
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.set(2015, 11, 17, 11, 00, 00);

    SESSION_START_TIME = calendar.getTime();
    calendar = null;
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public long getRemainingTimeInSeconds() {
    String bypassTime = System.getProperty("bypassTime");

    if(bypassTime.equals("true")){
      return (CheckTime.SESSION_START_TIME.getTime() - new Date().getTime()) / 1000;
    } else {
      return 0;
    }
  }
}

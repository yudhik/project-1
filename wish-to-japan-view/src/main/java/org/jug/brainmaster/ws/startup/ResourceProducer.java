package org.jug.brainmaster.ws.startup;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Singleton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ResourceProducer {

  @Produces
  @Singleton
  public Gson produceGson() {
    GsonBuilder builder = new GsonBuilder();
    return builder.create();
  }

  @Produces
  public Logger produceLog(InjectionPoint injectionPoint) {
    return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
  }
}

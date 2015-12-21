package org.jug.brainmaster.ws.jaxrs;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class Application extends javax.ws.rs.core.Application {
  // let the container scan for @Path resources
}

package org.jug.brainmaster.ejb;

import org.jug.brainmaster.model.response.GameMessage;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

@Singleton
@LocalBean
public class GameMessageListenerServiceBean {

  private static final List<Session> SESSION_LISTENERS = new ArrayList<Session>();

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void addListener(Session session) throws Exception {
    SESSION_LISTENERS.add(session);
  }

  public void publishEvent(@Observes GameMessage message) throws Exception {
    for (Session session : SESSION_LISTENERS) {
      if (session.isOpen()) {
        session.getAsyncRemote().sendText(message.toJSON());
      }
    }
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void removeListener(Session session) throws Exception {
    SESSION_LISTENERS.remove(session);
  }

}

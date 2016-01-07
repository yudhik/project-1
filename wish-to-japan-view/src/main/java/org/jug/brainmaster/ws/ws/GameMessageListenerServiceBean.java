package org.jug.brainmaster.ws.ws;

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
  private static final List<Session> SESSION_LIST = new ArrayList<>();

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void attachSession(Session session) throws Exception {
    SESSION_LIST.add(session);
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void detachSession(Session session) throws Exception {
    SESSION_LIST.remove(session);
  }

  public void publishMessage(@Observes GameMessage gameMessage) throws Exception {
    for (Session session : SESSION_LIST) {
      if (session.isOpen()) {
        session.getAsyncRemote().sendText(gameMessage.toJSON());
      }
    }
  }
}

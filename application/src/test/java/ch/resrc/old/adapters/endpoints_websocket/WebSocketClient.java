package ch.resrc.old.adapters.endpoints_websocket;

import io.vavr.collection.*;

import javax.websocket.*;

@ClientEndpoint
public class WebSocketClient {

  private List<Session> sessions = List.empty();
  private List<String> messages = List.empty();

  @OnOpen
  public void onOpen(Session session) {
    sessions = sessions.append(session);
  }

  @OnClose
  public void onClose(Session session) {
    sessions = sessions.remove(session);
  }

  @OnMessage
  public void onMessage(String message) {
    messages = messages.append(message);
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    messages = messages.append(throwable.getMessage());
    sessions = sessions.remove(session);
  }

  public List<String> getMessages() {
    return messages;
  }
}

package ch.resrc.tichu.adapters.endpoints_websocket;

import io.vavr.collection.List;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

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

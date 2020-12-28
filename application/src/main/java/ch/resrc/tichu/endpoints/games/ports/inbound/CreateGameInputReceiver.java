package ch.resrc.tichu.endpoints.games.ports.inbound;

public interface CreateGameInputReceiver {

  void onMessage(String message);

}

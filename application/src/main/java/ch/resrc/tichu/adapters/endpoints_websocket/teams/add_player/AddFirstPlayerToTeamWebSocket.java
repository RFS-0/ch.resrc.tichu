package ch.resrc.tichu.adapters.endpoints_websocket.teams.add_player;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses.Teams;
import ch.resrc.tichu.adapters.endpoints_websocket.teams.dto.IntendedPlayerAdditionDto;
import ch.resrc.tichu.capabilities.errorhandling.ProblemCatalogue;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.use_cases.teams.add_player.add_first_player.ports.inbound.AddFirstPlayerToTeam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(Teams.ADD_FIRST_PLAYER_TO_TEAM)
public class AddFirstPlayerToTeamWebSocket {

  private static final Logger LOG = LoggerFactory.getLogger(AddFirstPlayerToTeamWebSocket.class);

  private final AddFirstPlayerToTeam addFirstPlayerToTeam;

  private final ProblemCatalogue problemCatalogue;
  private final Json json;

  public AddFirstPlayerToTeamWebSocket(
    AddFirstPlayerToTeam addFirstPlayerToTeam,
    ProblemCatalogue problemCatalogue,
    Json json
  ) {
    this.addFirstPlayerToTeam = addFirstPlayerToTeam;
    this.problemCatalogue = problemCatalogue;
    this.json = json;
  }

  @OnOpen
  public void onOpen(Session session) {
    LOG.info("Successfully opened connection");
  }

  public void onMessage(IntendedPlayerAdditionDto input) {
    LOG.info("Got message");
  }

  @OnMessage
  public void onMessage(String message) {
    LOG.info("Got Message");
    //final var input = json.parse(message, IntendedPlayerAdditionDto.class);
   // onMessage(input);
  }
}

package ch.resrc.tichu.adapters.endpoints_websocket.teams.add_player;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses.Teams;
import ch.resrc.tichu.adapters.endpoints_websocket.teams.dto.IntendedPlayerAdditionDto;
import ch.resrc.tichu.capabilities.errorhandling.ProblemCatalogue;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.use_cases.teams.add_player.add_second_player.ports.inbound.AddSecondPlayerToTeam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(Teams.ADD_SECOND_PLAYER_TO_TEAM)
public class AddSecondPlayerToTeamWebSocket {

  private static final Logger LOG = LoggerFactory.getLogger(AddSecondPlayerToTeamWebSocket.class);

  private final AddSecondPlayerToTeam addSecondPlayerToTeam;

  private final ProblemCatalogue problemCatalogue;
  private final Json json;

  public AddSecondPlayerToTeamWebSocket(
    AddSecondPlayerToTeam addSecondPlayerToTeam,
    ProblemCatalogue problemCatalogue,
    Json json
  ) {
    this.addSecondPlayerToTeam = addSecondPlayerToTeam;
    this.problemCatalogue = problemCatalogue;
    this.json = json;
  }

  public void onMessage(IntendedPlayerAdditionDto input) {
  }

  @OnMessage
  public void onMessage(String message) {
    final var input = json.parse(message, IntendedPlayerAdditionDto.class);
    onMessage(input);
  }
}

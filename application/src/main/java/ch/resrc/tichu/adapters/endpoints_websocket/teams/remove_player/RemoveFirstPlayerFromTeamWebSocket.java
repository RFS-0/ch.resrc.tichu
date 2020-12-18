package ch.resrc.tichu.adapters.endpoints_websocket.teams.remove_player;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses.Teams;
import ch.resrc.tichu.adapters.endpoints_websocket.teams.dto.TeamIdDto;
import ch.resrc.tichu.capabilities.errorhandling.ProblemCatalogue;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.use_cases.teams.remove_player.remove_first_player_from_team.ports.inbound.RemoveFirstPlayerFromTeam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(Teams.REMOVE_FIRST_PLAYER_FROM_TEAM)
public class RemoveFirstPlayerFromTeamWebSocket {

  private static final Logger LOG = LoggerFactory.getLogger(RemoveFirstPlayerFromTeamWebSocket.class);

  private final RemoveFirstPlayerFromTeam removeFirstPlayerFromTeam;

  private final ProblemCatalogue problemCatalogue;
  private final Json json;

  public RemoveFirstPlayerFromTeamWebSocket(
    RemoveFirstPlayerFromTeam removeFirstPlayerFromTeam,
    ProblemCatalogue problemCatalogue,
    Json json
  ) {
    this.removeFirstPlayerFromTeam = removeFirstPlayerFromTeam;
    this.problemCatalogue = problemCatalogue;
    this.json = json;
  }

  public void onMessage(TeamIdDto input) {
  }

  @OnMessage
  public void onMessage(String message) {
    final var input = json.parse(message, TeamIdDto.class);
    onMessage(input);
  }
}

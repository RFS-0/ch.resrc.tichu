package ch.resrc.tichu.adapters.endpoints_websocket.teams.remove_player;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses.Teams;
import ch.resrc.tichu.adapters.endpoints_websocket.teams.dto.TeamIdDto;
import ch.resrc.tichu.capabilities.errorhandling.ProblemCatalogue;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.use_cases.teams.remove_player.remove_second_player_from_team.ports.inbound.RemoveSecondPlayerFromTeam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(Teams.REMOVE_SECOND_PLAYER_FROM_TEAM)
public class RemoveSecondPlayerFromTeamWebSocket {

  private static final Logger LOG = LoggerFactory.getLogger(RemoveSecondPlayerFromTeamWebSocket.class);

  private final RemoveSecondPlayerFromTeam removeSecondPlayerFromTeam;

  private final ProblemCatalogue problemCatalogue;
  private final Json json;

  public RemoveSecondPlayerFromTeamWebSocket(
    RemoveSecondPlayerFromTeam removeSecondPlayerFromTeam,
    ProblemCatalogue problemCatalogue,
    Json json
  ) {
    this.removeSecondPlayerFromTeam = removeSecondPlayerFromTeam;
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

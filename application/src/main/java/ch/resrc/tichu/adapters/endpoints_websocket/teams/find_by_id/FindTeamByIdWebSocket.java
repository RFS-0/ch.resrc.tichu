package ch.resrc.tichu.adapters.endpoints_websocket.teams.find_by_id;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses.Teams;
import ch.resrc.tichu.capabilities.errorhandling.ProblemCatalogue;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.use_cases.teams.find_by_id.ports.inbound.FindTeamById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(Teams.FIND_BY_ID)
public class FindTeamByIdWebSocket {

  private static final Logger LOG = LoggerFactory.getLogger(FindTeamByIdWebSocket.class);

  private final FindTeamById findTeamById;

  private final ProblemCatalogue problemCatalogue;
  private final Json json;

  public FindTeamByIdWebSocket(
    FindTeamById findTeamById,
    ProblemCatalogue problemCatalogue,
    Json json
  ) {
    this.findTeamById = findTeamById;
    this.problemCatalogue = problemCatalogue;
    this.json = json;
  }

  @OnMessage
  public void onMessage(String message) {

  }
}

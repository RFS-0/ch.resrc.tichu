package ch.resrc.tichu.endpoints.games.ports.outbound;

import ch.resrc.tichu.adapters.websocket.games.dto.GameDto;
import ch.resrc.tichu.capabilities.json.JsonBody;
import ch.resrc.tichu.domain.value_objects.Id;

public interface CreatedGameOutputSender {

  void send(Id userId, JsonBody<GameDto> createdGame);

}

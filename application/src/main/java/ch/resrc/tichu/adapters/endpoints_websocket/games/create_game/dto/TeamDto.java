package ch.resrc.tichu.adapters.endpoints_websocket.games.create_game.dto;

import ch.resrc.tichu.adapters.endpoints_websocket.teams.dto.PlayerDto;
import ch.resrc.tichu.use_cases.teams.ports.output.TeamDocument;
import io.vavr.collection.Set;

import java.util.function.Consumer;

import static ch.resrc.tichu.capabilities.functional.NullSafe.nullSafeToString;

public class TeamDto {

  public String id;
  public String name;
  public PlayerDto firstPlayer;
  public PlayerDto secondPlayer;

  public TeamDto() {
    // for JSON deserialization
  }

  public TeamDto(Consumer<TeamDto> initializer) {
    initializer.accept(this);
  }

  public static TeamDto fromDocument(TeamDocument doc) {
    return new TeamDto(x -> {
      x.id = doc.id().value().toString();
      x.name = nullSafeToString(doc.name());
      x.firstPlayer = PlayerDto.fromDocument(doc.firstPlayer());
      x.secondPlayer = PlayerDto.fromDocument(doc.secondPlayer());
    });
  }

  public static Set<TeamDto> fromDocuments(Set<TeamDocument> docs) {
    return docs.map(TeamDto::fromDocument);
  }
}

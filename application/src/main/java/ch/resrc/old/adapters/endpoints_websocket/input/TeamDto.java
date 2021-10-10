package ch.resrc.old.adapters.endpoints_websocket.input;

import ch.resrc.old.use_cases.common.output.*;
import io.vavr.collection.*;

import java.util.function.*;

import static ch.resrc.old.capabilities.functional.NullSafe.*;

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

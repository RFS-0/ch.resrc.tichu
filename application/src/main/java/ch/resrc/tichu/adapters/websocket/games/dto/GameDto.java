package ch.resrc.tichu.adapters.websocket.games.dto;

import static ch.resrc.tichu.capabilities.functional.NullSafe.nullSafeToString;

import ch.resrc.tichu.domain.value_objects.Round;
import ch.resrc.tichu.usecases.create_game.ports.documents.GameDocument;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;
import java.util.function.Consumer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDto {

  public String id;
  public String joinCode;
  public String team;
  public String otherTeam;
  public Set<Round> rounds;
  public String createdAt;
  public String finishedAt;

  public GameDto() {
    // for JSON deserialization
  }

  public GameDto(Consumer<GameDto> initializer) {

    initializer.accept(this);
  }

  public static GameDto fromDocument(GameDocument the) {

    return new GameDto(x -> {

      x.id = nullSafeToString(the.id());
      x.joinCode = nullSafeToString(the.joinCode());
      x.team = nullSafeToString(the.team());
      x.otherTeam = nullSafeToString(the.otherTeam());
    });
  }


}

package ch.resrc.tichu.adapters.endpoints_websocket.games.create_a_game.dto;

import ch.resrc.tichu.adapters.endpoints_rest.users.dto.UserDto;
import ch.resrc.tichu.adapters.endpoints_websocket.dto.TeamDto;
import ch.resrc.tichu.use_cases.games.create_a_game.ports.output.GameDocument;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.vavr.collection.Set;

import java.util.function.Consumer;

import static ch.resrc.tichu.capabilities.functional.NullSafe.nullSafeToString;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDto {

  public String id;
  public UserDto createdBy;
  public String joinCode;
  public Set<TeamDto> teams;
  public Set<RoundDto> rounds;
  public String createdAt;
  public String finishedAt;

  private GameDto() {
    // for deserialization
  }

  public GameDto(Consumer<GameDto> initializer) {
    initializer.accept(this);
  }

  public static GameDto fromDocument(GameDocument the) {
    return new GameDto(x -> {
      x.id = the.id().value().toString();
      x.joinCode = the.joinCode().value();
      x.createdBy = UserDto.fromDocument(the.createdBy());
      x.teams = TeamDto.fromDocuments(the.teams());
      x.rounds = RoundDto.fromRounds(the.rounds());
      x.createdAt = nullSafeToString(the.createdAt());
      x.finishedAt = nullSafeToString(the.finishedAt());
    });
  }
}

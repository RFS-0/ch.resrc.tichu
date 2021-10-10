package ch.resrc.old.adapters.endpoints_websocket.input;

import ch.resrc.old.adapters.endpoints_rest.users.dto.*;
import ch.resrc.old.use_cases.common.output.*;
import com.fasterxml.jackson.annotation.*;
import io.vavr.collection.*;

import java.util.function.*;

import static ch.resrc.old.capabilities.functional.NullSafe.*;

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

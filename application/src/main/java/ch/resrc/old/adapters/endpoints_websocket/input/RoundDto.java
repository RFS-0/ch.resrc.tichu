package ch.resrc.old.adapters.endpoints_websocket.input;

import ch.resrc.old.domain.value_objects.*;
import com.fasterxml.jackson.annotation.*;
import io.vavr.collection.*;

import java.util.function.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoundDto {

  private int roundNumber;
  private Map<Id, Integer> cardPoints;
  private Map<Id, Tichu> tichus;
  private Map<Id, Integer> ranks;
  private String startedAt;
  private String finishedAt;

  private RoundDto() {
    // for JSON deserialization
  }

  public RoundDto(Consumer<RoundDto> initializer) {
    initializer.accept(this);
  }

  public static RoundDto fromRound(Round round) {
    return new RoundDto(dto -> {
      dto.roundNumber = round.roundNumber().value();
      dto.cardPoints = round.cardPoints().values();
      dto.tichus = round.tichus().values();
      dto.ranks = round.ranks().values();
    });
  }

  public static Set<RoundDto> fromRounds(Set<Round> rounds) {
    return rounds.map(RoundDto::fromRound);
  }
}

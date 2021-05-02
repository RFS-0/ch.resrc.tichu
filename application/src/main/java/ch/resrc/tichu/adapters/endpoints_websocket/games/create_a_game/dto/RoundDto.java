package ch.resrc.tichu.adapters.endpoints_websocket.games.create_a_game.dto;

import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Round;
import ch.resrc.tichu.domain.value_objects.Tichu;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

import java.util.function.Consumer;

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

package ch.resrc.tichu.capabilities.endpoints.jsonfixtures;

import ch.resrc.tichu.adapters.endpoints_websocket.create_a_game.dto.IntendedGameDto;
import ch.resrc.tichu.capabilities.endpoints.JsonHabits;

import java.util.function.Consumer;

public class IntendedGameJson extends IntendedGameDto {

  IntendedGameJson(IntendedGameJson other) {
    super(other);
  }

  public IntendedGameJson but(Consumer<IntendedGameDto> modification) {
    var theCopy = new IntendedGameJson(this);
    modification.accept(theCopy);
    return theCopy;
  }

  @Override
  public String toString() {
    return JsonHabits.INSTANCE.asJson(this);
  }
}

package ch.resrc.tichu.testcapabilities.endpoints.jsonfixtures;

import ch.resrc.tichu.adapters.websocket.games.dto.IntendedGameDto;
import ch.resrc.tichu.testcapabilities.endpoints.JsonHabits;
import java.util.function.Consumer;

public class IntendedGameJson extends IntendedGameDto {

  public IntendedGameJson() {
    super();
  }

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

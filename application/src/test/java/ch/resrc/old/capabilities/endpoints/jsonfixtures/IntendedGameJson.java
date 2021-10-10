package ch.resrc.old.capabilities.endpoints.jsonfixtures;

import ch.resrc.old.adapters.endpoints_websocket.input.*;
import ch.resrc.old.capabilities.endpoints.*;

import java.util.function.*;

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

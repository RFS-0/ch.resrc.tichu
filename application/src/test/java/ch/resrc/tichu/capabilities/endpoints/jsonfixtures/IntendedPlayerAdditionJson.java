package ch.resrc.tichu.capabilities.endpoints.jsonfixtures;

import ch.resrc.tichu.adapters.endpoints_websocket.teams.dto.IntendedPlayerAdditionDto;
import ch.resrc.tichu.capabilities.endpoints.JsonHabits;

import java.util.function.Consumer;

public class IntendedPlayerAdditionJson extends IntendedPlayerAdditionDto {

  public IntendedPlayerAdditionJson(IntendedPlayerAdditionDto other) {
    super(other);
  }

  public IntendedPlayerAdditionJson but(Consumer<IntendedPlayerAdditionDto> modification) {
    var theCopy = new IntendedPlayerAdditionJson(this);
    modification.accept(theCopy);
    return theCopy;
  }

  @Override
  public String toString() {
    return JsonHabits.INSTANCE.asJson(this);
  }
}

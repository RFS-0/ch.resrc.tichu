package ch.resrc.old.adapters.endpoints_websocket.input;

import ch.resrc.old.use_cases.common.output.*;
import com.fasterxml.jackson.annotation.*;

import java.util.function.*;

import static ch.resrc.old.capabilities.functional.NullSafe.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerDto {

  public String id;
  public String name;

  public PlayerDto() {
    // for JSON deserialization
  }

  public PlayerDto(Consumer<PlayerDto> initializer) {
    initializer.accept(this);
  }

  public static PlayerDto fromDocument(PlayerDocument the) {
    if (the == null) {
      return null;
    }
    return new PlayerDto(x -> {
      x.id = nullSafeToString(the.id());
      x.name = nullSafeToString(the.name());
    });
  }
}

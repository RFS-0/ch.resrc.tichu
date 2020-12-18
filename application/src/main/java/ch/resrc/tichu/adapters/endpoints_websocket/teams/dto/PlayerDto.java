package ch.resrc.tichu.adapters.endpoints_websocket.teams.dto;

import ch.resrc.tichu.use_cases.ports.documents.PlayerDocument;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.function.Consumer;

import static ch.resrc.tichu.capabilities.functional.NullSafe.nullSafeToString;

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

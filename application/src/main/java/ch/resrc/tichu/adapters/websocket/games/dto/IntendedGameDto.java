package ch.resrc.tichu.adapters.websocket.games.dto;

import ch.resrc.tichu.adapters.javax_validation.IsConvertibleTo;
import ch.resrc.tichu.domain.value_objects.Id;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IntendedGameDto {

  @IsConvertibleTo(Id.class)
  public String userId;

  public IntendedGameDto() {
    // For JSON deserialization.
  }

  protected IntendedGameDto(IntendedGameDto other) {
    this.userId = other.userId;
  }
}

package ch.resrc.tichu.adapters.endpoints_websocket.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IntendedGameDto {

  public String createdBy;

  private IntendedGameDto() {
    // for deserialization
  }

  public IntendedGameDto(String createdBy) {
    this.createdBy = createdBy;
  }

  protected IntendedGameDto(IntendedGameDto other) {
    this.createdBy = other.createdBy;
  }
}

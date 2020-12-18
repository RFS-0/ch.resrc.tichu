package ch.resrc.tichu.adapters.endpoints_websocket.teams.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamIdDto {

  public String id;

  public TeamIdDto() {
    // For JSON deserialization.
  }

  public TeamIdDto(TeamIdDto other) {
    this.id = other.id;
  }
}

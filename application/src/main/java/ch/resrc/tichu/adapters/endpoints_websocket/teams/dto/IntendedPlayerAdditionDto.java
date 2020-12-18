package ch.resrc.tichu.adapters.endpoints_websocket.teams.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IntendedPlayerAdditionDto {

  public String teamId;
  public String userId;
  public String playerName;

  protected IntendedPlayerAdditionDto(IntendedPlayerAdditionDto other) {
    this.teamId = other.teamId;
    this.userId = other.userId;
    this.playerName = other.playerName;
  }
}

package ch.resrc.tichu.adapters.endpoints_websocket.add_first_player_to_team.dto;

public class IntendedFirstPlayerAdditionDto {

  public String gameId;
  public String teamId;
  public String userId;
  public String playerName;

  private IntendedFirstPlayerAdditionDto() {
    // for deserialization
  }

  public IntendedFirstPlayerAdditionDto(String gameId, String teamId, String userId, String playerName) {
    this.gameId = gameId;
    this.teamId = teamId;
    this.userId = userId;
    this.playerName = playerName;
  }
}

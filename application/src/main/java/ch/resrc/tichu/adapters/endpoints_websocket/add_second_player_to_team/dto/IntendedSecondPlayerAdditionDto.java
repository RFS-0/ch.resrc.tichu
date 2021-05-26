package ch.resrc.tichu.adapters.endpoints_websocket.add_second_player_to_team.dto;

public class IntendedSecondPlayerAdditionDto {

  public String gameId;
  public String teamId;
  public String userId;
  public String playerName;

  private IntendedSecondPlayerAdditionDto() {
    // for deserialization
  }

  public IntendedSecondPlayerAdditionDto(String gameId, String teamId, String userId, String playerName) {
    this.gameId = gameId;
    this.teamId = teamId;
    this.userId = userId;
    this.playerName = playerName;
  }
}

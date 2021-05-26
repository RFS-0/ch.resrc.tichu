package ch.resrc.tichu.adapters.endpoints_websocket.remove_second_player_from_team.dto;

public class IntendedSecondPlayerRemovalDto {

  public String gameId;
  public String teamId;
  public String userId;
  public String playerName;

  private IntendedSecondPlayerRemovalDto() {
    // for deserialization
  }

  public IntendedSecondPlayerRemovalDto(String gameId, String teamId, String userId, String playerName) {
    this.gameId = gameId;
    this.teamId = teamId;
    this.userId = userId;
    this.playerName = playerName;
  }
}

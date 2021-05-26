package ch.resrc.tichu.adapters.endpoints_websocket.remove_first_player_from_team.dto;

public class IntendedFirstPlayerRemovalDto {

  public String gameId;
  public String teamId;
  public String userId;
  public String playerName;

  private IntendedFirstPlayerRemovalDto() {
    // for deserialization
  }

  public IntendedFirstPlayerRemovalDto(String gameId, String teamId, String userId, String playerName) {
    this.gameId = gameId;
    this.teamId = teamId;
    this.userId = userId;
    this.playerName = playerName;
  }
}

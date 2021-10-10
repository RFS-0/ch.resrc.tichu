package ch.resrc.old.adapters.endpoints_websocket.input;

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

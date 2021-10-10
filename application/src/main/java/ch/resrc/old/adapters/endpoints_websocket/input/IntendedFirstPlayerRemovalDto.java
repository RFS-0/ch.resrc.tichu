package ch.resrc.old.adapters.endpoints_websocket.input;

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

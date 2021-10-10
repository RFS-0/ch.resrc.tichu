package ch.resrc.old.adapters.endpoints_websocket.input;

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

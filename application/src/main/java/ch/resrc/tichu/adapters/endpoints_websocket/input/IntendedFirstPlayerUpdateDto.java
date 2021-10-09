package ch.resrc.tichu.adapters.endpoints_websocket.input;

public class IntendedFirstPlayerUpdateDto {

  public String gameId;
  public String teamId;
  public String userId;
  public String playerName;

  private IntendedFirstPlayerUpdateDto() {
    // for deserialization
  }

  public IntendedFirstPlayerUpdateDto(String gameId, String teamId, String userId, String playerName) {
    this.gameId = gameId;
    this.teamId = teamId;
    this.userId = userId;
    this.playerName = playerName;
  }
}

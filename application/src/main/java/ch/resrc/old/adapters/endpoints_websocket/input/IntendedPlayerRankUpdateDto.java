package ch.resrc.old.adapters.endpoints_websocket.input;

public class IntendedPlayerRankUpdateDto {

  public String gameId;
  public String playerId;
  public String roundNumber;

  private IntendedPlayerRankUpdateDto() {
    // to deserialize
  }

  public IntendedPlayerRankUpdateDto(String gameId, String playerId, String roundNumber) {
    this.gameId = gameId;
    this.playerId = playerId;
    this.roundNumber = roundNumber;
  }
}

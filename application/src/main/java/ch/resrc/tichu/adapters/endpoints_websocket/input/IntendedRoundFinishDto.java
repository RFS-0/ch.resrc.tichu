package ch.resrc.tichu.adapters.endpoints_websocket.input;

public class IntendedRoundFinishDto {

  public String gameId;
  public String roundNumber;

  public IntendedRoundFinishDto() {
    // for deserialization
  }

  public IntendedRoundFinishDto(String gameId, String roundNumber) {
    this.gameId = gameId;
    this.roundNumber = roundNumber;
  }
}

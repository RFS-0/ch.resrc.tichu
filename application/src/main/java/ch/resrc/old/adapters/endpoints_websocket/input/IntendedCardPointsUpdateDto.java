package ch.resrc.old.adapters.endpoints_websocket.input;

public class IntendedCardPointsUpdateDto {

  public String gameId;
  public String teamId;
  public String roundNumber;
  public String cardPoints;

  private IntendedCardPointsUpdateDto() {
    // to deserialize
  }

  public IntendedCardPointsUpdateDto(String gameId, String teamId, String roundNumber, String cardPoints) {
    this.gameId = gameId;
    this.teamId = teamId;
    this.roundNumber = roundNumber;
    this.cardPoints = cardPoints;
  }
}

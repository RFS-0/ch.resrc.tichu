package ch.resrc.tichu.adapters.endpoints_websocket.update_card_points_of_round.dto;

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

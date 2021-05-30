package ch.resrc.tichu.adapters.endpoints_websocket.update_rank_of_player.dto;

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

package ch.resrc.tichu.adapters.endpoints_websocket.reset_rank_of_player.dto;

public class IntendedPlayerRankResetDto {

  public String gameId;
  public String playerId;
  public String roundNumber;

  private IntendedPlayerRankResetDto() {
    // to deserialize
  }

  public IntendedPlayerRankResetDto(String gameId, String playerId, String roundNumber) {
    this.gameId = gameId;
    this.playerId = playerId;
    this.roundNumber = roundNumber;
  }
}

package ch.resrc.tichu.adapters.endpoints_websocket.games.update_a_team_name.dto;

public class IntendedTeamNameDto {

  public String gameId;
  public String teamId;
  public String teamName;

  private IntendedTeamNameDto() {
    // to deserialize
  }

  public IntendedTeamNameDto(String gameId, String teamId, String teamName) {
    this.gameId = gameId;
    this.teamId = teamId;
    this.teamName = teamName;
  }
}

package ch.resrc.tichu.adapters.endpoints_websocket.teams.update_team_name.dto;

public class IntendedTeamNameDto {

  public String teamId;
  public String teamName;

  private IntendedTeamNameDto() {
    // to deserialize
  }

  public IntendedTeamNameDto(String teamId, String teamName) {
    this.teamId = teamId;
    this.teamName = teamName;
  }
}

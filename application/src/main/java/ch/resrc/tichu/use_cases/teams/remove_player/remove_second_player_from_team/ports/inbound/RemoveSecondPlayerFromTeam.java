package ch.resrc.tichu.use_cases.teams.remove_player.remove_second_player_from_team.ports.inbound;

import ch.resrc.tichu.use_cases.ports.output_boundary.TeamPresenter;
import ch.resrc.tichu.use_cases.teams.remove_player.ports.inbound.RemovePlayerFromTeamRequest;

public interface RemoveSecondPlayerFromTeam {

  void invoke(RemovePlayerFromTeamRequest requested, TeamPresenter out);

}

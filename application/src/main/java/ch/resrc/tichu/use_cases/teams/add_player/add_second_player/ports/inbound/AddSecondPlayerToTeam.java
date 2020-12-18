package ch.resrc.tichu.use_cases.teams.add_player.add_second_player.ports.inbound;

import ch.resrc.tichu.use_cases.ports.output_boundary.PlayerAddedToTeamPresenter;
import ch.resrc.tichu.use_cases.teams.add_player.ports.inbound.AddPlayerToTeamRequest;

public interface AddSecondPlayerToTeam {

  void invoke(AddPlayerToTeamRequest requested, PlayerAddedToTeamPresenter out);

}

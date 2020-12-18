package ch.resrc.tichu.use_cases.teams.add_player.add_first_player.ports.inbound;

import ch.resrc.tichu.use_cases.ports.input_boundary.InputBoundary;
import ch.resrc.tichu.use_cases.ports.output_boundary.PlayerAddedToTeamPresenter;
import ch.resrc.tichu.use_cases.teams.add_player.ports.inbound.AddPlayerToTeamRequest;

public interface AddFirstPlayerToTeam extends InputBoundary {

  void invoke(AddPlayerToTeamRequest requested, PlayerAddedToTeamPresenter out);

}

package ch.resrc.old.use_cases.add_second_player_to_team;

import ch.resrc.old.domain.operations.*;
import ch.resrc.old.use_cases.common.output.*;
import io.vavr.*;
import io.vavr.control.*;

import static ch.resrc.old.domain.operations.UpdateSecondPlayerOfTeam.UpdateSecondPlayerOfTeamProblem.*;

public class UpdateSecondPlayerOfTeamUseCase implements UpdateSecondPlayerOfTeam {

  private final FindOrCreatePlayerForUser findOrCreatePlayerForUser;
  private final UpdatePlayersOfTeam updatePlayersOfTeam;

  public UpdateSecondPlayerOfTeamUseCase(FindOrCreatePlayerForUser findOrCreatePlayerForUser,
                                         UpdatePlayersOfTeam updatePlayersOfTeam) {
    this.findOrCreatePlayerForUser = findOrCreatePlayerForUser;
    this.updatePlayersOfTeam = updatePlayersOfTeam;
  }

  @Override
  public Either<UpdateSecondPlayerOfTeamProblem, UpdateSecondPlayerOfTeamOutput> invoke(UpdateSecondPlayerOfTeamInput input) {

    final var problemOrPlayer = findOrCreatePlayerForUser.findOrCreate(input.userId(), input.playerName());

    if (problemOrPlayer.isLeft()) {
      return Either.left(operationFailed(problemOrPlayer.getLeft()));
    }

    return updatePlayersOfTeam.update(
        input.gameId(),
        input.teamId(),
        Tuple.of(problemOrPlayer.get(), null)
      )
      .mapLeft(UpdateSecondPlayerOfTeamProblem::operationFailed)
      .map(updatedGame -> new UpdateSecondPlayerOfTeamOutput(GameDocument.fromGame(updatedGame)));
  }
}

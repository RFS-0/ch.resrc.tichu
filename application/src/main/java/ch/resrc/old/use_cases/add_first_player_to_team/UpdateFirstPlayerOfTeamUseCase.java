package ch.resrc.old.use_cases.add_first_player_to_team;

import ch.resrc.old.domain.operations.*;
import ch.resrc.old.use_cases.common.output.*;
import io.vavr.*;
import io.vavr.control.*;

import static ch.resrc.old.domain.operations.UpdateFirstPlayerOfTeam.UpdateFirstPlayerOfTeamProblem.*;

public class UpdateFirstPlayerOfTeamUseCase implements UpdateFirstPlayerOfTeam {

  private final FindOrCreatePlayerForUser findOrCreatePlayerForUser;
  private final UpdatePlayersOfTeam updatePlayersOfTeam;

  public UpdateFirstPlayerOfTeamUseCase(FindOrCreatePlayerForUser findOrCreatePlayerForUser,
                                        UpdatePlayersOfTeam updatePlayersOfTeam
  ) {
    this.findOrCreatePlayerForUser = findOrCreatePlayerForUser;
    this.updatePlayersOfTeam = updatePlayersOfTeam;
  }

  @Override
  public Either<UpdateFirstPlayerOfTeamProblem, UpdateFirstPlayerOfTeamOutput> invoke(UpdateFirstPlayerOfTeamInput input) {

    final var problemOrPlayer = findOrCreatePlayerForUser.findOrCreate(input.userId(), input.playerName());

    if (problemOrPlayer.isLeft()) {
      return Either.left(operationFailed(problemOrPlayer.getLeft()));
    }

    return updatePlayersOfTeam.update(
        input.gameId(),
        input.teamId(),
        Tuple.of(problemOrPlayer.get(), null)
      )
      .mapLeft(UpdateFirstPlayerOfTeamProblem::operationFailed)
      .map(updatedGame -> new UpdateFirstPlayerOfTeamOutput(GameDocument.fromGame(updatedGame)));
  }
}

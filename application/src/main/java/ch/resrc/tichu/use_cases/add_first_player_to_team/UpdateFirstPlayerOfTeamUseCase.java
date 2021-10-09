package ch.resrc.tichu.use_cases.add_first_player_to_team;

import ch.resrc.tichu.domain.operations.FindOrCreatePlayerForUser;
import ch.resrc.tichu.domain.operations.UpdateFirstPlayerOfTeam;
import ch.resrc.tichu.domain.operations.UpdatePlayersOfTeam;
import ch.resrc.tichu.use_cases.common.output.GameDocument;
import io.vavr.Tuple;
import io.vavr.control.Either;

import static ch.resrc.tichu.domain.operations.UpdateFirstPlayerOfTeam.UpdateFirstPlayerOfTeamProblem.operationFailed;

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

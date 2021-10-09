package ch.resrc.tichu.use_cases.add_second_player_to_team;

import ch.resrc.tichu.domain.operations.FindOrCreatePlayerForUser;
import ch.resrc.tichu.domain.operations.UpdatePlayersOfTeam;
import ch.resrc.tichu.domain.operations.UpdateSecondPlayerOfTeam;
import ch.resrc.tichu.use_cases.common.output.GameDocument;
import io.vavr.Tuple;
import io.vavr.control.Either;

import static ch.resrc.tichu.domain.operations.UpdateSecondPlayerOfTeam.UpdateSecondPlayerOfTeamProblem.operationFailed;

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

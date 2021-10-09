package ch.resrc.tichu.domain.operations;

import ch.resrc.tichu.capabilities.errorhandling.problems.Problem;
import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.domain.value_objects.Id;
import io.vavr.Tuple2;
import io.vavr.control.Either;

@FunctionalInterface
public interface UpdatePlayersOfTeam {

  Either<? extends Problem, Game> update(Id gameId, Id teamId, Tuple2<Player, Player> players);
}

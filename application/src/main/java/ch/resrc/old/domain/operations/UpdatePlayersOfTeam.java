package ch.resrc.old.domain.operations;

import ch.resrc.old.capabilities.errorhandling.problems.*;
import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.*;
import io.vavr.control.*;

@FunctionalInterface
public interface UpdatePlayersOfTeam {

  Either<? extends Problem, Game> update(Id gameId, Id teamId, Tuple2<Player, Player> players);
}

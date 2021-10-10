package ch.resrc.old.use_cases.reset_rank_of_player.ports.input;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.util.function.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

public class IntendedPlayerRankReset {

  private Id gameId;
  private Id playerId;
  private RoundNumber roundNumber;

  private IntendedPlayerRankReset() {
  }

  private IntendedPlayerRankReset(IntendedPlayerRankReset other) {
    this.gameId = other.gameId;
    this.playerId = other.playerId;
    this.roundNumber = other.roundNumber;
  }

  private IntendedPlayerRankReset copied(Consumer<IntendedPlayerRankReset> modification) {
    var theCopy = new IntendedPlayerRankReset(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static Builder anIntendedPlayerRankReset() {
    return new Builder();
  }

  private static Validation<Seq<ValidationError>, IntendedPlayerRankReset> validation() {
    return allOf(
      attribute(x -> x.gameId, notNull(mustNotBeNull())),
      attribute(x -> x.playerId, notNull(mustNotBeNull())),
      attribute(x -> x.roundNumber, notNull(mustNotBeNull()))
    );
  }

  public Id gameId() {
    return gameId;
  }

  public Id playerId() {
    return playerId;
  }

  public RoundNumber roundNumber() {
    return roundNumber;
  }

  public static class Builder {

    IntendedPlayerRankReset workpiece;

    public Builder() {
      this.workpiece = new IntendedPlayerRankReset();
    }

    private Builder(IntendedPlayerRankReset workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withGameId(Id gameId) {
      return new Builder(workpiece.copied(but -> but.gameId = gameId));
    }

    public Builder withPlayerId(Id playerId) {
      return new Builder(workpiece.copied(but -> but.playerId = playerId));
    }

    public Builder withRoundNumber(RoundNumber roundNumber) {
      return new Builder(workpiece.copied(but -> but.roundNumber = roundNumber));
    }

    public Either<Seq<ValidationError>, IntendedPlayerRankReset> buildResult() {
      return validation().applyTo(workpiece);
    }
  }
}

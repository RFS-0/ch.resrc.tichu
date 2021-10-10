package ch.resrc.old.use_cases.update_rank_of_player.ports.input;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.util.function.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

public class IntendedPlayerRankUpdate {

  private static final Supplier<ValidationError> MUST_NOT_BE_NULL = () -> ValidationError.of(
    "must not be null"
  );

  private Id gameId;
  private Id playerId;
  private RoundNumber roundNumber;

  private IntendedPlayerRankUpdate() {
  }

  private IntendedPlayerRankUpdate(IntendedPlayerRankUpdate other) {
    this.gameId = other.gameId;
    this.playerId = other.playerId;
    this.roundNumber = other.roundNumber;
  }

  private IntendedPlayerRankUpdate copied(Consumer<IntendedPlayerRankUpdate> modification) {
    var theCopy = new IntendedPlayerRankUpdate(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static IntendedPlayerRankUpdate.Builder anIntendedPlayerRankUpdate() {
    return new Builder();
  }

  private static Validation<Seq<ValidationError>, IntendedPlayerRankUpdate> validation() {
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

    IntendedPlayerRankUpdate workpiece;

    public Builder() {
      this.workpiece = new IntendedPlayerRankUpdate();
    }

    private Builder(IntendedPlayerRankUpdate workpiece) {
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

    public Either<Seq<ValidationError>, IntendedPlayerRankUpdate> buildResult() {
      return validation().applyTo(workpiece);
    }
  }
}


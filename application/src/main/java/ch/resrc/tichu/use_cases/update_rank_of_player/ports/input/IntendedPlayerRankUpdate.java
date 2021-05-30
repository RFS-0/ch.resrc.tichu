package ch.resrc.tichu.use_cases.update_rank_of_player.ports.input;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.RoundNumber;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;
import static ch.resrc.tichu.use_cases.update_rank_of_player.ports.input.IntendedPlayerRankUpdateValidationErrors.MUST_NOT_BE_NULL;

public class IntendedPlayerRankUpdate {

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
      attribute(x -> x.gameId, notNull(MUST_NOT_BE_NULL)),
      attribute(x -> x.playerId, notNull(MUST_NOT_BE_NULL)),
      attribute(x -> x.roundNumber, notNull(MUST_NOT_BE_NULL))
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

class IntendedPlayerRankUpdateValidationErrors {

  static final Supplier<ValidationError> MUST_NOT_BE_NULL = () -> ValidationError.of(
    IntendedPlayerRankUpdate.class.getName(), "must not be null"
  );
}

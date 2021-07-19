package ch.resrc.tichu.use_cases.update_round.ports.input;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.RoundNumber;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.function.Consumer;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;
import static ch.resrc.tichu.domain.validation.DomainValidationErrors.mustNotBeNull;

public class IntendedRoundUpdate {

  private Id gameId;
  private RoundNumber roundNumber;

  public IntendedRoundUpdate() {
  }

  public IntendedRoundUpdate(IntendedRoundUpdate other) {
    this.gameId = other.gameId;
    this.roundNumber = other.roundNumber;
  }

  private IntendedRoundUpdate copied(Consumer<IntendedRoundUpdate> modification) {
    var theCopy = new IntendedRoundUpdate(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static Builder anIntendedRoundUpdate() {
    return new Builder();
  }

  private static Validation<Seq<ValidationError>, IntendedRoundUpdate> validation() {
    return allOf(
      attribute(x -> x.gameId, notNull(mustNotBeNull())),
      attribute(x -> x.roundNumber, notNull(mustNotBeNull()))
    );
  }

  public Id gameId() {
    return gameId;
  }

  public RoundNumber roundNumber() {
    return roundNumber;
  }

  public static class Builder {

    IntendedRoundUpdate workpiece;

    public Builder() {
      this.workpiece = new IntendedRoundUpdate();
    }

    public Builder(IntendedRoundUpdate workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withGameId(Id gameId) {
      return new Builder(workpiece.copied(but -> but.gameId = gameId));
    }

    public Builder withRoundNumber(RoundNumber roundNumber) {
      return new Builder(workpiece.copied(but -> but.roundNumber = roundNumber));
    }

    public Either<Seq<ValidationError>, IntendedRoundUpdate> buildResult() {
      return validation().applyTo(workpiece);
    }
  }
}

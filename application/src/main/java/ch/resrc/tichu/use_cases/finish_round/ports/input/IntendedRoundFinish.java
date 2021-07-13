package ch.resrc.tichu.use_cases.finish_round.ports.input;

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

public class IntendedRoundFinish {

  private Id gameId;
  private RoundNumber roundNumber;

  public IntendedRoundFinish() {
  }

  public IntendedRoundFinish(IntendedRoundFinish other) {
    this.gameId = other.gameId;
    this.roundNumber = other.roundNumber;
  }

  private IntendedRoundFinish copied(Consumer<IntendedRoundFinish> modification) {
    var theCopy = new IntendedRoundFinish(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static Builder anIntendedRoundFinish() {
    return new Builder();
  }

  private static Validation<Seq<ValidationError>, IntendedRoundFinish> validation() {
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

    IntendedRoundFinish workpiece;

    public Builder() {
      this.workpiece = new IntendedRoundFinish();
    }

    public Builder(IntendedRoundFinish workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withGameId(Id gameId) {
      return new Builder(workpiece.copied(but -> but.gameId = gameId));
    }

    public Builder withRoundNumber(RoundNumber roundNumber) {
      return new Builder(workpiece.copied(but -> but.roundNumber = roundNumber));
    }

    public Either<Seq<ValidationError>, IntendedRoundFinish> buildResult() {
      return validation().applyTo(workpiece);
    }
  }
}

package ch.resrc.old.use_cases.finish_round.ports.input;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.util.function.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

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

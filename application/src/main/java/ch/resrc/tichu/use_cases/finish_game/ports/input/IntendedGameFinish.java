package ch.resrc.tichu.use_cases.finish_game.ports.input;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.function.Consumer;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;
import static ch.resrc.tichu.domain.validation.DomainValidationErrors.mustNotBeNull;

public class IntendedGameFinish {

  private Id gameId;

  public IntendedGameFinish() {
  }

  public IntendedGameFinish(IntendedGameFinish other) {
    this.gameId = other.gameId;
  }

  private IntendedGameFinish copied(Consumer<IntendedGameFinish> modification) {
    var theCopy = new IntendedGameFinish(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static Builder anIntendedGameFinish() {
    return new Builder();
  }

  private static Validation<Seq<ValidationError>, IntendedGameFinish> validation() {
    return allOf(
      attribute(x -> x.gameId, notNull(mustNotBeNull()))
    );
  }

  public Id gameId() {
    return gameId;
  }

  public static class Builder {

    IntendedGameFinish workpiece;

    public Builder() {
      this.workpiece = new IntendedGameFinish();
    }

    public Builder(IntendedGameFinish workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withGameId(Id gameId) {
      return new Builder(workpiece.copied(but -> but.gameId = gameId));
    }

    public Either<Seq<ValidationError>, IntendedGameFinish> buildResult() {
      return validation().applyTo(workpiece);
    }
  }
}

package ch.resrc.tichu.use_cases.create_a_game.ports.input;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.use_cases.ports.input_boundary.InputData;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.function.Consumer;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;

public class IntendedGame implements InputData {

  private Id createdBy;

  private IntendedGame() {
  }

  private IntendedGame(IntendedGame other) {
    this.createdBy = other.createdBy;
  }

  public static Builder anIntendedGame() {
    return new Builder();
  }

  private static Validation<Seq<ValidationError>, IntendedGame> validation() {
    return allOf(
      attribute(x -> x.createdBy, notNull(() -> ValidationError.of("", "")))
    );
  }

  private IntendedGame copied(Consumer<IntendedGame> modification) {
    var theCopy = new IntendedGame(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public Id createdBy() {
    return createdBy;
  }

  public static class Builder {

    private final IntendedGame workpiece;

    private Builder() {
      this.workpiece = new IntendedGame();
    }

    private Builder(IntendedGame workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withCreatedBy(Id userId) {
      return new Builder(workpiece.copied(but -> but.createdBy = userId));
    }

    public Either<Seq<ValidationError>, IntendedGame> buildResult() {
      return validation().applyTo(workpiece);
    }
  }
}

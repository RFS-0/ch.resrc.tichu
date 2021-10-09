package ch.resrc.tichu.use_cases.finish_game;

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

public class FinishGameInput {

  private Id gameId;

  private FinishGameInput() {
  }

  public FinishGameInput(FinishGameInput other) {
    this.gameId = other.gameId;
  }

  private FinishGameInput copied(Consumer<FinishGameInput> modification) {
    var theCopy = new FinishGameInput(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static Builder aFinishGameInput() {
    return new Builder();
  }

  private static Validation<Seq<ValidationError>, FinishGameInput> validation() {
    return allOf(
      attribute(x -> x.gameId, notNull(mustNotBeNull()))
    );
  }

  public Id gameId() {
    return gameId;
  }

  public static class Builder {

    FinishGameInput workpiece;

    public Builder() {
      this.workpiece = new FinishGameInput();
    }

    public Builder(FinishGameInput workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withGameId(Id gameId) {
      return new Builder(workpiece.copied(but -> but.gameId = gameId));
    }

    public Either<Seq<ValidationError>, FinishGameInput> buildResult() {
      return validation().applyTo(workpiece);
    }
  }
}

package ch.resrc.old.use_cases.finish_game;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.util.function.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

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

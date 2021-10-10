package ch.resrc.old.use_cases.create_game;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.util.function.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

public class CreateGameInput {

  private Id createdBy;

  private CreateGameInput() {
  }

  private CreateGameInput(CreateGameInput other) {
    this.createdBy = other.createdBy;
  }

  public static CreateGameInput.Builder aCreateGameInput() {
    return new CreateGameInput.Builder();
  }

  private static Validation<Seq<ValidationError>, CreateGameInput> validation() {
    return allOf(
      attribute(x -> x.createdBy, notNull(mustNotBeNull()))
    );
  }

  private CreateGameInput copied(Consumer<CreateGameInput> modification) {
    var theCopy = new CreateGameInput(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public Id createdBy() {
    return createdBy;
  }

  public static class Builder {

    private final CreateGameInput workpiece;

    private Builder() {
      this.workpiece = new CreateGameInput();
    }

    private Builder(CreateGameInput workpiece) {
      this.workpiece = workpiece;
    }

    public CreateGameInput.Builder withCreatedBy(Id userId) {
      return new CreateGameInput.Builder(workpiece.copied(but -> but.createdBy = userId));
    }

    public Either<Seq<ValidationError>, CreateGameInput> buildResult() {
      return validation().applyTo(workpiece);
    }
  }
}

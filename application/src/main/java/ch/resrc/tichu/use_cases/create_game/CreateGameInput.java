package ch.resrc.tichu.use_cases.create_game;

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

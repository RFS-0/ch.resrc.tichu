package ch.resrc.tichu.use_cases.teams.update_team_name.ports.input;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Name;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;
import static ch.resrc.tichu.use_cases.teams.update_team_name.ports.input.IntendedTeamNameValidationErrors.MUST_NOT_BE_NULL;

public class IntendedTeamName {

  private Name teamName;

  private IntendedTeamName() {
  }

  private IntendedTeamName(IntendedTeamName other) {
    this.teamName = other.teamName;
  }

  private IntendedTeamName copied(Consumer<IntendedTeamName> modification) {
    var theCopy = new IntendedTeamName(this);
    modification.accept(theCopy);
    return theCopy;
  }

  private static Validation<Seq<ValidationError>, IntendedTeamName> validation() {
    return allOf(
      attribute(x -> x.teamName, notNull(MUST_NOT_BE_NULL))
    );
  }

  public Name teamName() {
    return teamName;
  }

  public static class Builder {

    private final IntendedTeamName workpiece;

    private Builder() {
      this.workpiece = new IntendedTeamName();
    }

    private Builder(IntendedTeamName workpiece) {
      this.workpiece = workpiece;
    }

    public IntendedTeamName.Builder withTeamName(Name teamName) {
      return new IntendedTeamName.Builder(workpiece.copied(but -> but.teamName = teamName));
    }

    public Either<Seq<ValidationError>, IntendedTeamName> buildResult() {
      return validation().applyTo(workpiece);
    }
  }
}

class IntendedTeamNameValidationErrors {

  static final Supplier<ValidationError> MUST_NOT_BE_NULL = () -> ValidationError.of(
    IntendedTeamName.class.getName(), "must not be null"
  );
}

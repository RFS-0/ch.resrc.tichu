package ch.resrc.tichu.use_cases.games.update_a_team_name.ports.input;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;
import static ch.resrc.tichu.use_cases.games.update_a_team_name.ports.input.IntendedTeamNameValidationErrors.MUST_NOT_BE_NULL;

public class IntendedTeamName {

  private Id gameId;
  private Id teamId;
  private Name teamName;

  private IntendedTeamName() {
  }

  private IntendedTeamName(IntendedTeamName other) {
    this.gameId = other.gameId;
    this.teamId = other.teamId;
    this.teamName = other.teamName;
  }

  public static IntendedTeamName.Builder anIntendedTeamName() {
    return new Builder();
  }

  private IntendedTeamName copied(Consumer<IntendedTeamName> modification) {
    var theCopy = new IntendedTeamName(this);
    modification.accept(theCopy);
    return theCopy;
  }

  private static Validation<Seq<ValidationError>, IntendedTeamName> validation() {
    return allOf(
      attribute(x -> x.gameId, notNull(MUST_NOT_BE_NULL)),
      attribute(x -> x.teamId, notNull(MUST_NOT_BE_NULL)),
      attribute(x -> x.teamName, notNull(MUST_NOT_BE_NULL))
    );
  }

  public Id gameId() {
    return gameId;
  }

  public Name teamName() {
    return teamName;
  }

  public Id teamId() {
    return teamId;
  }

  public static class Builder {

    private final IntendedTeamName workpiece;

    private Builder() {
      this.workpiece = new IntendedTeamName();
    }

    private Builder(IntendedTeamName workpiece) {
      this.workpiece = workpiece;
    }

    public IntendedTeamName.Builder withGameId(Id gameId) {
      return new IntendedTeamName.Builder(workpiece.copied(but -> but.gameId = gameId));
    }

    public IntendedTeamName.Builder withTeamId(Id teamId) {
      return new IntendedTeamName.Builder(workpiece.copied(but -> but.teamId = teamId));
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

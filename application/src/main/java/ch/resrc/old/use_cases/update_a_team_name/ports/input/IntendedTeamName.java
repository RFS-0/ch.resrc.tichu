package ch.resrc.old.use_cases.update_a_team_name.ports.input;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.util.function.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

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
      attribute(x -> x.gameId, notNull(mustNotBeNull())),
      attribute(x -> x.teamId, notNull(mustNotBeNull())),
      attribute(x -> x.teamName, notNull(mustNotBeNull()))
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

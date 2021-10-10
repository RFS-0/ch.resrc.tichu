package ch.resrc.old.use_cases.common.input;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.util.function.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

public class IntendedPlayerRemoval {

  private Id gameId;
  private Id teamId;
  private Id userId;
  private Name playerName;

  public IntendedPlayerRemoval() {
  }

  public IntendedPlayerRemoval(IntendedPlayerRemoval other) {
    this.gameId = other.gameId;
    this.teamId = other.teamId;
    this.userId = other.userId;
    this.playerName = other.playerName;
  }

  private IntendedPlayerRemoval copied(Consumer<IntendedPlayerRemoval> modification) {
    var theCopy = new IntendedPlayerRemoval(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static Builder anIntendedPlayerRemoval() {
    return new Builder();
  }

  private static Validation<Seq<ValidationError>, IntendedPlayerRemoval> validation() {
    return Validations.allOf(
      attribute(x -> x.gameId, notNull(errorDetails("must have either user id or player id"))),
      attribute(x -> x.teamId, notNull(errorDetails("must have either user id or player id"))),
      isTrueOrError(
        intendedPlayerAddition -> intendedPlayerAddition.userId != null || intendedPlayerAddition.playerName != null,
        errorDetails("must have either user id or player id")
      )
    );
  }

  public Id gameId() {
    return gameId;
  }

  public Id teamId() {
    return teamId;
  }

  public Id userId() {
    return userId;
  }

  public Name playerName() {
    return playerName;
  }

  public static class Builder {

    private final IntendedPlayerRemoval workpiece;

    private Builder() {
      this.workpiece = new IntendedPlayerRemoval();
    }

    public Builder(IntendedPlayerRemoval workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withGameId(Id gameId) {
      return new Builder(workpiece.copied(but -> but.gameId = gameId));
    }

    public Builder withTeamId(Id teamId) {
      return new Builder(workpiece.copied(but -> but.teamId = teamId));
    }

    public Builder withUserId(Id userId) {
      return new Builder(workpiece.copied(but -> but.userId = userId));
    }

    public Builder withPlayerName(Name playerName) {
      return new Builder(workpiece.copied(but -> but.playerName = playerName));
    }

    public Either<Seq<ValidationError>, IntendedPlayerRemoval> buildResult() {
      return validation().applyTo(workpiece);
    }
  }
}

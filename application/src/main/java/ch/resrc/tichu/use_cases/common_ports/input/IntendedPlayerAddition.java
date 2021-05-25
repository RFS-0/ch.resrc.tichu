package ch.resrc.tichu.use_cases.common_ports.input;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.capabilities.validation.Validations;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.isTrueOrError;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;
import static ch.resrc.tichu.use_cases.common_ports.input.IntendedPlayerAdditionValidationErrors.MUST_HAVE_EITHER_USER_ID_OR_PLAYER_ID;
import static ch.resrc.tichu.use_cases.common_ports.input.IntendedPlayerAdditionValidationErrors.MUST_NOT_BE_NULL;

public class IntendedPlayerAddition {

  private Id gameId;
  private Id teamId;
  private Id userId;
  private Name playerName;

  private IntendedPlayerAddition() {
  }

  private IntendedPlayerAddition(IntendedPlayerAddition other) {
    this.gameId = other.gameId;
    this.teamId = other.teamId;
    this.userId = other.userId;
    this.playerName = other.playerName;
  }

  private IntendedPlayerAddition copied(Consumer<IntendedPlayerAddition> modification) {
    var theCopy = new IntendedPlayerAddition(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static Builder anIntendedPlayerAddition() {
    return new Builder();
  }

  private static Validation<Seq<ValidationError>, IntendedPlayerAddition> validation() {
    return Validations.allOf(
      attribute(x -> x.gameId, notNull(MUST_NOT_BE_NULL)),
      attribute(x -> x.teamId, notNull(MUST_NOT_BE_NULL)),
      isTrueOrError(
        intendedPlayerAddition -> intendedPlayerAddition.userId != null || intendedPlayerAddition.playerName != null,
        MUST_HAVE_EITHER_USER_ID_OR_PLAYER_ID
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

    private final IntendedPlayerAddition workpiece;

    private Builder() {
      this.workpiece = new IntendedPlayerAddition();
    }

    public Builder(IntendedPlayerAddition workpiece) {
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

    public Either<Seq<ValidationError>, IntendedPlayerAddition> buildResult() {
      return validation().applyTo(workpiece);
    }
  }
}

class IntendedPlayerAdditionValidationErrors {

  static final Supplier<ValidationError> MUST_NOT_BE_NULL = () -> ValidationError.of(
    IntendedPlayerAddition.class.getName(), "must not be null"
  );

  static final Supplier<ValidationError> MUST_HAVE_EITHER_USER_ID_OR_PLAYER_ID = () -> ValidationError.of(
    IntendedPlayerAddition.class.getName(), "must have either user id or player id"
  );
}

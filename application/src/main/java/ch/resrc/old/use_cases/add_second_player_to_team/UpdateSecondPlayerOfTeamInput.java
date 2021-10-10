package ch.resrc.old.use_cases.add_second_player_to_team;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.util.function.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

public class UpdateSecondPlayerOfTeamInput {

  private Id gameId;
  private Id teamId;
  private Id userId;
  private Name playerName;

  private UpdateSecondPlayerOfTeamInput() {
  }

  private UpdateSecondPlayerOfTeamInput(UpdateSecondPlayerOfTeamInput other) {
    this.gameId = other.gameId;
    this.teamId = other.teamId;
    this.userId = other.userId;
    this.playerName = other.playerName;
  }

  private UpdateSecondPlayerOfTeamInput copied(Consumer<UpdateSecondPlayerOfTeamInput> modification) {
    var theCopy = new UpdateSecondPlayerOfTeamInput(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static UpdateSecondPlayerOfTeamInput.Builder anUpdateSecondPlayerOfTeamInput() {
    return new UpdateSecondPlayerOfTeamInput.Builder();
  }

  private static Validation<Seq<ValidationError>, UpdateSecondPlayerOfTeamInput> validation() {
    return Validations.allOf(
      attribute(x -> x.gameId, notNull(errorDetails("must not be null"))),
      attribute(x -> x.teamId, notNull(errorDetails("must not be null"))),
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

    private final UpdateSecondPlayerOfTeamInput workpiece;

    private Builder() {
      this.workpiece = new UpdateSecondPlayerOfTeamInput();
    }

    public Builder(UpdateSecondPlayerOfTeamInput workpiece) {
      this.workpiece = workpiece;
    }

    public UpdateSecondPlayerOfTeamInput.Builder withGameId(Id gameId) {
      return new Builder(workpiece.copied(but -> but.gameId = gameId));
    }

    public UpdateSecondPlayerOfTeamInput.Builder withTeamId(Id teamId) {
      return new Builder(workpiece.copied(but -> but.teamId = teamId));
    }

    public UpdateSecondPlayerOfTeamInput.Builder withUserId(Id userId) {
      return new Builder(workpiece.copied(but -> but.userId = userId));
    }

    public UpdateSecondPlayerOfTeamInput.Builder withPlayerName(Name playerName) {
      return new Builder(workpiece.copied(but -> but.playerName = playerName));
    }

    public Either<Seq<ValidationError>, UpdateSecondPlayerOfTeamInput> buildResult() {
      return validation().applyTo(workpiece);
    }
  }
}

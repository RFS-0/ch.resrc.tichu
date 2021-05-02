package ch.resrc.tichu.use_cases.common_ports.input;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.capabilities.validation.Validations;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.function.Consumer;

import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.isTrueOrError;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;

public class IntendedPlayerAddition {

  private Id teamId;
  private Id userId;
  private Name playerName;

  ///// Characteristics /////

  public Id teamId() {
    return teamId;
  }

  public Id userId() {
    return userId;
  }

  public Name playerName() {
    return playerName;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("teamId", teamId)
      .append("userId", userId)
      .append("playerName", playerName)
      .toString();
  }

  private static Validation<Seq<ValidationError>, IntendedPlayerAddition> validation() {
    var bla = "test";
    return Validations.allOf(
      attribute(
        x -> x.teamId,
        notNull(
          () -> ValidationError.of(bla, bla)
        )
      ),
      isTrueOrError(
        intendedPlayerAddition -> intendedPlayerAddition.userId != null || intendedPlayerAddition.playerName != null,
        () -> ValidationError.of(null, null)
      )
    );
  }

  public static Builder anIntendedPlayerAddition() {
    return new Builder();
  }

  private IntendedPlayerAddition() {
  }

  private IntendedPlayerAddition(IntendedPlayerAddition other) {
    this.teamId = other.teamId;
    this.userId = other.userId;
    this.playerName = other.playerName;
  }

  private IntendedPlayerAddition copied(Consumer<IntendedPlayerAddition> modification) {
    var theCopy = new IntendedPlayerAddition(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static class Builder {

    private final IntendedPlayerAddition workpiece;

    private Builder() {
      this.workpiece = new IntendedPlayerAddition();
    }

    public Builder(IntendedPlayerAddition workpiece) {
      this.workpiece = workpiece;
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

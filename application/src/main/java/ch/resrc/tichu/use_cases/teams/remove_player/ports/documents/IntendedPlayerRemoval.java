package ch.resrc.tichu.use_cases.teams.remove_player.ports.documents;

import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.function.Consumer;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;

public class IntendedPlayerRemoval {

  private Id teamId;

  ///// Characteristics /////

  public Id teamId() {
    return teamId;
  }


  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("teamId", teamId)
      .toString();
  }

  ///// Constraints /////

  private static Validation<Seq<ValidationError>, IntendedPlayerRemoval> validation() {
    return allOf(
      attribute(x -> x.teamId, notNull(() -> ValidationError.of("", "")))
    );
  }

  ///// Construction /////

  public static Builder anIntendedPlayerRemoval() {
    return new Builder();
  }

  private IntendedPlayerRemoval() {
  }

  private IntendedPlayerRemoval(IntendedPlayerRemoval other) {
    this.teamId = other.teamId;
  }

  private IntendedPlayerRemoval copied(Consumer<IntendedPlayerRemoval> modification) {
    var theCopy = new IntendedPlayerRemoval(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static class Builder {

    private final IntendedPlayerRemoval workpiece;

    private Builder() {
      this.workpiece = new IntendedPlayerRemoval();
    }

    public Builder(IntendedPlayerRemoval workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withTeamId(Id teamId) {
      return new Builder(workpiece.copied(but -> but.teamId = teamId));
    }

    public Either<Seq<ValidationError>, IntendedPlayerRemoval> buildResult() {
      return validation().applyTo(workpiece);
    }

    public IntendedPlayerRemoval build() {
      return buildResult().getOrElseThrow(InvalidInputDetected::of);
    }
  }

}

package ch.resrc.tichu.usecases.create_game.ports.documents;

import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.context;
import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.mustBeSpecifiedMsg;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.modified;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;

import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.capabilities.validation.Validations;
import ch.resrc.tichu.domain.value_objects.Id;
import java.util.function.Consumer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class IntendedGame {

  private Id createdBy;

  ///// Characteristics /////

  public Id createdBy() {
    return createdBy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    IntendedGame that = (IntendedGame) o;

    return createdBy.equals(that.createdBy);
  }

  @Override
  public int hashCode() {
    return createdBy.hashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
      .append("createdBy", createdBy)
      .toString();
  }

  ///// Constraints /////

  private static Validation<IntendedGame, ValidationError> validation() {

    return modified(Validations.allOf(
      attribute(x -> x.createdBy, notNull(mustBeSpecifiedMsg(), context("time bin name")))
      ),
      context(IntendedGame.class)
    );
  }

  ///// Construction /////

  public static IntendedGame.Builder anIntendedGame() {
    return new Builder();
  }

  public IntendedGame.Builder but() {
    return new Builder(this.copied(__ -> {
    }));
  }

  private IntendedGame() {
  }

  private IntendedGame(IntendedGame other) {
    this.createdBy = other.createdBy;
  }

  private IntendedGame copied(Consumer<IntendedGame> modification) {
    var theCopy = new IntendedGame(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static class Builder {

    private final IntendedGame workpiece;

    private Builder() {
      this.workpiece = new IntendedGame();
    }

    private Builder(IntendedGame workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withCreatedBy(Id userId) {
      return new Builder(workpiece.copied(but -> but.createdBy = userId));
    }

    public Result<IntendedGame, ValidationError> buildResult() {
      return validation().applyTo(workpiece);
    }

    public IntendedGame build() {
      return this.buildResult().getOrThrow(InvalidInputDetected::of);
    }
  }
}

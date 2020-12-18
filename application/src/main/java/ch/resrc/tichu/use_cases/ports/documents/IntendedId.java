package ch.resrc.tichu.use_cases.ports.documents;

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

public class IntendedId {

  private Id id;

  ///// Characteristics /////

  public Id id() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    IntendedId that = (IntendedId) o;

    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("id", id)
      .toString();
  }

  ///// Constraints /////

  private static Validation<Seq<ValidationError>, IntendedId> validation() {
    return allOf(
      attribute((IntendedId x) -> x.id, notNull(() -> ValidationError.of("", "")))
    );
  }

  ///// Construction /////

  public static IntendedId.Builder anIntendedId() {
    return new Builder();
  }

  public IntendedId.Builder but() {
    return new Builder(this.copied(__ -> {
    }));
  }

  private IntendedId() {
  }

  private IntendedId(IntendedId other) {
    this.id = other.id;
  }

  private IntendedId copied(Consumer<IntendedId> modification) {
    var theCopy = new IntendedId(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static class Builder {

    private final IntendedId workpiece;

    private Builder() {
      this.workpiece = new IntendedId();
    }

    private Builder(IntendedId workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withId(Id id) {
      return new Builder(workpiece.copied(but -> but.id = id));
    }

    public Either<Seq<ValidationError>, IntendedId> buildResult() {
      return validation().applyTo(workpiece);
    }

    public IntendedId build() {
      return this.buildResult().getOrElseThrow(InvalidInputDetected::of);
    }
  }
}

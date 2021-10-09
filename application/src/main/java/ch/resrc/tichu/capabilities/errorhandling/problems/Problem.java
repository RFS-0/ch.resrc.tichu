package ch.resrc.tichu.capabilities.errorhandling.problems;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.HashSet;
import io.vavr.collection.Seq;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;

public class Problem {

  private final UUID id;
  private final Instant occurredOn;
  private String title;
  private String details;
  private Problem causedBy;
  private Seq<ValidationError> validationErrors;

  protected Problem() {
    this.id = UUID.randomUUID();
    this.occurredOn = Instant.now();
  }

  public static Builder aProblem() {
    return new Builder();
  }

  private Problem copied(Consumer<Problem> modification) {
    final var theCopy = new Problem();
    modification.accept(theCopy);
    return theCopy;
  }

  public UUID id() {
    return id;
  }

  public Instant occurredOn() {
    return occurredOn;
  }

  public String title() {
    return title;
  }

  public Problem butTitle(String title) {
    return copied(but -> but.title = title);
  }

  public String details() {
    return details;
  }

  public Problem butDetails(String details) {
    return copied(but -> but.details = details);
  }

  public Problem causedBy() {
    return causedBy;
  }

  public Problem butCausedBy(Problem causedBy) {
    return copied(but -> but.causedBy = causedBy);
  }

  public Seq<ValidationError> validationErrors() {
    return validationErrors;
  }

  public Problem butValidationErrors(Seq<ValidationError> validationErrors) {
    return copied(but -> but.validationErrors = validationErrors);
  }

  public boolean is(Problem tested) {
    return isOneOf(tested);
  }

  public boolean isOneOf(Problem... tested) {
    return HashSet.of(tested).contains(this);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("id", id)
      .append("occurredOn", occurredOn)
      .append("title", title)
      .append("details", details)
      .append("causedBy", causedBy)
      .append("validationErrors", validationErrors)
      .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Problem problem = (Problem) o;

    if (id != null ? !id.equals(problem.id) : problem.id != null) return false;
    if (occurredOn != null ? !occurredOn.equals(problem.occurredOn) : problem.occurredOn != null) return false;
    if (title != null ? !title.equals(problem.title) : problem.title != null) return false;
    if (details != null ? !details.equals(problem.details) : problem.details != null) return false;
    if (causedBy != null ? !causedBy.equals(problem.causedBy) : problem.causedBy != null) return false;
    return validationErrors != null ? validationErrors.equals(problem.validationErrors) : problem.validationErrors == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (occurredOn != null ? occurredOn.hashCode() : 0);
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (details != null ? details.hashCode() : 0);
    result = 31 * result + (causedBy != null ? causedBy.hashCode() : 0);
    result = 31 * result + (validationErrors != null ? validationErrors.hashCode() : 0);
    return result;
  }

  public static class Builder {

    Problem workpiece;

    public Builder() {
      workpiece = new Problem();
    }

    public Builder(Problem workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withTitle(String title) {
      return new Builder(workpiece.copied(but -> but.title = title));
    }

    public Builder withDetails(String details) {
      return new Builder(workpiece.copied(but -> but.details = details));
    }

    public Builder withCausedBy(Problem causedBy) {
      return new Builder(workpiece.copied(but -> but.causedBy = causedBy));
    }

    public Builder withValidationErrors(Seq<ValidationError> validationErrors) {
      return new Builder(workpiece.copied(but -> but.validationErrors = validationErrors));
    }

    public Problem build() {
      return workpiece;
    }
  }
}

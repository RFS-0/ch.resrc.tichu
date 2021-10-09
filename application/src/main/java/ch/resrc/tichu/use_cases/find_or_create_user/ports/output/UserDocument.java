package ch.resrc.tichu.use_cases.find_or_create_user.ports.output;

import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.entities.User;
import ch.resrc.tichu.domain.value_objects.Email;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.time.Instant;
import java.util.function.Consumer;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;
import static ch.resrc.tichu.domain.validation.DomainValidationErrors.mustNotBeNull;

public class UserDocument {

  private Id id;
  private Name name;
  private Email email;
  private Instant createdAt;

  private UserDocument() {
  }

  public UserDocument(UserDocument other) {
    this.id = other.id;
    this.name = other.name;
    this.email = other.email;
    this.createdAt = other.createdAt;
  }

  public static UserDocument.Builder aUserDocument() {
    return new Builder();
  }

  public static UserDocument fromUser(User user) {
    return UserDocument.aUserDocument()
      .withId(user.id())
      .withName(user.name())
      .withEmail(user.email())
      .withCreatedAt(user.createdAt())
      .build();
  }

  private UserDocument copied(Consumer<UserDocument> modification) {
    final var theCopy = new UserDocument(this);
    modification.accept(theCopy);
    return theCopy;
  }

  private static Validation<Seq<ValidationError>, UserDocument> validation() {
    return allOf(
      attribute(x -> x.id, notNull(mustNotBeNull())),
      attribute(x -> x.name, notNull(mustNotBeNull())),
      attribute(x -> x.email, notNull(mustNotBeNull())),
      attribute(x -> x.createdAt, notNull(mustNotBeNull()))
    );
  }

  public Id id() {
    return id;
  }

  public Email email() {
    return email;
  }

  public Name name() {
    return name;
  }

  public Instant createdAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserDocument that = (UserDocument) o;

    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  public static class Builder {

    private final UserDocument workpiece;

    private Builder() {
      this.workpiece = new UserDocument();
    }

    private Builder(UserDocument workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withId(Id id) {
      return new Builder(workpiece.copied(but -> but.id = id));
    }

    public Builder withEmail(Email email) {
      return new Builder(workpiece.copied(but -> but.email = email));
    }

    public Builder withName(Name name) {
      return new Builder(workpiece.copied(but -> but.name = name));
    }

    public Builder withCreatedAt(Instant createdAt) {
      return new Builder(workpiece.copied(but -> but.createdAt = createdAt));
    }

    public Either<Seq<ValidationError>, UserDocument> buildResult() {
      return validation().applyTo(workpiece);
    }

    UserDocument build() {
      return buildResult().getOrElseThrow(InvalidInputDetected::of);
    }
  }
}

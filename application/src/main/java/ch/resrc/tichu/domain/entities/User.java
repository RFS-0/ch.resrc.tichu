package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
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

public class User {

  private final Id id;
  private final Name name;
  private final Email email;
  private final Instant createdAt;

  private static Validation<Seq<ValidationError>, User> validation() {
    return allOf(
      attribute(x -> x.id, notNull(mustNotBeNull())),
      attribute(x -> x.email, notNull(mustNotBeNull())),
      attribute(x -> x.name, notNull(mustNotBeNull())),
      attribute(x -> x.createdAt, notNull(mustNotBeNull()))
    );
  }

  public User(Id id, Name name, Email email, Instant createdAt) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.createdAt = createdAt;
  }

  public User(User other) {
    this.id = other.id;
    this.email = other.email;
    this.name = other.name;
    this.createdAt = other.createdAt;
  }

  public User copied(Consumer<User> modification) {
    final var theCopy = new User(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static Either<Seq<ValidationError>, User> create(Id id, Name name, Email mail, Instant createdAt) {
    return validation().applyTo(new User(id, name, mail, createdAt));
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
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    User user = (User) o;

    return id.equals(user.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}

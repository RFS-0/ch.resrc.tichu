package ch.resrc.old.domain.entities;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.time.*;
import java.util.function.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

public class Player {

  private Id id;
  private Name name;
  private Instant createdAt;

  private static Validation<Seq<ValidationError>, Player> validation() {
    return Validations.allOf(
      attribute(x -> x.id, notNull(mustNotBeNull())),
      attribute(x -> x.name, notNull(mustNotBeNull())),
      attribute(x -> x.createdAt, notNull(mustNotBeNull()))
    );
  }

  private Player(Id id, Name name, Instant createdAt) {
    this.id = id;
    this.name = name;
    this.createdAt = createdAt;
  }

  private Player(Player other) {
    this.id = other.id;
    this.name = other.name;
    this.createdAt = other.createdAt;
  }

  public Player copied(Consumer<Player> modification) {
    final var theCopy = new Player(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static Either<Seq<ValidationError>, Player> create(Id id, Name name, Instant createdAt) {
    return validation().applyTo(new Player(id, name, createdAt));
  }

  public Id id() {
    return id;
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

    Player player = (Player) o;

    return id.equals(player.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}

package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.function.Consumer;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;
import static ch.resrc.tichu.domain.validation.DomainValidationErrors.mustNotBeNull;

public class Team {

  private final Id id;
  private Name name;
  private Player firstPlayer;
  private Player secondPlayer;

  private static Validation<Seq<ValidationError>, Team> validation() {
    return allOf(
      attribute(x -> x.id, notNull(mustNotBeNull()))
    );
  }

  private Team(Id id) {
    this.id = id;
    this.name = null;
    this.firstPlayer = null;
    this.secondPlayer = null;
  }

  private Team(Team other) {
    this.id = other.id;
    this.name = other.name;
    this.firstPlayer = other.firstPlayer;
    this.secondPlayer = other.secondPlayer;
  }

  public Team copied(Consumer<Team> modification) {
    final var theCopy = new Team(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static Either<Seq<ValidationError>, Team> create(Id id) {
    return validation().applyTo(new Team(id));
  }

  public Id id() {
    return id;
  }

  public Name name() {
    return name;
  }

  public Team butName(Name name) {
    return copied(team -> team.name = name);
  }

  public Player firstPlayer() {
    return firstPlayer;
  }

  public Team butFirstPlayer(Player firstPlayer) {
    return copied(team -> team.firstPlayer = firstPlayer);
  }

  public Player secondPlayer() {
    return secondPlayer;
  }

  public Team butSecondPlayer(Player secondPlayer) {
    return copied(team -> team.secondPlayer = secondPlayer);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Team team = (Team) o;

    return id.equals(team.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}

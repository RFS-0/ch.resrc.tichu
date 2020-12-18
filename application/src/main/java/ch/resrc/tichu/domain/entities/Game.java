package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.JoinCode;
import ch.resrc.tichu.domain.value_objects.Round;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import io.vavr.control.Either;

import java.time.Instant;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;
import static ch.resrc.tichu.domain.entities.GameValidationErrors.MUST_NOT_BE_NULL;

public class Game {

  private final Id id;
  private final User createdBy;
  private final JoinCode joinCode;
  private final Set<Team> teams;
  private final Set<Round> rounds;
  private final Instant createdAt;
  private Instant finishedAt;

  private static Validation<Seq<ValidationError>, Game> validation() {
    return allOf(
      attribute(x -> x.id, notNull(MUST_NOT_BE_NULL)),
      attribute(x -> x.createdBy, notNull(MUST_NOT_BE_NULL)),
      attribute(x -> x.joinCode, notNull(MUST_NOT_BE_NULL)),
      attribute(x -> x.teams, notNull(MUST_NOT_BE_NULL)),
      attribute(x -> x.rounds, notNull(MUST_NOT_BE_NULL))
    );
  }

  private Game(Id id, User createdBy, JoinCode joinCode, Set<Team> teams, Set<Round> rounds, Instant createdAt) {
    this.id = id;
    this.createdBy = createdBy;
    this.joinCode = joinCode;
    this.teams = teams;
    this.rounds = rounds;
    this.createdAt = createdAt;
  }

  private Game(Game other) {
    this.id = other.id;
    this.createdBy = other.createdBy;
    this.joinCode = other.joinCode;
    this.teams = other.teams;
    this.rounds = other.rounds;
    this.createdAt = other.createdAt;
    this.finishedAt = other.createdAt;
  }

  public Game copied(Consumer<Game> modification) {
    final var theCopy = new Game(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static Either<Seq<ValidationError>, Game> create(Id id, User createdBy, JoinCode joinCode, Set<Team> teams, Set<Round> rounds, Instant createdAt) {
    return validation().applyTo(new Game(id, createdBy, joinCode, teams, rounds, createdAt));
  }

  public Id id() {
    return id;
  }

  public User createdBy() {
    return createdBy;
  }

  public JoinCode joinCode() {
    return joinCode;
  }

  public Set<Team> teams() {
    return teams;
  }

  public Set<Round> rounds() {
    return rounds;
  }

  public Instant createdAt() {
    return createdAt;
  }

  public Instant finishedAt() {
    return finishedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Game game = (Game) o;

    return id.equals(game.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}

class GameValidationErrors {

  static final Supplier<ValidationError> MUST_NOT_BE_NULL = () -> ValidationError.of(
    Game.class.getName(), "must not be null"
  );
}

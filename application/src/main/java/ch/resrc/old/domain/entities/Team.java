package ch.resrc.old.domain.entities;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.validation.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.util.function.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;

public class Team {

  private final Id id;
  private Name name;
  private Player firstPlayer;
  private Player secondPlayer;

  private static Validation<Seq<ValidationError>, Team> validation() {
    return allOf(
      Validations.attribute(x -> x.id, Validations.notNull(DomainValidationErrors.mustNotBeNull()))
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

  public static Team create() {
    return new Team(Id.next());
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

  public Tuple2<Player, Player> players() {
    return Tuple.of(firstPlayer, secondPlayer);
  }

  public Seq<Id> playerIds() {
    return List.of(firstPlayer.id(), secondPlayer.id());
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

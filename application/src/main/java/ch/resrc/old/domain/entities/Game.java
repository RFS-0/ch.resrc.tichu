package ch.resrc.old.domain.entities;

import ch.resrc.old.capabilities.errorhandling.*;
import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.time.*;
import java.util.function.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

public class Game {

  private final Id id;
  private final User createdBy;
  private final JoinCode joinCode;
  private Set<Team> teams;
  private Set<Round> rounds;
  private final Instant createdAt;
  private Instant finishedAt;

  private Game(Id id,
               User createdBy,
               JoinCode joinCode,
               Set<Team> teams,
               Set<Round> rounds,
               Instant createdAt) {
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

  private static Validation<Seq<ValidationError>, Game> validation() {
    return allOf(
      attribute(x -> x.id, notNull(mustNotBeNull())),
      attribute(x -> x.createdBy, notNull(mustNotBeNull())),
      attribute(x -> x.joinCode, notNull(mustNotBeNull())),
      attribute(x -> x.teams, notNull(mustNotBeNull())),
      attribute(x -> x.rounds, notNull(mustNotBeNull()))
    );
  }

  public static Either<Seq<ValidationError>, Game> create(Id id, User createdBy, JoinCode joinCode, Set<Team> teams, Set<Round> rounds, Instant createdAt) {
    return validation().applyTo(new Game(id, createdBy, joinCode, teams, rounds, createdAt));
  }

  public boolean finished() {
    return teams
      .map(team -> totalPoints(team.id()))
      .exists(totalPoints -> totalPoints >= 1000);
  }

  public boolean notFinished() {
    return !finished();
  }

  public int totalPoints(Id teamId) {
    Tuple2<Player, Player> players = teams
      .filter(team -> team.id().equals(teamId))
      .map(Team::players)
      .getOrElseThrow(() -> DomainProblemDetected.of(DomainProblem.INVARIANT_VIOLATED));

    return rounds
      .map(round -> round.totalPoints(teamId, players._1.id(), players._2.id()))
      .sum()
      .intValue();
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

  public Game butTeam(Team team) {
    return copied(game -> game.teams = game.teams.remove(team).add(team));
  }

  public Game butRound(Round round) {
    return copied(game -> game.rounds = game.rounds.remove(round).add(round));
  }

  public Game addRound(RoundNumber roundNumber) {
    Round roundToAdd = Round.resultOf(
      roundNumber,
      initialCardPoints(),
      initialRanks(),
      initialTichus()
    ).getOrElseThrow(() -> DomainProblemDetected.of(DomainProblem.INVARIANT_VIOLATED));

    return butRound(roundToAdd);
  }

  private CardPoints initialCardPoints() {
    return CardPoints.resultOf(
      teams
        .map(team -> Tuple.of(team.id(), 0))
        .foldLeft(HashMap.empty(), (result, entry) -> result.put(entry._1, entry._2))
    ).getOrElseThrow(() -> DomainProblemDetected.of(DomainProblem.INVARIANT_VIOLATED));
  }

  private Ranks initialRanks() {
    return Ranks.resultOf(
      teams
        .flatMap(Team::playerIds)
        .foldLeft(HashMap.empty(), (result, entry) -> result.put(entry, 0)))
      .getOrElseThrow(() -> DomainProblemDetected.of(DomainProblem.INVARIANT_VIOLATED));
  }

  private Tichus initialTichus() {
    return Tichus.resultOf(
      teams
        .flatMap(Team::playerIds)
        .foldLeft(HashMap.empty(), (result, entry) -> result.put(entry, Tichu.NONE))
    )
      .getOrElseThrow(() -> DomainProblemDetected.of(DomainProblem.INVARIANT_VIOLATED));
  }

  public Set<Round> rounds() {
    return rounds;
  }

  public Instant createdAt() {
    return createdAt;
  }

  public Game butFinishedAt(Instant finishedAt) {
    return copied(game -> game.finishedAt = finishedAt);
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


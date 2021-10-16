package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;
import ch.resrc.tichu.domain.errorhandling.*;
import ch.resrc.tichu.domain.value_objects.*;
import io.vavr.*;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Set;

import java.time.*;
import java.util.*;
import java.util.function.*;

import static ch.resrc.tichu.capabilities.validation.Validations.*;
import static ch.resrc.tichu.domain.validation.DomainValidations.*;

public class Game {

    private final Id id;
    private final User createdBy;
    private final JoinCode joinCode;
    private Set<Team> teams;
    private List<Round> rounds;
    private final Instant createdAt;
    private Instant finishedAt;

    private Game(Id id,
                 User createdBy,
                 JoinCode joinCode,
                 Set<Team> teams,
                 List<Round> rounds,
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

    private static Validation<Game, ValidationError> validation() {
        return allOf(
                attribute(x -> x.id, notNull()),
                attribute(x -> x.createdBy, notNull()),
                attribute(x -> x.joinCode, notNull()),
                attribute(x -> x.teams, notNull()),
                attribute(x -> x.rounds, notNull())
        );
    }

    public static Result<Game, ValidationError> resultOf(Id id, User createdBy, JoinCode joinCode, Set<Team> teams, List<Round> rounds, Instant createdAt) {
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
        return copied(game -> game.rounds = game.rounds.remove(round).append(round));
    }

    public Game addRound() {
        Round roundToAdd = Round.resultOf(
                rounds.last().roundNumber().increment(),
                initialCardPoints(),
                initialRanks(),
                initialTichus()
        ).getOrThrow(invariantViolated());

        return butRound(roundToAdd);
    }

    private CardPoints initialCardPoints() {
        return CardPoints.resultOf(
                teams.map(Team::id)
                        .foldLeft(HashMap.empty(), (teamIdsToPoints, id) -> teamIdsToPoints.put(id, 0))
        ).getOrThrow(invariantViolated());
    }

    private Ranks initialRanks() {
        return Ranks.resultOf(
                        teams
                                .flatMap(Team::playerIds)
                                .foldLeft(HashMap.empty(), (result, entry) -> result.put(entry, Rank.NONE)))
                .getOrThrow(invariantViolated());
    }

    private Tichus initialTichus() {
        return Tichus.resultOf(
                        teams
                                .flatMap(Team::playerIds)
                                .foldLeft(HashMap.empty(), (result, entry) -> result.put(entry, Tichu.NONE))
                )
                .getOrThrow(invariantViolated());
    }

    public List<Round> rounds() {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id.equals(game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


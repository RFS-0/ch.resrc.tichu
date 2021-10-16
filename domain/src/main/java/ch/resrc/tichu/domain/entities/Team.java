package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;
import ch.resrc.tichu.domain.value_objects.*;
import io.vavr.*;
import io.vavr.collection.List;
import io.vavr.collection.*;

import java.util.*;
import java.util.function.*;

import static ch.resrc.tichu.capabilities.validation.Validations.*;

public class Team {

    private final Id id;
    private Name name;
    private Player firstPlayer;
    private Player secondPlayer;

    private static Validation<Team, ValidationError> validation() {
        return attribute(x -> x.id, notNull());
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

    public static Result<Team, ValidationError> resultOf(Id id) {
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

    public Tuple2<Player, Player> players() {
        return Tuple.of(firstPlayer, secondPlayer);
    }

    public Seq<Id> playerIds() {
        return List.of(firstPlayer.id(), secondPlayer.id());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id.equals(team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

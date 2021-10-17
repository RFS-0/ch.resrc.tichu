package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;
import ch.resrc.tichu.domain.value_objects.*;

import java.time.*;
import java.util.*;
import java.util.function.*;

import static ch.resrc.tichu.capabilities.validation.Validations.*;

public class Player {

    private final PlayerId id;
    private Name name;
    private Instant createdAt;

    private static Validation<Player, ValidationError> validation() {
        return allOf(
                attribute(x -> x.id, notNull()),
                attribute(x -> x.name, notNull()),
                attribute(x -> x.createdAt, notNull())
        );
    }

    private Player(PlayerId id, Name name, Instant createdAt) {
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

    public static Result<Player, ValidationError> resultOf(PlayerId id, Name name, Instant createdAt) {
        return validation().applyTo(new Player(id, name, createdAt));
    }

    public PlayerId id() {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

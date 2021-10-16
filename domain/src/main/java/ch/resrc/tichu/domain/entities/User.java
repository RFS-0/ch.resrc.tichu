package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;
import ch.resrc.tichu.domain.value_objects.*;

import java.time.*;
import java.util.*;
import java.util.function.*;

import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.*;
import static ch.resrc.tichu.capabilities.validation.Validations.*;

public class User {

    private final Id id;
    private final Name name;
    private final Email email;
    private final Instant createdAt;

    private static Validation<User, ValidationError> validation() {
        return modified(
                allOf(
                        attribute(x -> x.id, notNull()),
                        attribute(x -> x.email, notNull()),
                        attribute(x -> x.name, notNull()),
                        attribute(x -> x.createdAt, notNull())
                ),
                context(User.class)
        );
    }

    private User(Id id, Name name, Email email, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.createdAt = createdAt;
    }

    private User(User other) {
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

    public static Result<User, ValidationError> resultOf(Id id, Name name, Email mail, Instant createdAt) {
        return validation().applyTo(new User(id, name, mail, createdAt));
    }

    static Result<User, ValidationError> create(Supplier<Result<User, ValidationError>> creationFunction) {

        return creationFunction.get();
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
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package ch.resrc.tichu.test.capabilities.habits.fixtures;

import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.entities.*;
import ch.resrc.tichu.domain.value_objects.*;

import java.time.Instant;
import java.util.function.Consumer;

public class UserSpec {

    private UserId id = UserId.of("8f1f244f-ef18-4393-916b-70f5ded89442");
    private Name name = Name.of("Tichu User");
    private Email email = Email.of("user@tichu.resrc.ch");
    private Instant createdAt = Instant.now();

    private UserSpec() {
    }

    public UserSpec(UserSpec other) {
        this.id = other.id;
        this.name = other.name;
        this.email = other.email;
        this.createdAt = other.createdAt;
    }

    private UserSpec copied(Consumer<UserSpec> modification) {
        var copy = new UserSpec(this);
        modification.accept(copy);
        return copy;
    }

    public static UserSpec user() {
        return new UserSpec();
    }

    public UserSpec id(UserId id) {
        return copied(but -> but.id = id);
    }

    public UserSpec name(Name name) {
        return copied(but -> but.name = name);
    }

    public UserSpec email(Email email) {
        return copied(but -> but.email = email);
    }

    public UserSpec createdAt(Instant createdAt) {
        return copied(but -> but.createdAt = createdAt);
    }

    public Result<User, ValidationError> asResult() {
        return User.resultOf(
                id,
                name,
                email,
                createdAt
        );
    }

    public User asEntity() {
        return asResult().value();
    }
}

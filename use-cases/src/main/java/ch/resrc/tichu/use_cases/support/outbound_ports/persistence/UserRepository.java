package ch.resrc.tichu.use_cases.support.outbound_ports.persistence;

import ch.resrc.tichu.domain.entities.*;
import ch.resrc.tichu.domain.value_objects.Email;
import io.vavr.collection.List;

import java.util.Optional;

public interface UserRepository {

    void store(User user);

    List<User> findAll();

    Optional<User> findUserById(UserId userId);

    Optional<User> findUserByEmail(Email email);
}

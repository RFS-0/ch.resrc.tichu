package ch.resrc.tichu.adapters.persistence.in_memory;

import ch.resrc.tichu.domain.entities.*;
import ch.resrc.tichu.domain.value_objects.Email;
import ch.resrc.tichu.use_cases.support.outbound_ports.persistence.UserRepository;
import io.vavr.collection.*;

import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

    private HashMap<UserId, User> users = HashMap.empty();

    @Override
    public void store(User user) {
        users = users.put(user.id(), user);
    }

    @Override
    public List<User> findAll() {
        return users.values().toList();
    }

    @Override
    public Optional<User> findUserById(UserId userId) {
        return users.get(userId).toJavaOptional();
    }

    @Override
    public Optional<User> findUserByEmail(Email email) {
        return users.values()
                .find(user -> user.email().equals(email))
                .toJavaOptional();
    }
}

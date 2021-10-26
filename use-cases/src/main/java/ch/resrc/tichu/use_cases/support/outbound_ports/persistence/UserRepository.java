package ch.resrc.tichu.use_cases.support.outbound_ports.persistence;

import ch.resrc.tichu.domain.entities.*;
import ch.resrc.tichu.domain.value_objects.*;

import java.util.*;

public interface UserRepository {

    Optional<User> findUserByNameAndEmail(Name name, Email email);
}

package ch.resrc.tichu.adapters.persistence.in_memory;

import ch.resrc.tichu.domain.entities.UserId;
import ch.resrc.tichu.domain.value_objects.Otp;
import ch.resrc.tichu.use_cases.support.outbound_ports.persistence.OtpRepository;
import io.vavr.collection.HashMap;

import java.util.Optional;

public class InMemoryOtpRepository implements OtpRepository {

    private HashMap<UserId, Otp> oneTimePasswords = HashMap.empty();

    @Override
    public Optional<Otp> findByUserId(UserId userId) {
        return oneTimePasswords.get(userId).toJavaOptional();
    }

    @Override
    public void store(UserId userId, Otp otp) {
        oneTimePasswords = oneTimePasswords.put(userId, otp);
    }
}

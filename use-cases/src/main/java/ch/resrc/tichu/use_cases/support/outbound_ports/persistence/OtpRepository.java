package ch.resrc.tichu.use_cases.support.outbound_ports.persistence;

import ch.resrc.tichu.domain.entities.UserId;
import ch.resrc.tichu.domain.value_objects.Otp;

import java.util.Optional;

public interface OtpRepository {

    Optional<Otp> findByUserId(UserId userId);

    void store(UserId userId, Otp otp);
}

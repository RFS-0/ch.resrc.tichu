package ch.resrc.tichu.test.capabilities.habits.use_cases;

import ch.resrc.tichu.domain.entities.UserId;
import ch.resrc.tichu.use_cases.create_otp_for_user.CreateOtpUseCase;
import ch.resrc.tichu.use_cases.create_otp_for_user.ports.inbound.CreateOtp;

public interface CreateOtpHabits
        extends
        PortsHabits {

    default CreateOtp newCreateOtp() {
        return CreateOtpUseCase.create(new CreateOtpUseCase.OutboundPorts(
                        ports().userRepository(),
                        UserId::random,
                        ports().otpRepository()
                )
        );
    }
}

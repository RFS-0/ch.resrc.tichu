package ch.resrc.tichu.use_cases.validate_otp.ports.inbound;

import ch.resrc.tichu.domain.entities.UserId;
import ch.resrc.tichu.domain.value_objects.Otp;
import ch.resrc.tichu.use_cases.support.outbound_ports.authentication.Client;
import ch.resrc.tichu.use_cases.validate_otp.ports.outbound.OtpValidationPresenter;

public interface ValidateOtp {

    void invoke(Input input, OtpValidationPresenter presenter);

    class Input {

        private final Client client;
        private final UserId userId;
        private final Otp otp;

        public Input(Client client, UserId userId, Otp otp) {
            this.client = client;
            this.userId = userId;
            this.otp = otp;
        }

        public Client client() {
            return client;
        }

        public UserId userId() {
            return userId;
        }

        public Otp otp() {
            return otp;
        }
    }
}

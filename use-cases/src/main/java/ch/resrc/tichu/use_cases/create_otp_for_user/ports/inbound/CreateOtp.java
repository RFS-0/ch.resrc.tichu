package ch.resrc.tichu.use_cases.create_otp_for_user.ports.inbound;

import ch.resrc.tichu.domain.value_objects.Email;
import ch.resrc.tichu.use_cases.create_otp_for_user.ports.outbound.OtpPresenter;
import ch.resrc.tichu.use_cases.support.outbound_ports.authentication.Client;

public interface CreateOtp {

    void invoke(Input input, OtpPresenter presenter);

    class Input {

        private final Client client;
        private final Email email;

        public Input(Client client, Email email) {
            this.client = client;
            this.email = email;
        }

        public Client client() {
            return client;
        }

        public Email email() {
            return email;
        }
    }
}

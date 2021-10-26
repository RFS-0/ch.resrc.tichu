package ch.resrc.tichu.use_cases.create_otp_for_user.ports.inbound;

import ch.resrc.tichu.domain.value_objects.*;
import ch.resrc.tichu.use_cases.support.outbound_ports.presentation.*;

public interface CreateOtpForUser {

    void invoke(Input input, ErrorPresenter presenter);

    class Input {

        private final Name name;
        private final Email email;

        public Input(Name name, Email email) {
            this.name = name;
            this.email = email;
        }
    }
}

package ch.resrc.tichu.test.capabilities.habits.use_cases;

import ch.resrc.tichu.domain.value_objects.Email;
import ch.resrc.tichu.use_cases.create_otp_for_user.ports.inbound.CreateOtp;
import ch.resrc.tichu.use_cases.support.outbound_ports.authentication.Client;

import java.util.function.Consumer;

public class CreateOtpInputSpec {

    private Client client;
    private Email email;

    private CreateOtpInputSpec() {
    }

    public CreateOtpInputSpec(CreateOtpInputSpec other) {
        this.client = other.client;
        this.email = other.email;
    }

    private CreateOtpInputSpec copied(Consumer<CreateOtpInputSpec> modification) {
        var copy = new CreateOtpInputSpec(this);
        modification.accept(copy);
        return copy;
    }

    public static CreateOtpInputSpec createOtpInput() {
        return new CreateOtpInputSpec();
    }

    public CreateOtpInputSpec client(Client client) {
        return copied(but -> but.client = client);
    }

    public CreateOtpInputSpec email(Email email) {
        return copied(but -> but.email = email);
    }

    public CreateOtp.Input asInput() {
        return new CreateOtp.Input(client, email);
    }
}

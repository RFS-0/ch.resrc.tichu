package ch.resrc.tichu.use_cases.create_otp_for_user.ports.outbound;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.use_cases.create_otp_for_user.ports.documents.OtpDocument;
import ch.resrc.tichu.use_cases.support.outbound_ports.authentication.Client;
import ch.resrc.tichu.use_cases.support.outbound_ports.presentation.ErrorPresenter;

public interface OtpPresenter extends ErrorPresenter {

    void present(Client client, OtpDocument toBePresented);

    void present(Client client, ValidationError validationError);
}

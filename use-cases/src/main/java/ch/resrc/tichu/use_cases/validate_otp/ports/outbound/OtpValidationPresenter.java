package ch.resrc.tichu.use_cases.validate_otp.ports.outbound;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.use_cases.support.outbound_ports.authentication.Client;
import ch.resrc.tichu.use_cases.support.outbound_ports.presentation.ErrorPresenter;
import ch.resrc.tichu.use_cases.validate_otp.ports.documents.OtpValidationDocument;

public interface OtpValidationPresenter extends ErrorPresenter {

    void present(Client client, OtpValidationDocument toBePresented);

    void present(Client client, ValidationError validationError);
}

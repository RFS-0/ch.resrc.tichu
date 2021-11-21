package ch.resrc.tichu.use_cases.validate_otp.ports.events;

import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.domain.value_objects.OtpValidationResult;
import ch.resrc.tichu.use_cases.validate_otp.ports.documents.OtpValidationDocument;

public class OtpValidated extends Event {

    private final OtpValidationDocument document;

    private OtpValidated(OtpValidationDocument document) {
        super();
        this.document = document;
    }

    public static OtpValidated of(OtpValidationResult otpValidationResult) {
        return new OtpValidated(new OtpValidationDocument(otpValidationResult));
    }

    public OtpValidationDocument document() {
        return document;
    }
}

package ch.resrc.tichu.use_cases.validate_otp.ports.documents;

import ch.resrc.tichu.domain.value_objects.OtpValidationResult;

public class OtpValidationDocument {

    private final OtpValidationResult otpValidationResult;

    public OtpValidationDocument(OtpValidationResult otpValidationResult) {
        this.otpValidationResult = otpValidationResult;
    }
}

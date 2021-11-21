package ch.resrc.tichu.use_cases.create_otp_for_user.ports.documents;

import ch.resrc.tichu.domain.value_objects.Otp;

public class OtpDocument {

    private final Otp otp;

    public OtpDocument(Otp otp) {
        this.otp = otp;
    }

    public Otp oneTimePassword() {
        return otp;
    }
}

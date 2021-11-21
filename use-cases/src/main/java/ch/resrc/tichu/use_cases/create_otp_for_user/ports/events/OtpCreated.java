package ch.resrc.tichu.use_cases.create_otp_for_user.ports.events;

import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.domain.value_objects.Otp;
import ch.resrc.tichu.use_cases.create_otp_for_user.ports.documents.OtpDocument;

public class OtpCreated extends Event {

    private final OtpDocument document;

    private OtpCreated(OtpDocument document) {
        super();
        this.document = document;
    }

    public static OtpCreated of(Otp otp) {
        return new OtpCreated(new OtpDocument(otp));
    }

    public OtpDocument document() {
        return document;
    }
}

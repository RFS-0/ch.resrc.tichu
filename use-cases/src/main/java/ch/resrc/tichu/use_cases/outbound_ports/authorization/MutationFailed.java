package ch.resrc.tichu.use_cases.outbound_ports.authorization;

import ch.resrc.tichu.capabilities.error_handling.*;
import ch.resrc.tichu.capabilities.events.*;

public class MutationFailed extends Event {

    private final HavingDiagnosis error;

    private MutationFailed(HavingDiagnosis error) {

        this.error = error;
    }

    public static MutationFailed of(HavingDiagnosis error) {
        return new MutationFailed(error);
    }

    public HavingDiagnosis error() { return error; }
}

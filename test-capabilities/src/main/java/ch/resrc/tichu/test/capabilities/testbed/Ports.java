package ch.resrc.tichu.test.capabilities.testbed;

import ch.resrc.tichu.use_cases.support.outbound_ports.authorization.AccessControl;
import ch.resrc.tichu.use_cases.support.outbound_ports.eventbus.EventBus;
import ch.resrc.tichu.use_cases.support.outbound_ports.persistence.*;
import ch.resrc.tichu.use_cases.support.outbound_ports.transaction.*;

public interface Ports {

    EventBus eventBus();

    NewTransaction newTransaction();

    EnsuredTransaction ensuredTransaction();

    AccessControl accessControl();

    UserRepository userRepository();

    OtpRepository otpRepository();
}

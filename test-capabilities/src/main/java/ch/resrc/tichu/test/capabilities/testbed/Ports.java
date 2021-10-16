package ch.resrc.tichu.test.capabilities.testbed;

import ch.resrc.tichu.use_cases.outbound_ports.authorization.*;
import ch.resrc.tichu.use_cases.outbound_ports.eventbus.*;
import ch.resrc.tichu.use_cases.outbound_ports.transaction.*;

public interface Ports {

    EventBus eventBus();

    NewTransaction newTransaction();

    EnsuredTransaction ensuredTransaction();

    AccessControl accessControl();
}
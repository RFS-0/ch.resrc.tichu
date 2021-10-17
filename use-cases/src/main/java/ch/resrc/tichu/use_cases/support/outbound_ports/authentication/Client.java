package ch.resrc.tichu.use_cases.support.outbound_ports.authentication;

import ch.resrc.tichu.domain.value_objects.*;

public interface Client {

    ClientId id();

    default NamespaceId namespace() {

        return NamespaceId.publicNamespace();
    }

    static Client anonymous() {
        return Client.of(ClientId.of("dfa7a524-de08-4c81-87e6-ba1ed6216f14"));
    }

    static Client of(ClientId id) {
        return () -> id;
    }
}

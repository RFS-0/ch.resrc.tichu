package ch.resrc.tichu.endpoints.input;

import ch.resrc.tichu.ports.outbound.authentication.Client;

public interface ClientInput {

  default Client client() {
    return Client.anonymous();
  }
}

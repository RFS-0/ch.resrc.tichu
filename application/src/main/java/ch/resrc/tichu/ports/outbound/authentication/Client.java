package ch.resrc.tichu.ports.outbound.authentication;


import ch.resrc.tichu.domain.value_objects.Id;

public interface Client {

  Id id();

  static Client anonymous() {
    return Client.of(Id.of("dfa7a524-de08-4c81-87e6-ba1ed6216f14"));
  }

  static Client of(Id id) {
    return () -> id;
  }
}

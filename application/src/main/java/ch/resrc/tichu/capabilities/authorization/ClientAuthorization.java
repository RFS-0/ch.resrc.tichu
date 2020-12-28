package ch.resrc.tichu.capabilities.authorization;

import static ch.resrc.tichu.capabilities.errorhandling.Blame.expectClientAccessDenied;
import static java.util.Objects.requireNonNull;

import ch.resrc.tichu.capabilities.errorhandling.faults.ClientFault;
import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.capabilities.events.Events;
import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.ports.outbound.authentication.Client;
import ch.resrc.tichu.ports.outbound.authorization.AccessControl;
import ch.resrc.tichu.ports.outbound.authorization.UseCaseInvoked;
import ch.resrc.tichu.usecases.UseCasePort;

/**
 * Authorizes requests and data access of a use case client using a given {@link AccessControl}.
 */
public class ClientAuthorization implements AccessAuthorization {

  private final Client client;
  private final AccessControl accessControl;
  private final Class<? extends UseCasePort> invocationPort;

  /**
   * Creates an instances that represents the given client and which uses the specified access control for authorizing the client's
   * data access.
   *
   * @param client         identifies the client that gets represented by this role
   * @param accessControl  the access control that is responsible for authorizing the client's data access.
   * @param invocationPort the port interface on which the use case that uses this object was invoked.
   */
  public ClientAuthorization(Class<? extends UseCasePort> invocationPort,
    Client client,
    AccessControl accessControl) {
    this.client = requireNonNull(client);
    this.accessControl = requireNonNull(accessControl);
    this.invocationPort = requireNonNull(invocationPort);
  }

  public Result<Void, ClientFault> authorize(Events toBeAuthorized) {
    return accessControl.authorize(toBeAuthorized, client.id())
      .handleErrors(expectClientAccessDenied())
      .yieldVoidSuccess();
  }

  public Result<Void, ClientFault> authorize(Event toBeAuthorized) {
    return this.authorize(Events.of(toBeAuthorized));
  }

  public Result<Void, ClientFault> authorizeUseCaseAccess() {
    return this.authorize(UseCaseInvoked.of(invocationPort));
  }

}

package ch.resrc.tichu.capabilities.authorization;

import ch.resrc.tichu.capabilities.errorhandling.faults.ClientFault;
import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.capabilities.events.Events;
import ch.resrc.tichu.capabilities.result.Result;
import java.util.function.Function;

public interface AccessAuthorization extends Function<Events, Result<Void, ClientFault>> {

  Result<Void, ClientFault> authorize(Events toBeAuthorized);

  default Result<Void, ClientFault> authorize(Event toBeAuthorized) {
    return authorize(Events.of(toBeAuthorized));
  }

  default Result<Void, ClientFault> apply(Events toBeAuthorized) {
    return authorize(toBeAuthorized);
  }

  static AccessAuthorization grantAll() {
    return events -> Result.voidSuccess();
  }
}

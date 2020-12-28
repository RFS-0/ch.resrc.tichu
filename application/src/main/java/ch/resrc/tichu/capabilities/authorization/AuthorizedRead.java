package ch.resrc.tichu.capabilities.authorization;

import ch.resrc.tichu.capabilities.errorhandling.faults.ClientFault;
import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.ports.outbound.authorization.DataAccessed;
import java.util.Optional;

public class AuthorizedRead {

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public static <T> Result<T, ClientFault> of(Optional<T> dataItem, AccessAuthorization auth) {
    return dataItem
      .map((T x) -> auth.authorize(DataAccessed.of(x)).yield(() -> x))
      .orElse(Result.empty());
  }

  public static <T> Result<T, ClientFault> of(T dataItem, AccessAuthorization auth) {
    return of(Optional.of(dataItem), auth);
  }
}

package ch.resrc.tichu.domain.validation;


import ch.resrc.tichu.capabilities.errorhandling.DomainError;
import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.domain.doto.DoTo;

public class DomainFlow<T> {

  public static <T> DoTo<T, DomainError> doTo(T domainObject) {
    return DoTo.of(domainObject, DomainError.class);
  }

  public static <T> DoTo<T, DomainError> doTo(Result<T, ? extends DomainError> domainResult) {
    return DoTo.of(domainResult, DomainError.class);
  }

}

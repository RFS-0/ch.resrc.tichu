package ch.resrc.tichu.domain.validation;

import ch.resrc.tichu.capabilities.doto.*;
import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.domain.errorhandling.*;

public class DomainFlow<T> {

    public static <T> DoTo<T, DomainError> doTo(T domainObject) {
        return DoTo.of(domainObject, DomainError.class);
    }

    public static <T> DoTo<T, DomainError> doTo(Result<T, ? extends DomainError> domainResult) {
        return DoTo.of(domainResult, DomainError.class);
    }
}

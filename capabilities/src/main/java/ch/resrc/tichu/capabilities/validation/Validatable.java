package ch.resrc.tichu.capabilities.validation;

import ch.resrc.tichu.capabilities.result.*;

public interface Validatable<T> {

    Result<T, ValidationError> validated();
}
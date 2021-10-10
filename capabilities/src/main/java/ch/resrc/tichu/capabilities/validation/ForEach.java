package ch.resrc.tichu.capabilities.validation;

import ch.resrc.tichu.capabilities.result.*;

import java.util.*;
import java.util.function.*;

import static ch.resrc.tichu.capabilities.functional.Mapped.*;
import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.*;

public class ForEach<T, E> implements Validation<Collection<T>, E> {

    private final Function<T, Validation<T, E>> getValidationForElement;

    public ForEach(Validation<T, E> elementValidation) {

        this.getValidationForElement = x -> elementValidation;
    }

    public ForEach(Function<T, Validation<T, E>> getValidationForElement) {

        this.getValidationForElement = getValidationForElement;
    }

    @Override
    public Result<Collection<T>, E> applyTo(Collection<T> toBeValidated) {

        if (toBeValidated == null) {
            return Result.success(null);
        }

        List<Result<?, E>> allValidated = mapIndexed(toBeValidated, (BiFunction<? super T, Integer, Result<?, E>>) this::validateElement);

        return Precondition.of(allValidated).thenValueOf(() -> toBeValidated);
    }

    private Result<T, E> validateElement(T elementToBeValidated, Integer index) {

        return getValidationForElement.apply(elementToBeValidated)
                                      .applyTo(elementToBeValidated)
                                      .mapErrors(withIndex(index));
    }

}

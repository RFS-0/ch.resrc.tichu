package ch.resrc.tichu.capabilities.validation;

import static ch.resrc.tichu.capabilities.functional.Mapped.mapIndexed;
import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.withIndex;

import ch.resrc.tichu.capabilities.result.Result;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

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

    return Result.combined(allValidated).yield(() -> toBeValidated);
  }

  private Result<T, E> validateElement(T elementToBeValidated, Integer index) {

    return getValidationForElement.apply(elementToBeValidated)
      .applyTo(elementToBeValidated)
      .mapErrors(withIndex(index));
  }

}

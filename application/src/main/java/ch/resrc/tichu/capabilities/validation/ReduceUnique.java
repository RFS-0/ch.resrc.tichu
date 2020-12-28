package ch.resrc.tichu.capabilities.validation;

import static java.util.stream.Collectors.toList;

import ch.resrc.tichu.capabilities.result.Result;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ReduceUnique {

  public static <T> Function<Collection<T>, Result<T, ValidationError>> reducedToUnique() {

    return (Collection<T> elements) -> {
      List<T> uniqueElements = elements.stream()
        .distinct()
        .collect(toList());

      if (uniqueElements.isEmpty()) {
        return Result.failure(ValidationError.of("Collection was empty", elements));
      }

      if (uniqueElements.size() > 1) {
        return Result.failure(ValidationError.of("Collection has non-unique values <" +
          elements + ">", uniqueElements));
      }

      return Result.success(uniqueElements.get(0));
    };
  }

  @SafeVarargs
  public static <T> Result<T, ValidationError> reducedToUnique(Collection<T> elements,
    UnaryOperator<ValidationError>... errorModifiers) {

    Result<T, ValidationError> result = ReduceUnique.<T>reducedToUnique().apply(elements);
    return Result.modifyErrors(result, errorModifiers);
  }

}

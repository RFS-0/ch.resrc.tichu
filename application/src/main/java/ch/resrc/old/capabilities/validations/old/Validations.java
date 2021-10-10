package ch.resrc.old.capabilities.validations.old;

import io.vavr.collection.List;
import io.vavr.collection.*;
import io.vavr.control.*;
import org.apache.commons.lang3.*;

import java.net.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;

public class Validations {

  @SafeVarargs
  public static <E, T> Validation<Seq<E>, T> allOf(Validation<E, T>... validations) {
    return (T toBeValidated) -> {
      final Seq<E> errors = List.of(validations)
        .map(validation -> validation.applyTo(toBeValidated))
        .flatMap(Either::swap);
      return errors.isEmpty() ? Either.right(toBeValidated) : Either.left(errors);
    };
  }

  @SafeVarargs
  public static <E, T> Validation<Seq<E>, T> chained(Validation<E, T>... validations) {
    return (T toBeValidated) -> {
      for (Validation<E, T> validation : List.of(validations)) {
        Either<E, T> validationResult = validation.applyTo(toBeValidated);
        if (validationResult.isLeft()) {
          return Either.left(List.of(validationResult.getLeft()));
        }
      }
      return Either.right(toBeValidated);
    };
  }

  public static <T, U, E> Validation<E, T> attribute(Function<T, U> attributeLense, Validation<E, U> attributeValidation) {
    return (T toBeValidated) -> attributeValidation.apply(attributeLense.apply(toBeValidated)).map(__ -> toBeValidated);
  }

  // GENERIC VALIDATIONS

  public static <T> Validation<ValidationError, T> isTrueOrError(Predicate<T> condition, Function<T, ValidationError> error) {
    return (T toBeValidated) -> {
      Validation<ValidationError, T> validation = (T input) -> condition.test(input) ? Either.right(input) : Either.left(error.apply(input));
      return validation.applyTo(toBeValidated);
    };
  }

  public static <T> Validation<ValidationError, T> isFalseOrError(Predicate<T> condition, Function<T, ValidationError> error) {
    return (T toBeValidated) -> {
      Validation<ValidationError, T> validation = (T input) -> !condition.test(input) ? Either.right(input) : Either.left(error.apply(input));
      return validation.applyTo(toBeValidated);
    };
  }

  public static <T> Validation<ValidationError, T> notNull(Function<T, ValidationError> error) {
    return toBeValidated -> Validations.isTrueOrError(Objects::nonNull, error).apply(toBeValidated);
  }

  // INTEGER VALIDATIONS

  public static Validation<ValidationError, Integer> min(int minimum, Function<Integer, ValidationError> error) {
    return isTrueOrError(x -> x >= minimum, error);
  }

  public static Validation<ValidationError, Seq<Integer>> allMin(int minimum, Function<Seq<Integer>, ValidationError> error) {
    return (Seq<Integer> values) -> values.exists(value -> value < minimum) ? Either.left(error.apply(values)) : Either.right(values);
  }

  public static Validation<ValidationError, Integer> max(int maximum, Function<Integer, ValidationError> error) {
    return isTrueOrError(x -> x <= maximum, error);
  }

  public static Validation<ValidationError, Seq<Integer>> allMax(int maximum, Function<Seq<Integer>, ValidationError> error) {
    return (Seq<Integer> values) -> values.exists(value -> value > maximum) ? Either.left(error.apply(values)) : Either.right(values);
  }

  public static Validation<ValidationError, Number> equalTo(Number number, Function<Number, ValidationError> error) {
    return isTrueOrError(x -> x.doubleValue() == number.doubleValue(), error);
  }

  public static Validation<ValidationError, Integer> divisibleBy(int divisor, Function<Integer, ValidationError> error) {
    return isTrueOrError(x -> x % divisor == 0, error);
  }

  public static Validation<ValidationError, Seq<Integer>> allDivisibleBy(int divisor, Function<Seq<Integer>, ValidationError> error) {
    return (Seq<Integer> values) -> values.exists(value -> value % divisor != 0) ? Either.left(error.apply(values)) : Either.right(values);
  }

  // STRING VALIDATIONS

  public static Validation<ValidationError, String> notBlank(Function<String, ValidationError> error) {
    return (String value) -> {
      if (StringUtils.isBlank(value)) {
        return Either.left(error.apply(value));
      } else {
        return Either.right(value);
      }
    };
  }

  public static Validation<ValidationError, String> matches(String regexp, Function<String, ValidationError> error) {
    return isTrueOrError(x -> Pattern.compile(regexp).matcher(x).matches(), error);
  }

  public static Validation<ValidationError, String> matches(String regexp, int flags, Function<String, ValidationError> error) {
    return isTrueOrError(x -> Pattern.compile(regexp, flags).matcher(x).matches(), error);
  }

  public static Validation<ValidationError, String> maxSize(int maxSize, Function<String, ValidationError> error) {
    return (String toBeValidated) -> isTrueOrError((String x) -> x.length() <= maxSize, error).apply(toBeValidated);
  }

  public static Validation<ValidationError, String> isUuid(Function<String, ValidationError> error) {
    return (String value) -> {
      try {
        var __ = UUID.fromString(value);
        return Either.right(value);
      } catch (IllegalArgumentException bad) {
        return Either.left(error.apply(value));
      }
    };
  }

  public static Validation<ValidationError, String> isUrl(Function<String, ValidationError> error) {
    return (String value) -> {
      try {
        var __ = new URL(value);
        __.toURI();
        return Either.right(value);
      } catch (URISyntaxException | MalformedURLException bad) {
        return Either.left(error.apply(value));
      }
    };
  }

  // SEQUENCE VALIDATIONS

  public static <T> Validation<ValidationError, Seq<T>> distinct(Function<Seq<T>, ValidationError> error) {
    return (Seq<T> values) -> values.length() == values.distinct().length() ? Either.right(values) : Either.left(error.apply(values));
  }
}

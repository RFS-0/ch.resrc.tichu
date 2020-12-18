package ch.resrc.tichu.capabilities.validation;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

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

  public static <T> Validation<ValidationError, T> isTrueOrError(Predicate<T> condition, Supplier<ValidationError> error) {
    return (T toBeValidated) -> {
      if (condition.test(toBeValidated)) {
        return Either.right(toBeValidated);
      } else {
        Validation<ValidationError, T> validation = (T input) -> condition.test(input) ? Either.right(input) : Either.left(error.get());
        return validation.applyTo(toBeValidated);
      }
    };
  }

  public static <T> Validation<ValidationError, T> isFalseOrError(Predicate<T> condition, Supplier<ValidationError> error) {
    return (T toBeValidated) -> {
      if (!condition.test(toBeValidated)) {
        return Either.right(toBeValidated);
      } else {
        Validation<ValidationError, T> validation = (T input) -> !condition.test(input) ? Either.right(input) : Either.left(error.get());
        return validation.applyTo(toBeValidated);
      }
    };
  }

  public static <T> Validation<ValidationError, T> notNull(Supplier<ValidationError> error) {
    return toBeValidated -> Validations.<T>isTrueOrError(Objects::nonNull, error).apply(toBeValidated);
  }

  // INTEGER VALIDATIONS

  public static Validation<ValidationError, Integer> min(int minimum, Supplier<ValidationError> error) {
    return isTrueOrError(x -> x >= minimum, error);
  }

  public static Validation<ValidationError, Seq<Integer>> allMin(int minimum, Supplier<ValidationError> error) {
    return (Seq<Integer> values) -> values.exists(value -> value < minimum) ? Either.left(error.get()) : Either.right(values);
  }

  public static Validation<ValidationError, Integer> max(int maximum, Supplier<ValidationError> error) {
    return isTrueOrError(x -> x <= maximum, error);
  }

  public static Validation<ValidationError, Seq<Integer>> allMax(int maximum, Supplier<ValidationError> error) {
    return (Seq<Integer> values) -> values.exists(value -> value > maximum) ? Either.left(error.get()) : Either.right(values);
  }

  public static Validation<ValidationError, Number> equalTo(Number number, Supplier<ValidationError> error) {
    return isTrueOrError(x -> x.doubleValue() == number.doubleValue(), error);
  }

  public static Validation<ValidationError, Integer> divisibleBy(int divisor, Supplier<ValidationError> error) {
    return isTrueOrError(x -> x % divisor == 0, error);
  }

  public static Validation<ValidationError, Seq<Integer>> allDivisibleBy(int divisor, Supplier<ValidationError> error) {
    return (Seq<Integer> values) -> values.exists(value -> value % divisor != 0) ? Either.left(error.get()) : Either.right(values);
  }

  // STRING VALIDATIONS

  public static Validation<ValidationError, String> notBlank(Supplier<ValidationError> error) {
    return (String value) -> {
      if (StringUtils.isBlank(value)) {
        return Either.left(error.get());
      } else {
        return Either.right(value);
      }
    };
  }

  public static Validation<ValidationError, String> matches(String regexp, Supplier<ValidationError> error) {
    return isTrueOrError(x -> Pattern.compile(regexp).matcher(x).matches(), error);
  }

  public static Validation<ValidationError, String> matches(String regexp, int flags, Supplier<ValidationError> error) {
    return isTrueOrError(x -> Pattern.compile(regexp, flags).matcher(x).matches(), error);
  }

  public static Validation<ValidationError, String> maxSize(int maxSize, Supplier<ValidationError> error) {
    return (String toBeValidated) -> isTrueOrError((String x) -> x.length() <= maxSize, error).apply(toBeValidated);
  }

  public static Validation<ValidationError, String> isUuid(Supplier<ValidationError> error) {
    return (String value) -> {
      try {
        var __ = UUID.fromString(value);
        return Either.right(value);
      } catch (IllegalArgumentException bad) {
        return Either.left(error.get());
      }
    };
  }

  public static Validation<ValidationError, String> isUrl(Supplier<ValidationError> error) {
    return (String value) -> {
      try {
        var __ = new URL(value);
        __.toURI();
        return Either.right(value);
      } catch (URISyntaxException | MalformedURLException bad) {
        return Either.left(error.get());
      }
    };
  }

  // SEQUENCE VALIDATIONS

  public static <T> Validation<ValidationError, Seq<T>> distinct(Supplier<ValidationError> error) {
    return (Seq<T> values) -> values.length() == values.distinct().length() ? Either.right(values) : Either.left(error.get());
  }
}

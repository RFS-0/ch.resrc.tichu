package ch.resrc.tichu.adapters.javax_validation;

import static ch.resrc.tichu.capabilities.validation.ValidationError.Claim.UNINTERESTING_VALUE;
import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.cannotBeUndefinedMsg;
import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.chained;
import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.claimed;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import java.util.Set;
import java.util.function.Function;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorContext;
import javax.validation.constraints.NotNull;

public class TypeConstraints {

  private static final ValidatorContext validatorContext =
    javax.validation.Validation.buildDefaultValidatorFactory()
      .usingContext()
      // We must not do message interpolation, because we use the message template for
      // serialized ValidationError representations.
      .messageInterpolator(new NoMessageInterpolation());


  public static <T> Result<T, ValidationError> validate(T obj) {
    Validator validator = validatorContext.getValidator();

    Set<ConstraintViolation<T>> conversionFailures = validator.validate(obj);

    var validationErrors = conversionFailures.stream()
      .map(asValidationError())
      .map(Result::failure)
      .collect(toList());

    return Result.combined(validationErrors)
      .yield(() -> obj);
  }

  private static <T> Function<ConstraintViolation<T>, ValidationError> asValidationError() {
    return (ConstraintViolation<T> violation) -> {

      if (isViolationOf(IsConvertibleTo.class, violation)) {
        return asConversionError(violation);
      } else if (isViolationOf(NotNull.class, violation)) {
        return asNotNullValidationError(violation);
      } else {
        throw new IllegalArgumentException(
          format("Only NotNull constraint is supported in addition to conversion constraint. " +
            "But was: <%s>", constraintTypeOf(violation).getSimpleName())
        );
      }
    };
  }

  private static ValidationError asConversionError(ConstraintViolation<?> violation) {
    return ValidationError.fromMemento(violation.getMessageTemplate(),
      violation.getInvalidValue())
      .butOrigin(violation.getPropertyPath().toString());
  }

  private static ValidationError asNotNullValidationError(ConstraintViolation<?> violation) {
    var validationError = ValidationError.of(violation.getMessage(), violation.getInvalidValue())
      .butOrigin(violation.getPropertyPath().toString());

    return chained(cannotBeUndefinedMsg(), claimed(UNINTERESTING_VALUE)).apply(validationError);
  }

  private static boolean isViolationOf(Class<?> testedAnnotationType, ConstraintViolation<?> violation) {
    return constraintTypeOf(violation).equals(testedAnnotationType);
  }

  private static Class<?> constraintTypeOf(ConstraintViolation<?> violation) {
    return violation.getConstraintDescriptor().getAnnotation().annotationType();
  }
}

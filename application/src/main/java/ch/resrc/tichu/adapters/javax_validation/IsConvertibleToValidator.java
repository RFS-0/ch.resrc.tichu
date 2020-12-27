package ch.resrc.tichu.adapters.javax_validation;

import ch.resrc.tichu.capabilities.errorhandling.faults.Defect;
import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsConvertibleToValidator implements ConstraintValidator<IsConvertibleTo, Object> {

  private Validation<Object, ValidationError> validation;

  @SuppressWarnings("unchecked")
  @Override
  public void initialize(IsConvertibleTo constraintAnnotation) {
    var domainType = constraintAnnotation.value();

    try {
      Method validation = domainType.getMethod("validation");
      this.validation = (Validation<Object, ValidationError>) validation.invoke(null);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw Defect.of(e);
    }
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    var validationResult = validation.applyTo(value);

    context.disableDefaultConstraintViolation();

    return validationResult.yield(() -> true)
      .forEachError(addConstraintViolationTo(context))
      .recover(false)
      .value();
  }

  private static Consumer<ValidationError> addConstraintViolationTo(ConstraintValidatorContext context) {
    return (ValidationError x) -> context.buildConstraintViolationWithTemplate(x.asMementoWithoutValue())
      .addConstraintViolation();
  }
}

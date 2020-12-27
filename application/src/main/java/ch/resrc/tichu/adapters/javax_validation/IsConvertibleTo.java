package ch.resrc.tichu.adapters.javax_validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;


/**
 * Validates that the value of the annotated field can be converted to a certain type according. Uses the type's validation() method to
 * determine whether the value is convertible.
 * <p>
 * If the constraint passes, the value can be converted into the target type using a conversion method that is assumed to exist on the
 * target type.
 */
@Target({FIELD, ANNOTATION_TYPE, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = IsConvertibleToValidator.class)
@Documented
public @interface IsConvertibleTo {

  String message() default "cannot be converted to target type";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * @return the target type to use for the validation. The type must have a public static validate() method without arguments which
   * returns Validation<T, ValidationError>. T is the type of the validated field.
   */
  Class<?> value();
}

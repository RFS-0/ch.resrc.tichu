package ch.resrc.old.capabilities.validations.old;

import io.vavr.control.*;

import java.util.function.*;

@FunctionalInterface
public interface Validation<E, T> extends Function<T, Either<E, T>> {

  Either<E, T> applyTo(T toBeValidated);

  default Either<E, T> apply(T toBeValidated) {
    return applyTo(toBeValidated);
  }

  default <EE> Validation<EE, T> mapErrors(Function<E, EE> errorMap) {
    return this.asFunction().andThen(validated -> validated.mapLeft(errorMap))::apply;
  }

  default Function<T, Either<E, T>> asFunction() {
    return this::applyTo;
  }
}

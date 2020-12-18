package ch.resrc.tichu.capabilities.validation;

import io.vavr.collection.Seq;
import io.vavr.control.Either;

/**
 * Parses values in a fault tolerant way.
 *
 * @param <T> the type of the value that gets parsed
 * @param <U> the type of the parsing result
 */
public interface InputParser<T, U> {

  /**
   * Parses the given value. Reports all validation errors if the value cannot be parsed.
   *
   * @param toBeParsed the value to be parsed
   *
   * @return a {@code Result} object with the parsing result.
   */
  Either<Seq<ValidationError>, ? extends U> parse(T toBeParsed);
}

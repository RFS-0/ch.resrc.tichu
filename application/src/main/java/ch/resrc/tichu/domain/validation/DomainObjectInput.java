package ch.resrc.tichu.domain.validation;

import static ch.resrc.tichu.capabilities.functional.Mapped.map;
import static ch.resrc.tichu.capabilities.functional.NullSafe.nullSafe;
import static ch.resrc.tichu.capabilities.validation.Validations.isInteger;
import static ch.resrc.tichu.capabilities.validation.Validations.validatedInput;
import static java.lang.String.format;

import ch.resrc.tichu.capabilities.errorhandling.faults.Defect;
import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.capabilities.validation.Input;
import ch.resrc.tichu.capabilities.validation.InputParser;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


/**
 * Provides parsing methods for domain objects that help you with creating domain objects from untrusted primitive input data. The
 * methods help you with validating the data and then creating a suitable domain object from it.
 *
 * <p>Input controllers can run raw primitive request data through these parser methods and specify the reaction
 * in case the data is invalid</p>
 */
public class DomainObjectInput {

  private static final Map<Class<?>, InputParser<String, ?>> parserByType = new HashMap<>();

  static {
    parserByType.put(Id.class, Id::resultOf);
  }

  public static <T> T parse(Class<T> domainType, String input) {
    if (input == null) {
      return null;
    }

    return parsingResult(domainType, Input.of(input)).getOrThrow(InvalidInputDetected::of);
  }

  public static <T> List<T> parseList(Class<T> domainType, List<String> listOfInputs) {
    return nullSafe(map((String x) -> parse(domainType, x)), listOfInputs);
  }

  public static <T> Result<T, ValidationError> parsingResult(Class<T> domainType, Input<String> input) {
    if (!parserByType.containsKey(domainType)) {
      throw Defect.of(format("Cannot parse %s. Unsupported domain type.", domainType.getName()));
    }

    if (!input.isPresent()) {
      return Result.success(null);
    }

    return parserByType.get(domainType)
      .parse(input.value())
      .map(domainType::cast)
      .mapErrors(x -> x.butOrigin(input.origin()));
  }

  public static Result<YearMonth, ValidationError> parseYearMonthResult(Input<String> year, Input<String> month) {
    Result<Integer, ValidationError> theYear = validatedInput(year, isInteger()).map(Integer::valueOf);
    Result<Integer, ValidationError> theMonth = validatedInput(month, isInteger()).map(Integer::valueOf);

    Supplier<YearMonth> createYearMonth = () -> YearMonth.of(theYear.value(), theMonth.value());

    return Result.combined(theYear, theMonth)
      .yield(createYearMonth);
  }
}

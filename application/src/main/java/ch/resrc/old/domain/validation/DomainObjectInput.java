package ch.resrc.old.domain.validation;

import ch.resrc.old.capabilities.errorhandling.faults.*;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import static java.lang.String.*;

public class DomainObjectInput {

  private static final Map<Class<?>, InputParser<? super String, ?>> parseStringToType = HashMap.of(
    Id.class, Id::resultOf,
    Email.class, Email::resultOf,
    Name.class, Name::resultOf,
    Picture.class, Picture::resultOf,
    RoundNumber.class, RoundNumber::resultOf,
    Integer.class, DomainObjectInput::resultOfInt
  );
  private static final Map<Class<?>, InputParser<Map<String, String>, ?>> parserMapToType = HashMap.of(
    CardPoints.class, CardPoints::resultOfRaw
  );

  public static <T> T parse(Class<T> domainType, String input) {
    if (input == null) {
      return null;
    }

    Either<Seq<ValidationError>, T> result = parsingResult(domainType, Input.of(input));
    if (result.isLeft()) {
      throw InvalidInputDetected.of(result.getLeft());
    } else {
      return result.get();
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> Either<Seq<ValidationError>, T> parsingResult(Class<T> domainType, Input<?> input) {
    if (!input.isPresent()) {
      return Either.left(List.of(DomainValidationErrors.errorDetails("must be defined").apply(input)));
    }

    if (parseStringToType.get(domainType).isDefined()) {
      Input<String> stringInput = (Input<String>) input;
      return parseStringToType.get(domainType)
        .get()
        .parse(stringInput.value())
        .map(domainType::cast);
    } else if (parserMapToType.get(domainType).isDefined()) {
      Input<Map<String, String>> mapInput = (Input<Map<String, String>>) input;
      return parserMapToType.get(domainType)
        .get()
        .parse(mapInput.value())
        .map(domainType::cast);
    } else {
      throw Defect.of(format("Cannot parse %s. Unsupported domain type.", domainType.getName()));
    }
  }

  private static Either<Seq<ValidationError>, Integer> resultOfInt(String literal) {
    Try<Integer> tryParseInt = Try.of(() -> Integer.parseInt(literal));
    if (tryParseInt.isFailure()) {
      ValidationError stringNotParsableToInt = ValidationError.of(
        ValidationError.Origin.CLIENT,
        "literal must be parsable to String",
        literal,
        HashSet.of(ValidationError.Claim.CAN_BE_DISPLAYED)
      );
      return Either.left(
        List.of(stringNotParsableToInt));
    } else {
      return Either.right(tryParseInt.get());
    }
  }
}

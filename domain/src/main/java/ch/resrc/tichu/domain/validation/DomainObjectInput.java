package ch.resrc.tichu.domain.validation;

import ch.resrc.tichu.capabilities.error_handling.faults.*;
import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;

import java.util.*;

import static ch.resrc.tichu.capabilities.functional.Mapped.*;
import static ch.resrc.tichu.capabilities.functional.NullSafe.*;
import static java.lang.String.*;

/**
 * Provides parsing methods for domain objects that help you with creating domain objects
 * from untrusted primitive input data. The methods help you with validating the data and
 * then creating a suitable domain object from it.
 *
 * <p>Input controllers can run raw primitive request data through these parser methods and specify the reaction
 * in case the data is invalid</p>
 */
public class DomainObjectInput {

    private static final Map<Class<?>, InputParser<String, ?>> parserByType = new HashMap<>();

    static {
    }

    public static <T> T parse(Class<T> domainType, String input) {

        if (input == null) return null;

        return parsingResult(domainType, Input.of(input)).getOrThrow(InvalidInputDetected::of);
    }

    public static <T> List<T> parseList(Class<T> domainType, List<String> listOfInputs) {

        return nullSafe(listOfInputs, map((String x) -> parse(domainType, x)));
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
}

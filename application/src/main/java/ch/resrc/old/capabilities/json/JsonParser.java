package ch.resrc.old.capabilities.json;

import ch.resrc.old.capabilities.errorhandling.faults.*;
import ch.resrc.old.capabilities.validations.old.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.*;
import io.vavr.control.*;
import org.apache.commons.lang3.exception.*;

import java.util.*;
import java.util.stream.*;

class JsonParser {

  private final ObjectMapper objectMapper;

  public JsonParser(ObjectMapper objectMapper) {

    this.objectMapper = objectMapper;
  }

  public <T> Either<ValidationError, T> parsingResult(Input<String> input, Class<T> targetType) {

    try {
      return Either.right(objectMapper.readValue(input.value(), targetType));
    } catch (MismatchedInputException bad) {
      String propertyPath = extractPropertyPathFrom(bad);
      return Either.left(input.invalidate("Illegal JSON value").butOrigin(propertyPath));

    } catch (InvalidDefinitionException bad) {
      throw OurFault.of(bad);
    } catch (JsonMappingException bad) {
      String propertyPath = extractPropertyPathFrom(bad);

      return Either.left(input.invalidate("Invalid JSON").butOrigin(propertyPath));
    } catch (JsonParseException bad) {
      return Either.left(input.invalidate("Invalid JSON"));
    } catch (Exception bad) {
      throw Defect.of(bad);
    }
  }

  private <X extends Exception> Optional<X> findInCauseChainOf(Exception bad, Class<X> causeType) {
    return ExceptionUtils.getThrowableList(bad).stream()
      .filter(cause -> causeType.isAssignableFrom(cause.getClass()))
      .map(causeType::cast)
      .findFirst();
  }

  private String extractPropertyPathFrom(JsonMappingException bad) {
    return bad.getPath().stream()
      .map(JsonMappingException.Reference::getFieldName)
      .collect(Collectors.joining("."));
  }
}

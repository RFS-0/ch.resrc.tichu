package ch.resrc.old.capabilities.json;

import ch.resrc.old.capabilities.errorhandling.faults.*;
import ch.resrc.old.capabilities.validations.old.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import io.vavr.control.*;
import io.vavr.jackson.datatype.*;

public class JsonCapability {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    new DomainJsonCapability().addTo(objectMapper);
  }

  private static final JsonParser JSON_PARSER = new JsonParser(objectMapper);

  private static ObjectMapper objectMapper() {
    return objectMapper;
  }

  public static Json json() {
    return new Json() {

      @Override
      public <T> Either<ValidationError, T> parsingResult(Input<String> json, Class<T> type) {
        return JSON_PARSER.parsingResult(json, type);
      }

      @Override
      public String toJsonString(Object object) {
        try {
          return objectMapper()
            .registerModule(new VavrModule())
            .writeValueAsString(object);
        } catch (JsonProcessingException bad) {
          throw OurFault.of(bad);
        }
      }
    };
  }
}

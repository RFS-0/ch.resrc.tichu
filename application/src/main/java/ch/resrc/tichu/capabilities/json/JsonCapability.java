package ch.resrc.tichu.capabilities.json;

import ch.resrc.tichu.capabilities.errorhandling.faults.OurFault;
import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.capabilities.validation.Input;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
      public <T> Result<T, ValidationError> parsingResult(Input<String> json, Class<T> type) {
        return JSON_PARSER.parsingResult(json, type);
      }

      @Override
      public String toJsonString(Object object) {
        try {
          return objectMapper().writeValueAsString(object);
        } catch (JsonProcessingException bad) {
          throw OurFault.of(bad);
        }
      }
    };
  }
}

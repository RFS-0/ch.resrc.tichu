package ch.resrc.tichu.capabilities.json;

import ch.resrc.tichu.capabilities.validation.Input;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.domain.validation.DomainObjectInput;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class DomainObjectDeserializer<T> extends StdDeserializer<T> {

  DomainObjectDeserializer(Class<T> domainType) {
    super(domainType);
  }

  @Override
  @SuppressWarnings("unchecked")
  public T deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
    String value = parser.getText();
    Class<T> domainType = (Class<T>) handledType();

    return DomainObjectInput.parsingResult(domainType, Input.of(value))
      .getOrElseThrow(InvalidInputDetected::of);
  }
}

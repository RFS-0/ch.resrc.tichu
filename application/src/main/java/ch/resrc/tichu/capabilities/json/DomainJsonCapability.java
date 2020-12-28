package ch.resrc.tichu.capabilities.json;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Locale;

public class DomainJsonCapability {

  public Module domainJsonModule() {
    SimpleModule theModule = new SimpleModule();
    return theModule;
  }

  public ObjectMapper addTo(ObjectMapper objectMapper) {
    return objectMapper.registerModule(new JavaTimeModule())
      .registerModule(domainJsonModule())
      .setLocale(Locale.ENGLISH)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .enable(SerializationFeature.INDENT_OUTPUT);
  }

}

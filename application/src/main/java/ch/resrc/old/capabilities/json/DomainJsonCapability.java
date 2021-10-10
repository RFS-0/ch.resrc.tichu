package ch.resrc.old.capabilities.json;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.*;
import com.fasterxml.jackson.datatype.jsr310.*;

import java.util.*;

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

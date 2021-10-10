package ch.resrc.old.capabilities.endpoints;

import ch.resrc.old.capabilities.errorhandling.faults.*;
import ch.resrc.old.capabilities.json.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.google.gson.*;

public interface JsonHabits {

  JsonHabits INSTANCE = new JsonHabits() {
  };

  default Json json() {
    return JsonCapability.json();
  }

  default String asJson(Object obj) {

    // Cannot use Gson here, because Gson is not able to serialize anonymous inner classes reliably.
    ObjectMapper mapper = new ObjectMapper()
      .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException bad) {
      throw Defect.of(bad);
    }
  }

  default <T> T fromJson(String json, Class<T> type) {
    return new Gson().fromJson(json, type);
  }

  default <T> JsonBody<T> asRequestBody(T obj) {
    return JsonBody.requestBodyOf(asJson(obj), json());
  }
}

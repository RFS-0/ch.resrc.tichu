package ch.resrc.tichu.testcapabilities.endpoints;

import ch.resrc.tichu.capabilities.errorhandling.faults.Defect;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.json.JsonBody;
import ch.resrc.tichu.capabilities.json.JsonCapability;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

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

package ch.resrc.tichu.endpoints.errorhandling;

import static ch.resrc.tichu.capabilities.functional.PersistentCollections.addedTo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ErrorDto {

  @JsonProperty
  public List<Error> errors = List.of();

  public static class Error {

    public String type;
    public String title;
    public String details;
    public String occurredOn;
    public String instance;


    public String getType() {
      return type;
    }

    public Error setType(String type) {
      this.type = type;
      return this;
    }

    public String getDetails() {
      return details;
    }

    public Error setDetails(String details) {
      this.details = details;
      return this;
    }

    public String getInstance() {
      return instance;
    }

    public Error setInstance(String instance) {
      this.instance = instance;
      return this;
    }

    public String getOccurredOn() {
      return occurredOn;
    }

    public Error setOccurredOn(String occurredOn) {
      this.occurredOn = occurredOn;
      return this;
    }

    public String getTitle() {
      return title;
    }

    public Error setTitle(String title) {
      this.title = title;
      return this;
    }
  }

  public Error firstError() {
    return this.errors.get(0);
  }

  public ErrorDto addError(Error error) {
    this.errors = addedTo(errors, error);
    return this;
  }
}

package ch.resrc.old.adapters.endpoints_rest.users.dto;

import ch.resrc.old.use_cases.find_or_create_user.ports.output.*;
import com.fasterxml.jackson.annotation.*;

import java.util.function.*;

import static ch.resrc.old.capabilities.functional.NullSafe.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

  public String id;
  public String email;
  public String name;
  public String createdAt;

  public UserDto() {
    // to deserialize
  }

  public UserDto(Consumer<UserDto> initializer) {
    initializer.accept(this);
  }

  public static UserDto fromDocument(UserDocument the) {
    return new UserDto(x -> {
      x.id = nullSafeToString(the.id().value());
      x.email = nullSafeToString(the.email().value());
      x.name = nullSafeToString(the.name().value());
      x.createdAt = nullSafeToString(the.createdAt().toString());
    });
  }
}

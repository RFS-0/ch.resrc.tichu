package ch.resrc.tichu.adapters.endpoints_rest.users.dto;

import ch.resrc.tichu.use_cases.find_or_create_user.ports.output.UserDocument;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.function.Consumer;

import static ch.resrc.tichu.capabilities.functional.NullSafe.nullSafeToString;

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

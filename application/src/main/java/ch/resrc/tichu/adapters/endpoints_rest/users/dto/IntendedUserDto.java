package ch.resrc.tichu.adapters.endpoints_rest.users.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IntendedUserDto {

  public String name;
  public String email;

  private IntendedUserDto() {
    // to deserialize
  }

  public IntendedUserDto(String name, String email) {
    this.name = name;
    this.email = email;
  }
}

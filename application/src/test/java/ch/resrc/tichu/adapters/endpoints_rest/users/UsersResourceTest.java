package ch.resrc.tichu.adapters.endpoints_rest.users;

import ch.resrc.tichu.adapters.endpoints_rest.RestPaths;
import ch.resrc.tichu.adapters.endpoints_rest.users.dto.IntendedUserDto;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@QuarkusTest
@TestHTTPEndpoint(UsersResource.class)
class UsersResourceTest {

  @Test
  public void findOrCreate_validIntendedUserDto_userDto() {
    // given
    var expectedName = "someName";
    var expectedEmail = "someOne@domain.ch";
    var intendedUserDto = new IntendedUserDto(expectedName, expectedEmail);

    // when
    io.restassured.response.Response response = given()
      .contentType(MediaType.APPLICATION_JSON)
      .request().body(intendedUserDto).accept(MediaType.APPLICATION_JSON)
      .when()
      .log().all()
      .post(RestPaths.Users.FIND_OR_CREATE)
      .then()
      .log().all()
      .statusCode(Response.Status.CREATED.getStatusCode())
      .extract()
      .response();

    // then
    var id = response.getBody().jsonPath().getString("id");
    var name = response.getBody().jsonPath().getString("name");
    var email = response.getBody().jsonPath().getString("email");
    var createdAt = response.getBody().jsonPath().getString("createdAt");

    assertDoesNotThrow(() -> UUID.fromString(id));
    assertThat(email).isEqualTo(expectedEmail);
    assertThat(name).isEqualTo(expectedName);
    assertDoesNotThrow(() -> Instant.parse(createdAt));
  }
}

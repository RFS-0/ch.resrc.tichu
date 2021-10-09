package ch.resrc.tichu.adapters.endpoints_rest.users;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestHTTPEndpoint(UsersResource.class)
class UsersResourceTest {

}

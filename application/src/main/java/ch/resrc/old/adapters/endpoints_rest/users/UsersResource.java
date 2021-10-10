package ch.resrc.old.adapters.endpoints_rest.users;

import ch.resrc.old.adapters.endpoints_rest.*;
import ch.resrc.old.adapters.endpoints_rest.RestPaths.*;
import ch.resrc.old.adapters.endpoints_rest.users.dto.*;
import ch.resrc.old.adapters.endpoints_rest.users.input.*;
import ch.resrc.old.adapters.endpoints_rest.users.output.*;
import ch.resrc.old.use_cases.find_or_create_user.ports.input.*;
import ch.resrc.old.use_cases.find_or_create_user.ports.output.*;

import javax.enterprise.context.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path(RestPaths.Users.USERS)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class UsersResource {

  private final FindOrCreateUserInput findOrCreateUser;

  public UsersResource(FindOrCreateUserInput findOrCreateUser) {
    this.findOrCreateUser = findOrCreateUser;
  }

  @POST
  @Path(Users.FIND_OR_CREATE)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public javax.ws.rs.core.Response findOrCreate(IntendedUserDto theIntended) {
    FindOrCreateUserRestInput input = new FindOrCreateUserRestInput(theIntended);
    FindOrCreateUserOutput.Response useCaseResponse = findOrCreateUser.apply(input.request());
    FindOrCreateUserRestOutput output = new FindOrCreateUserRestOutput(useCaseResponse);
    return output.response();
  }
}

package ch.resrc.tichu.adapters.endpoints_rest.users;

import ch.resrc.tichu.adapters.endpoints_rest.RestPaths.Users;
import ch.resrc.tichu.adapters.endpoints_rest.users.dto.IntendedUserDto;
import ch.resrc.tichu.adapters.endpoints_rest.users.input.FindOrCreateUserRestInput;
import ch.resrc.tichu.adapters.endpoints_rest.users.output.FindOrCreateUserRestOutput;
import ch.resrc.tichu.use_cases.find_or_create_user.ports.input.FindOrCreateUserInput;
import ch.resrc.tichu.use_cases.find_or_create_user.ports.output.FindOrCreateUserOutput;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(Users.USERS)
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

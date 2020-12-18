package ch.resrc.tichu;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api")
public class SampleResource {

  @GET()
  @Path("hello")
  @Produces(MediaType.TEXT_PLAIN)
  public String hello() {
    return "hello";
  }


  @GET
  @Path("pwd")
  @Produces(MediaType.APPLICATION_JSON)
  public String test() {
    return System.getProperty("user.dir");
  }
}

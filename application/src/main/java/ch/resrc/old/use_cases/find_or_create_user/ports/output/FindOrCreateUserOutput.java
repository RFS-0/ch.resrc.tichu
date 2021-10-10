package ch.resrc.old.use_cases.find_or_create_user.ports.output;

import ch.resrc.old.use_cases.*;

public interface FindOrCreateUserOutput extends UseCaseOutput {

  UserDocument get();

  class Response implements UseCaseOutput {

    private final UserDocument toBePresented;

    public Response(UserDocument toBePresented) {
      this.toBePresented = toBePresented;
    }

    public UserDocument toBePresented() {
      return toBePresented;
    }
  }
}

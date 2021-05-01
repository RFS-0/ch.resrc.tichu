package ch.resrc.tichu.use_cases.teams.update_team_name.ports.output;

import ch.resrc.tichu.use_cases.UseCaseOutput;
import ch.resrc.tichu.use_cases.teams.ports.output.TeamDocument;

public interface UpdateTeamNameOutput extends UseCaseOutput {

  TeamDocument get();

  class Response implements UseCaseOutput {

    private final TeamDocument toBePresented;

    public Response(TeamDocument toBePresented) {
      this.toBePresented = toBePresented;
    }

    public TeamDocument toBePresented() {
      return toBePresented;
    }
  }
}

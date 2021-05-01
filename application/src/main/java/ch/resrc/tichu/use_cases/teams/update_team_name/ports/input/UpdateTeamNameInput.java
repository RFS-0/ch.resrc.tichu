package ch.resrc.tichu.use_cases.teams.update_team_name.ports.input;

import ch.resrc.tichu.use_cases.UseCaseInput;
import ch.resrc.tichu.use_cases.ports.input_boundary.InputBoundary;
import ch.resrc.tichu.use_cases.teams.update_team_name.ports.output.UpdateTeamNameOutput;

@FunctionalInterface
public interface UpdateTeamNameInput extends InputBoundary {

  UpdateTeamNameOutput.Response apply(Request requested);

  class Request implements UseCaseInput {

    private final IntendedTeamName intent;

    public Request(IntendedTeamName intent) {
      this.intent = intent;
    }

    public IntendedTeamName intent() {
      return intent;
    }
  }
}

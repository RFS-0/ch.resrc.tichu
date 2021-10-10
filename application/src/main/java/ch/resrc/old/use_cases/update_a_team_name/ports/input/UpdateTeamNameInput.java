package ch.resrc.old.use_cases.update_a_team_name.ports.input;

import ch.resrc.old.domain.*;
import ch.resrc.old.use_cases.*;
import ch.resrc.old.use_cases.update_a_team_name.ports.output.*;

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

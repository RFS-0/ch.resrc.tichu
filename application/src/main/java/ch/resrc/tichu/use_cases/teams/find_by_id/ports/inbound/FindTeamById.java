package ch.resrc.tichu.use_cases.teams.find_by_id.ports.inbound;

import ch.resrc.tichu.use_cases.ports.documents.IntendedId;
import ch.resrc.tichu.use_cases.ports.input_boundary.InputBoundary;
import ch.resrc.tichu.use_cases.ports.output_boundary.TeamPresenter;

@FunctionalInterface
public interface FindTeamById extends InputBoundary {

  void invoke(Request requested, TeamPresenter out);

  class Request {

    private final IntendedId intent;

    public Request(IntendedId intent) {
      this.intent = intent;
    }

    public IntendedId intent() {
      return intent;
    }
  }
}

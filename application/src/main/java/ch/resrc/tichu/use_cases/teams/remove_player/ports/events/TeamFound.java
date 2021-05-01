package ch.resrc.tichu.use_cases.teams.remove_player.ports.events;

import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.use_cases.teams.ports.output.TeamDocument;
import ch.resrc.tichu.use_cases.teams.remove_player.ports.documents.IntendedPlayerRemoval;

import static ch.resrc.tichu.use_cases.teams.ports.output.TeamDocument.aTeamDocument;

public class TeamFound extends Event {

  private final IntendedPlayerRemoval intent;
  private final Team team;

  public TeamFound(IntendedPlayerRemoval intent, Team team) {
    this.intent = intent;
    this.team = team;
  }

  public static TeamFound of(IntendedPlayerRemoval intent, Team team) {
    return new TeamFound(intent, team);
  }

  public IntendedPlayerRemoval intent() {
    return intent;
  }

  public Team team() {
    return team;
  }

  public TeamDocument asDocument() {
    return aTeamDocument()
      .withId(team.id())
      .withName(team.name())
      .build();
  }
}

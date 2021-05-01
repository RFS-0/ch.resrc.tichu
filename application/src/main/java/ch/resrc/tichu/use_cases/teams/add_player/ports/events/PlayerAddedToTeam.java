package ch.resrc.tichu.use_cases.teams.add_player.ports.events;

import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.use_cases.ports.documents.PlayerDocument;
import ch.resrc.tichu.use_cases.teams.add_player.ports.documents.PlayerAddedToTeamDocument;

import static ch.resrc.tichu.use_cases.ports.documents.PlayerDocument.aPlayerDocument;
import static ch.resrc.tichu.use_cases.teams.add_player.ports.documents.PlayerAddedToTeamDocument.aPlayerAddedToTeamDocument;
import static ch.resrc.tichu.use_cases.teams.ports.output.TeamDocument.aTeamDocument;

public class PlayerAddedToTeam extends Event {

  private final Player player;
  private final Team team;

  private PlayerAddedToTeam(Player player, Team team) {
    this.player = player;
    this.team = team;
  }

  public static PlayerAddedToTeam of(Player player, Team team) {
    return new PlayerAddedToTeam(player, team);
  }

  public Player player() {
    return player;
  }

  public Team team() {
    return team;
  }

  public PlayerAddedToTeamDocument asDocument() {
    return aPlayerAddedToTeamDocument()
      .withPlayer(aPlayerDocument()
        .withId(player.id())
        .withName(player.name())
        .withCreatedAt(player.createdAt())
        .build())
      .withTeam(aTeamDocument()
        .withId(team.id())
        .withName(team.name())
        .withFirstPlayer(PlayerDocument.fromPlayer(team.firstPlayer()))
        .withSecondPlayer(PlayerDocument.fromPlayer(team.secondPlayer()))
        .build())
      .build();
  }

}

package ch.resrc.tichu.use_cases.teams.remove_player.remove_first_player_from_team;

import ch.resrc.tichu.capabilities.errorhandling.DomainProblem;
import ch.resrc.tichu.capabilities.errorhandling.DomainProblemDetected;
import ch.resrc.tichu.capabilities.events.ErrorOccurred;
import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.capabilities.events.EventForwarding;
import ch.resrc.tichu.capabilities.events.Eventful;
import ch.resrc.tichu.capabilities.events.Notifiable;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.use_cases.UseCaseEnactment;
import ch.resrc.tichu.use_cases.UseCaseOutput;
import ch.resrc.tichu.use_cases.ports.documents.TeamDocument;
import ch.resrc.tichu.use_cases.ports.output_boundary.TeamPresenter;
import ch.resrc.tichu.use_cases.teams.remove_player.ports.documents.IntendedPlayerRemoval;
import ch.resrc.tichu.use_cases.teams.remove_player.ports.events.RemovePlayerFromTeam;
import ch.resrc.tichu.use_cases.teams.remove_player.ports.events.TeamFound;
import ch.resrc.tichu.use_cases.teams.remove_player.ports.inbound.RemovePlayerFromTeamRequest;
import ch.resrc.tichu.use_cases.teams.remove_player.remove_first_player_from_team.ports.inbound.RemoveFirstPlayerFromTeam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ch.resrc.tichu.capabilities.functional.VoidMatch.Case;
import static ch.resrc.tichu.capabilities.functional.VoidMatch.DefaultIgnore;
import static io.vavr.API.$;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

public class RemoveFirstPlayerFromTeamUseCase implements RemoveFirstPlayerFromTeam {

  private static final Logger LOG = LoggerFactory.getLogger(RemoveFirstPlayerFromTeamUseCase.class);

  private final OutboundPorts ports;

  private RemoveFirstPlayerFromTeamUseCase(OutboundPorts ports) {
    this.ports = ports;
  }

  ///// Outbound Ports /////

  public static class OutboundPorts {


    public OutboundPorts() {

    }
  }

  ///// Roles /////

  static class Workflow extends Eventful implements EventForwarding {

    final TeamsSource teamsSource;

    Workflow(TeamsSource teamsSource) {
      this.teamsSource = teamsSource;
      this.events().subscribe(this.teamsSource);
      teamsSource.events().subscribe(this);
    }

    void process(IntendedPlayerRemoval intent) {
      LOG.info("[USE-CASE] [REMOVE-FIRST-PLAYER] [INPUT]: Processing input: " + intent);
      this.publish(RemovePlayerFromTeam.of(intent));
    }

    @Override
    public Object on(Object anEvent) {
      LOG.info("[USE-CASE] [REMOVE-FIRST-PLAYER] [EVENTS]: Processing event of type '" + anEvent.getClass() + "'");
      Match(anEvent).of(
        DefaultIgnore()
      );
      return anEvent;
    }
  }

  static class TeamsSource extends Eventful implements EventForwarding {


    public TeamsSource() {
    }

    @Override
    public Object on(Object anEvent) {
      Match(anEvent).of(
        Case($(instanceOf(RemovePlayerFromTeam.class)), remove -> publish(searchTeam(remove.intent()))),
        Case($(instanceOf(TeamFound.class)), found -> publish(removeFirstPlayer(found.team()))),
        DefaultIgnore()
      );
      return anEvent;
    }

    Event searchTeam(IntendedPlayerRemoval intent) {
      return ErrorOccurred.of(DomainProblemDetected.of(DomainProblem.INVARIANT_VIOLATED));
    }

    Event removeFirstPlayer(Team team) {
      return null;
    }
  }

  static class UserInterface implements Notifiable {

    final Id receiver;
    final TeamPresenter presenter;

    UserInterface(Id receiver, TeamPresenter presenter) {
      this.receiver = receiver;
      this.presenter = presenter;
    }

    @Override
    public Object on(Object anEvent) {
      Match(anEvent).of(
        DefaultIgnore()
      );
      return anEvent;
    }

    private void present(TeamDocument theDocument) {
      LOG.info("[USE-CASE] [REMOVED-FIRST-PLAYER] [OUTPUT]: Presenting output: " + theDocument);
      presenter.present(receiver, theDocument);
    }
  }

  ///// Enactment /////

  public static RemoveFirstPlayerFromTeam create(OutboundPorts ports) {
    return new RemoveFirstPlayerFromTeamUseCase(ports);
  }

  @Override
  public void invoke(RemovePlayerFromTeamRequest requested, TeamPresenter out) {

    new UseCaseEnactment() {

      final Workflow workflow;

      public UseCaseOutput apply() {
        workflow.process(requested.intent());
        return null;
      }

      ///// Role Assignment /////
      {
        TeamsSource teamsSource = new TeamsSource();

        workflow = new Workflow(teamsSource);

        UserInterface ui = new UserInterface(requested.intent().teamId(), out);

        workflow.events().subscribe(ui);
      }
    }.apply();
  }
}

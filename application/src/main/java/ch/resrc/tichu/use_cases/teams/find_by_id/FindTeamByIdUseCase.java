package ch.resrc.tichu.use_cases.teams.find_by_id;

import ch.resrc.tichu.capabilities.events.EventForwarding;
import ch.resrc.tichu.capabilities.events.Eventful;
import ch.resrc.tichu.capabilities.events.Notifiable;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.use_cases.UseCaseEnactment;
import ch.resrc.tichu.use_cases.UseCaseOutput;
import ch.resrc.tichu.use_cases.ports.documents.IntendedId;
import ch.resrc.tichu.use_cases.ports.output_boundary.TeamPresenter;
import ch.resrc.tichu.use_cases.teams.find_by_id.ports.inbound.FindTeamById;

public class FindTeamByIdUseCase implements FindTeamById {


  private final OutboundPorts ports;

  private FindTeamByIdUseCase(OutboundPorts ports) {
    this.ports = ports;
  }

  public static FindTeamById create(OutboundPorts ports) {
    return new FindTeamByIdUseCase(ports);
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
      teamsSource.events().subscribe(this);
    }

    void findTeamById(IntendedId theSearchedTeamId) {

    }
  }

  static class TeamsSource extends Eventful implements EventForwarding {

    public TeamsSource() {

    }
  }

  static class FoundTeam {

    final Team self;

    void publishSearchResultTo(Notifiable receiver) {
      receiver.on(null);
    }

    public FoundTeam(Team self) {
      this.self = self;
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
      return anEvent;
    }
  }

  ///// Enactment /////

  @Override
  public void invoke(Request requested, TeamPresenter out) {
    new UseCaseEnactment() {

      final Workflow workflow;

      public UseCaseOutput apply() {
        workflow.findTeamById(requested.intent());
        return null;
      }

      ///// Role Assignment /////
      {
        TeamsSource teamsSource = new TeamsSource();

        workflow = new Workflow(teamsSource);

        UserInterface ui = new UserInterface(requested.intent().id(), out);

        workflow.events().subscribe(ui);
      }
    }.apply();
  }
}

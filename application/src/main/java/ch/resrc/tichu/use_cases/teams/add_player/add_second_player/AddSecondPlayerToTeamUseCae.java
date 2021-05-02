package ch.resrc.tichu.use_cases.teams.add_player.add_second_player;

import ch.resrc.tichu.capabilities.events.ErrorOccurred;
import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.capabilities.events.EventForwarding;
import ch.resrc.tichu.capabilities.events.Eventful;
import ch.resrc.tichu.capabilities.events.Notifiable;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.use_cases.UseCaseEnactment;
import ch.resrc.tichu.use_cases.UseCaseOutput;
import ch.resrc.tichu.use_cases.common_ports.input.IntendedPlayerAddition;
import ch.resrc.tichu.use_cases.ports.output_boundary.PlayerAddedToTeamPresenter;
import ch.resrc.tichu.use_cases.teams.add_player.add_second_player.ports.inbound.AddSecondPlayerToTeam;
import ch.resrc.tichu.use_cases.teams.add_player.ports.documents.PlayerAddedToTeamDocument;
import ch.resrc.tichu.use_cases.teams.add_player.ports.events.CreateAnonymousPlayer;
import ch.resrc.tichu.use_cases.teams.add_player.ports.events.PlayerAddedToTeam;
import ch.resrc.tichu.use_cases.teams.add_player.ports.events.PlayerCreated;
import ch.resrc.tichu.use_cases.teams.add_player.ports.events.PlayerFound;
import ch.resrc.tichu.use_cases.teams.add_player.ports.events.PlayerNotFound;
import ch.resrc.tichu.use_cases.teams.add_player.ports.events.SearchPlayer;
import ch.resrc.tichu.use_cases.teams.add_player.ports.events.TeamFound;
import ch.resrc.tichu.use_cases.teams.add_player.ports.events.TeamNotFound;
import ch.resrc.tichu.use_cases.teams.add_player.ports.inbound.AddPlayerToTeamRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ch.resrc.tichu.capabilities.errorhandling.Blame.isClientFault;
import static ch.resrc.tichu.capabilities.functional.VoidMatch.Case;
import static ch.resrc.tichu.capabilities.functional.VoidMatch.DefaultIgnore;
import static io.vavr.API.$;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

public class AddSecondPlayerToTeamUseCae implements AddSecondPlayerToTeam {

  private static final Logger LOG = LoggerFactory.getLogger(AddSecondPlayerToTeamUseCae.class);

  private final OutboundPorts ports;

  private AddSecondPlayerToTeamUseCae(OutboundPorts ports) {
    this.ports = ports;
  }

  ///// Outbound Ports /////

  public static class OutboundPorts {


    public OutboundPorts() {
    }
  }

  ///// Roles /////

  static class Workflow extends Eventful implements EventForwarding {

    final PlayerSource playerSource;
    final TeamsSource teamsSource;

    Workflow(
      PlayerSource playerSource,
      TeamsSource teamsSource
    ) {
      this.playerSource = playerSource;
      this.teamsSource = teamsSource;
      this.events().subscribe(playerSource);
      this.events().subscribe(teamsSource);
      this.playerSource.events().subscribe(this);
      this.teamsSource.events().subscribe(this);
    }

    void process(IntendedPlayerAddition theIntendedPlayerAddition) {
      LOG.info("[USE-CASE] [ADD-SECOND-PLAYER] [INPUT]: Processing input: " + theIntendedPlayerAddition);
      if (theIntendedPlayerAddition.userId() != null) {
        this.publish(SearchPlayer.of(theIntendedPlayerAddition));
      } else if (theIntendedPlayerAddition.playerName() != null) {
        this.publish(CreateAnonymousPlayer.of(theIntendedPlayerAddition));
      }
    }

    @Override
    public Object on(Object anEvent) {
      LOG.info("[USE-CASE] [ADD-SECOND-PLAYER] [EVENTS]: Processing event of type '" + anEvent.getClass() + "'");
      Match(anEvent).of(
        Case($(instanceOf(PlayerFound.class)), this::publish),
        Case($(instanceOf(PlayerNotFound.class)), this::publish),
        Case($(instanceOf(PlayerCreated.class)), this::publish),
        Case($(instanceOf(TeamFound.class)), this::publish),
        Case($(instanceOf(TeamNotFound.class)), this::publish),
        Case($(instanceOf(PlayerAddedToTeam.class)), this::publish),
        Case($(instanceOf(ErrorOccurred.class)), this::publish),
        DefaultIgnore()
      );
      return anEvent;
    }
  }

  static class PlayerSource extends Eventful implements EventForwarding {

    public PlayerSource() {

    }

    @Override
    public Object on(Object anEvent) {
      Match(anEvent).of(
        Case($(instanceOf(SearchPlayer.class)), search -> publish(searchPlayer(search.intent()))),
        Case($(instanceOf(PlayerNotFound.class)), notFound -> publish(createPlayerForUser(notFound.intent()))),
        Case($(instanceOf(CreateAnonymousPlayer.class)), anonymousPlayer -> publish(createAnonymousPlayer(anonymousPlayer.intent()))),
        DefaultIgnore()
      );
      return anEvent;
    }

    Event searchPlayer(IntendedPlayerAddition intendedPlayerAddition) {
      return null;
    }

    Event createPlayerForUser(IntendedPlayerAddition intent) {
      return createPlayer(intent.userId(), intent);
    }

    Event createAnonymousPlayer(IntendedPlayerAddition intent) {
      return createPlayer(Id.next(), intent);
    }

    private Event createPlayer(Id id, IntendedPlayerAddition intent) {
      return null;
    }
  }

  static class TeamsSource extends Eventful implements EventForwarding {

    public TeamsSource() {
    }

    @Override
    public Object on(Object anEvent) {
      Match(anEvent).of(
        Case($(instanceOf(PlayerFound.class)), found -> publish(searchTeam(found.player(), found.intent()))),
        Case($(instanceOf(PlayerCreated.class)), created -> publish(searchTeam(created.player(), created.intent()))),
        Case($(instanceOf(TeamNotFound.class)), notFound -> publish(createTeam(notFound.player(), notFound.intent()))),
        Case($(instanceOf(TeamFound.class)), found -> publish(addSecondPlayerToTeam(found.player(), found.team()))),
        DefaultIgnore()
      );
      return anEvent;
    }

    Event searchTeam(Player player, IntendedPlayerAddition intendedPlayerAddition) {
      return TeamNotFound.of(intendedPlayerAddition, player);
    }

    Event createTeam(Player player, IntendedPlayerAddition intent) {
      return null;
    }

    private Event addSecondPlayerToTeam(Player thePlayer, Team theTeam) {
      return null;
    }
  }

  static class UserInterface implements Notifiable {

    final Id receiver;
    final PlayerAddedToTeamPresenter presenter;

    UserInterface(Id receiver, PlayerAddedToTeamPresenter presenter) {
      this.receiver = receiver;
      this.presenter = presenter;
    }

    @Override
    public Object on(Object anEvent) {
      Match(anEvent).of(
        Case($(instanceOf(PlayerAddedToTeam.class)), the -> this.presentPlayerAddedToTeam(the.asDocument())),
        Case($(isClientFault()), presenter::presentBusinessError),
        DefaultIgnore()
      );
      return anEvent;
    }

    private void presentPlayerAddedToTeam(PlayerAddedToTeamDocument theDocument) {
      LOG.info("[USE-CASE] [ADD-SECOND-PLAYER] [OUTPUT]: Presenting output: " + theDocument);
    }
  }

  ///// Enactment /////

  public static AddSecondPlayerToTeam create(OutboundPorts ports) {
    return new AddSecondPlayerToTeamUseCae(ports);
  }

  @Override
  public void invoke(AddPlayerToTeamRequest requested, PlayerAddedToTeamPresenter out) {

    new UseCaseEnactment() {

      final Workflow workflow;

      public UseCaseOutput apply() {
        workflow.process(requested.intent());
        return null;
      }

      ///// Role Assignment /////
      {
        PlayerSource playerSource = new PlayerSource();

        TeamsSource teamsSource = new TeamsSource();

        workflow = new Workflow(playerSource, teamsSource);

        UserInterface ui = new UserInterface(requested.intent().teamId(), out);

        workflow.events().subscribe(ui);
      }
    }.apply();
  }
}

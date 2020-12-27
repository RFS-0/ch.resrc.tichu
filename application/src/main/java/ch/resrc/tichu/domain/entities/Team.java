package ch.resrc.tichu.domain.entities;

import static ch.resrc.tichu.capabilities.functional.Reduce.folded;
import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.cannotBeUndefinedMsg;
import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.context;
import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.mustBeSpecifiedMsg;
import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;
import static ch.resrc.tichu.domain.validation.DomainValidations.mandatoryPropertyMissing;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

import ch.resrc.tichu.capabilities.changelog.ChangeLog;
import ch.resrc.tichu.capabilities.changelog.ChangeLogging;
import ch.resrc.tichu.capabilities.errorhandling.DomainError;
import ch.resrc.tichu.capabilities.errorhandling.DomainProblem;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis;
import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Team implements ChangeLogging {

  private Id id;
  private Set<Id> players;
  private Instant createdAt;

  private ChangeLog changeLog = ChangeLog.empty();

  ///// Characteristics /////

  @Override
  public ChangeLog changeLog() {
    return changeLog;
  }

  public Id id() {
    return id;
  }

  public Set<Id> players() {
    return players;
  }

  public Instant createdAt() {
    return createdAt;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("id", id)
      .append("players", players)
      .append("createdAt", createdAt)
      .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Team team = (Team) o;

    if (!id.equals(team.id)) {
      return false;
    }
    if (!players.equals(team.players)) {
      return false;
    }
    return createdAt.equals(team.createdAt);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + players.hashCode();
    result = 31 * result + createdAt.hashCode();
    return result;
  }

  private Result<Team, ProblemDiagnosis> validated() {
    return mandatoryProperties().mapErrors(mandatoryPropertyMissing()).applyTo(this);
  }

  private Team copied(Consumer<Team> modification) {
    final var theCopy = new Team(this);
    modification.accept(theCopy);
    return theCopy;
  }

  ///// Constraints /////

  private static Validation<Team, ValidationError> mandatoryProperties() {
    return allOf(
      attribute(x -> x.id, notNull(mustBeSpecifiedMsg(), context(Id.class))),
      attribute(x -> x.players, notNull(mustBeSpecifiedMsg(), context(Id.class))),
      attribute(x -> x.createdAt, notNull(cannotBeUndefinedMsg(), context(Instant.class)))
    );
  }

  ///// Construction /////

  private Team() {
  }

  private Team(Team other) {
    this.id = other.id;
    this.players = other.players;
    this.createdAt = other.createdAt;
    this.changeLog = other.changeLog;
  }

  public static Result<Team, CreationError> fromChangeLog(ChangeLog changeLog) {
    Team newTeam = folded(new Team(), changeLog.allEvents(), Team::mutated);
    newTeam.changeLog = changeLog;
    return newTeam.validated().mapErrors(CreationError::new);
  }


  public static Result<Team, CreationError> create(Id userId) {
    return Team.fromChangeLog(ChangeLog.createWithBase(new Created(userId)));
  }

  ///// Mutation Events /////

  public static class Created extends Event {

    private Id userId;

    public Created(Id userId) {
      this.userId = userId;
    }
  }

  public static class PlayerAdded extends Event {

    private Id playerId;

    public PlayerAdded(Id playerId) {
      this.playerId = playerId;
    }
  }

  //// Mutation Execution ////

  public Result<Team, AddPlayerError> addedPlayer(Id thePlayerId) {
    return this.applied(new PlayerAdded(thePlayerId)).mapErrors(AddPlayerError::new);
  }

  private Result<Team, ProblemDiagnosis> applied(Event change) {
    return this.mutated(change).changeLogged(change).validated();
  }

  private Team changeLogged(Event change) {
    return this.copied(theCopy -> theCopy.changeLog = this.changeLog.changeAppended(change));
  }

  private Team mutated(Event change) {
    return Match(change).of(
      Case($(instanceOf(Created.class)), this::when),
      Case($(instanceOf(PlayerAdded.class)), this::when)
    );
  }

  private Team when(Created the) {
    return this.copied(theCopy -> {
      theCopy.id = Id.next();
      theCopy.createdAt = Instant.now();
      final Set<Id> players = new HashSet<>();
      players.add(the.userId);
      theCopy.players = players;
    });
  }

  private Team when(PlayerAdded the) {
    return this.copied(theCopy -> theCopy.players().add(the.playerId));
  }

  ///// Errors /////

  public static class CreationError extends DomainError {

    public CreationError(ProblemDiagnosis diagnosis) {
      super(diagnosis);
    }

    @Override
    protected List<Problem> choices() {
      return List.of(DomainProblem.MANDATORY_VALUE_MISSING);
    }
  }

  public static class AddPlayerError extends DomainError {

    public AddPlayerError(ProblemDiagnosis diagnosis) {
      super(diagnosis);
    }

    @Override
    protected List<Problem> choices() {
      return List.of(DomainProblem.MANDATORY_VALUE_MISSING);
    }
  }
}

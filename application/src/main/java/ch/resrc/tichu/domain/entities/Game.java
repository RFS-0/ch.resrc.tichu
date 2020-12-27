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
import ch.resrc.tichu.domain.value_objects.JoinCode;
import ch.resrc.tichu.domain.value_objects.Round;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Game implements ChangeLogging {

  private Id id;
  private JoinCode joinCode;
  private Team team;
  private Team otherTeam;
  private Set<Round> rounds;
  private Instant createdAt;
  private Instant finishedAt;

  private ChangeLog changeLog = ChangeLog.empty();

  ///// Characteristics /////

  @Override
  public ChangeLog changeLog() {
    return changeLog;
  }

  public Id id() {
    return id;
  }

  public JoinCode joinCode() {
    return joinCode;
  }

  public Team team() {
    return team;
  }

  public Team otherTeam() {
    return otherTeam;
  }

  public Set<Round> rounds() {
    return rounds;
  }

  public Instant createdAt() {
    return createdAt;
  }

  public Instant finishedAt() {
    return finishedAt;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
      .append("id", id)
      .append("joinCode", joinCode)
      .append("team", team)
      .append("otherTeam", otherTeam)
      .append("rounds", rounds)
      .append("createdAt", createdAt)
      .append("finishedAt", finishedAt)
      .append("events", changeLog.asEventClassNames())
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

    Game game = (Game) o;

    return id.equals(game.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

///// Constraints /////

  private static Validation<Game, ValidationError> mandatoryProperties() {
    return allOf(
      attribute(x -> x.id, notNull(mustBeSpecifiedMsg(), context(Id.class))),
      attribute(x -> x.joinCode, notNull(mustBeSpecifiedMsg(), context(JoinCode.class))),
      attribute(x -> x.team, notNull(mustBeSpecifiedMsg(), context(Team.class))),
      attribute(x -> x.rounds, notNull(cannotBeUndefinedMsg(), context(Round.class))),
      attribute(x -> x.createdAt, notNull(cannotBeUndefinedMsg(), context(Instant.class)))
    );
  }

  private Result<Game, ProblemDiagnosis> validated() {
    return mandatoryProperties().mapErrors(mandatoryPropertyMissing()).applyTo(this);
  }

  ///// Construction /////

  private Game() {
  }

  private Game(Game other) {
    this.id = other.id;
    this.joinCode = other.joinCode;
    this.team = other.team;
    this.otherTeam = other.otherTeam;
    this.rounds = other.rounds;
    this.createdAt = other.createdAt;
    this.finishedAt = other.createdAt;
    this.changeLog = other.changeLog;
  }

  private Game copied(Consumer<Game> modification) {
    final var theCopy = new Game(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static Result<Game, CreationError> createdBy(Id userId) {
    return Game.fromChangeLog(ChangeLog.createWithBase(new Created(userId)));
  }

  public static Result<Game, CreationError> fromChangeLog(ChangeLog changeLog) {
    Game newGame = folded(new Game(), changeLog.allEvents(), Game::mutated);
    newGame.changeLog = changeLog;
    return newGame.validated().mapErrors(CreationError::new);
  }

  ///// Mutation Events /////

  public static class Created extends Event {

    private Id userId;

    public Created(Id userId) {
      this.userId = userId;
    }
  }

  public static class Finished extends Event {

    private Instant finishedAt;

    public Finished(Instant finishedAt) {
      this.finishedAt = finishedAt;
    }
  }

  //// Mutation Execution ////

  public Result<Game, FinishError> finishedAt(Instant time) {
    return this.applied(new Finished(time)).mapErrors(FinishError::new);
  }


  private Result<Game, ProblemDiagnosis> applied(Event change) {
    return this.mutated(change).changeLogged(change).validated();
  }

  private Game changeLogged(Event change) {
    return this.copied(theCopy -> theCopy.changeLog = this.changeLog.changeAppended(change));
  }

  private Game mutated(Event change) {
    return Match(change).of(
      Case($(instanceOf(Created.class)), this::when),
      Case($(instanceOf(Finished.class)), this::when)
    );
  }

  private Game when(Created the) {
    return this.copied(theCopy -> {
      theCopy.id = Id.next();
      theCopy.joinCode = JoinCode.next();
      theCopy.team = Team.create(the.userId).value();
      theCopy.rounds = new HashSet<>();
      theCopy.createdAt = Instant.now();
    });
  }

  private Game when(Finished the) {
    return this.copied(theCopy -> theCopy.finishedAt = the.finishedAt);
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

  public static class FinishError extends DomainError {

    public FinishError(ProblemDiagnosis diagnosis) {
      super(diagnosis);
    }

    @Override
    protected List<Problem> choices() {
      return List.of(DomainProblem.MANDATORY_VALUE_MISSING);
    }
  }
}

package ch.resrc.tichu.usecases.create_game.ports.documents;

import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.cannotBeUndefinedMsg;
import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.context;
import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.mustBeSpecifiedMsg;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.modified;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;

import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.capabilities.validation.Validations;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.JoinCode;
import ch.resrc.tichu.domain.value_objects.Round;
import java.time.Instant;
import java.util.Set;
import java.util.function.Consumer;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GameDocument {

  private Id id;
  private JoinCode joinCode;
  private Team team;
  private Team otherTeam;
  private Set<Round> rounds;
  private Instant createdAt;
  private Instant finishedAt;

  ///// Characteristics /////

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
    return new ToStringBuilder(this)
      .append("id", id)
      .append("joinCode", joinCode)
      .append("team", team)
      .append("otherTeam", otherTeam)
      .append("rounds", rounds)
      .append("createdAt", createdAt)
      .append("finishedAt", finishedAt)
      .toString();
  }

  ///// Constraints /////

  private static Validation<GameDocument, ValidationError> validation() {
    return modified(Validations.allOf(
      attribute(x -> x.id, notNull(mustBeSpecifiedMsg(), context(Id.class))),
      attribute(x -> x.joinCode, notNull(mustBeSpecifiedMsg(), context(JoinCode.class))),
      attribute(x -> x.team, notNull(mustBeSpecifiedMsg(), context(Team.class))),
      attribute(x -> x.rounds, notNull(cannotBeUndefinedMsg(), context(Round.class))),
      attribute(x -> x.createdAt, notNull(cannotBeUndefinedMsg(), context(Instant.class)))
    ), context(GameDocument.class));
  }

  private GameDocument copied(Consumer<GameDocument> modification) {
    var theCopy = new GameDocument(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static GameDocument.Builder aGameDocument() {
    return new Builder();
  }

  public GameDocument.Builder but() {

    return new Builder(this.copied(__ -> {
    }));
  }

  ///// Construction /////

  private GameDocument() {
  }

  private GameDocument(GameDocument other) {
    this.id = other.id;
    this.joinCode = other.joinCode;
    this.team = other.team;
    this.otherTeam = other.otherTeam;
    this.rounds = other.rounds;
    this.createdAt = other.createdAt;
    this.finishedAt = other.finishedAt;
  }

  public static class Builder {

    private final GameDocument workpiece;

    public Builder() {
      this.workpiece = new GameDocument();
    }

    public Builder(GameDocument workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withId(Id id) {
      return new Builder(workpiece.copied(but -> but.id = id));
    }

    public Builder withJoinCode(JoinCode joinCode) {
      return new Builder(workpiece.copied(but -> but.joinCode = joinCode));
    }

    public Builder withTeam(Team team) {
      return new Builder(workpiece.copied(but -> but.team = team));
    }

    public Builder withOtherTeam(Team otherTeam) {
      return new Builder(workpiece.copied(but -> but.otherTeam = otherTeam));
    }

    public Builder withRounds(Set<Round> rounds) {
      return new Builder(workpiece.copied(but -> but.rounds = rounds));
    }

    public Builder withCreatedAt(Instant createdAt) {
      return new Builder(workpiece.copied(but -> but.createdAt = createdAt));
    }

    public Builder withFinishedAt(Instant finishedAt) {
      return new Builder(workpiece.copied(but -> but.finishedAt = finishedAt));
    }

    public Result<GameDocument, ValidationError> buildResult() {
      return validation().applyTo(workpiece);
    }

    public GameDocument build() {
      return buildResult().getOrThrow(InvalidInputDetected::of);
    }
  }
}

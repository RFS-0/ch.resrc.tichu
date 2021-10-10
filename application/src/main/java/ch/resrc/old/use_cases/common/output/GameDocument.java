package ch.resrc.old.use_cases.common.output;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.entities.*;
import ch.resrc.old.domain.value_objects.*;
import ch.resrc.old.use_cases.find_or_create_user.ports.output.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.time.*;
import java.util.function.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

public class GameDocument {

  private Id id;
  private UserDocument createdBy;
  private JoinCode joinCode;
  private Set<TeamDocument> teams;
  private Set<Round> rounds;
  private Instant createdAt;
  private Instant finishedAt;

  private GameDocument() {
  }

  private GameDocument(GameDocument other) {
    this.id = other.id;
    this.createdBy = other.createdBy;
    this.joinCode = other.joinCode;
    this.teams = other.teams;
    this.rounds = other.rounds;
    this.createdAt = other.createdAt;
    this.finishedAt = other.finishedAt;
  }

  public static GameDocument.Builder aGameDocument() {
    return new Builder();
  }

  public static GameDocument fromGame(Game game) {
    return GameDocument.aGameDocument()
      .withId(game.id())
      .withCreatedBy(UserDocument.fromUser(game.createdBy()))
      .withJoinCode(game.joinCode())
      .withTeams(TeamDocument.fromTeams(game.teams()))
      .withRounds(game.rounds())
      .withCreatedAt(game.createdAt())
      .withFinishedAt(game.finishedAt())
      .build();
  }

  private GameDocument copied(Consumer<GameDocument> modification) {
    var theCopy = new GameDocument(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public GameDocument.Builder but() {

    return new Builder(this.copied(__ -> {
    }));
  }

  private static Validation<Seq<ValidationError>, GameDocument> validation() {
    return allOf(
      attribute(x -> x.id, notNull(errorDetails("must not be null"))),
      attribute(x -> x.createdBy, notNull(errorDetails("must not be null"))),
      attribute(x -> x.joinCode, notNull(errorDetails("must not be null"))),
      attribute(x -> x.rounds, notNull(errorDetails("must not be null"))),
      attribute(x -> x.createdAt, notNull(errorDetails("must not be null")))
    );
  }

  public Id id() {
    return id;
  }

  public UserDocument createdBy() {
    return createdBy;
  }

  public JoinCode joinCode() {
    return joinCode;
  }

  public Set<TeamDocument> teams() {
    return teams;
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

  public static class Builder {

    private final GameDocument workpiece;

    private Builder() {
      this.workpiece = new GameDocument();
    }

    private Builder(GameDocument workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withId(Id id) {
      return new Builder(workpiece.copied(but -> but.id = id));
    }

    public Builder withCreatedBy(UserDocument createdBy) {
      return new Builder(workpiece.copied(but -> but.createdBy = createdBy));
    }

    public Builder withJoinCode(JoinCode joinCode) {
      return new Builder(workpiece.copied(but -> but.joinCode = joinCode));
    }

    public Builder withTeams(Set<TeamDocument> teams) {
      return new Builder(workpiece.copied(but -> but.teams = teams));
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

    public Either<Seq<ValidationError>, GameDocument> buildResult() {
      return validation().applyTo(workpiece);
    }

    public GameDocument build() {
      return buildResult().getOrElseThrow(InvalidInputDetected::of);
    }
  }
}
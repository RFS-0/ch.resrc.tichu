package ch.resrc.tichu.use_cases.teams.ports.output;

import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import ch.resrc.tichu.use_cases.ports.documents.PlayerDocument;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import io.vavr.control.Either;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;
import static ch.resrc.tichu.use_cases.teams.ports.output.TeamDocumentValidationError.MUST_NOT_BE_NULL;

public class TeamDocument {

  private Id id;
  private Name name;
  private PlayerDocument firstPlayer;
  private PlayerDocument secondPlayer;

  public Id id() {
    return id;
  }

  public Name name() {
    return name;
  }

  public PlayerDocument firstPlayer() { return firstPlayer; }

  public PlayerDocument secondPlayer() {
    return secondPlayer;
  }

  private TeamDocument() {
  }

  private TeamDocument(TeamDocument other) {
    this.id = other.id;
    this.name = other.name;
    this.firstPlayer = other.firstPlayer;
    this.secondPlayer = other.secondPlayer;
  }

  public static TeamDocument fromTeam(Team team) {
    return aTeamDocument()
      .withId(team.id())
      .withName(team.name())
      .withFirstPlayer(PlayerDocument.fromPlayer(team.firstPlayer()))
      .withSecondPlayer(PlayerDocument.fromPlayer(team.secondPlayer()))
      .build();
  }

  public static Set<TeamDocument> fromTeams(Set<Team> teams) {
    return teams.map(TeamDocument::fromTeam);
  }

  private TeamDocument copied(Consumer<TeamDocument> modification) {
    var theCopy = new TeamDocument(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static TeamDocument.Builder aTeamDocument() {
    return new Builder();
  }

  public TeamDocument.Builder but() {
    return new Builder(this.copied(__ -> {
    }));
  }

  private static Validation<Seq<ValidationError>, TeamDocument> validation() {
    return allOf(
      attribute(x -> x.id, notNull(MUST_NOT_BE_NULL))
    );
  }

  public static class Builder {

    private final TeamDocument workpiece;

    private Builder() {
      this.workpiece = new TeamDocument();
    }

    private Builder(TeamDocument workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withId(Id id) {
      return new Builder(workpiece.copied(but -> but.id = id));
    }

    public Builder withName(Name name) {
      return new Builder(workpiece.copied(but -> but.name = name));
    }

    public Builder withFirstPlayer(PlayerDocument firstPlayer) {
      return new Builder(workpiece.copied(but -> but.firstPlayer = firstPlayer));
    }

    public Builder withSecondPlayer(PlayerDocument secondPlayer) {
      return new Builder(workpiece.copied(but -> but.secondPlayer = secondPlayer));
    }

    public Either<Seq<ValidationError>, TeamDocument> buildResult() {
      return validation().applyTo(workpiece);
    }

    public TeamDocument build() {
      return buildResult().getOrElseThrow(InvalidInputDetected::of);
    }
  }
}

class TeamDocumentValidationError {

  static final Supplier<ValidationError> MUST_NOT_BE_NULL = () -> ValidationError.of(
    TeamDocument.class.getName(), "must not be null"
  );

}

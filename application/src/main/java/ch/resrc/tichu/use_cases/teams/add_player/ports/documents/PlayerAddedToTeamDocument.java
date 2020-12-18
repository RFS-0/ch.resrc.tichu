package ch.resrc.tichu.use_cases.teams.add_player.ports.documents;

import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.use_cases.ports.documents.PlayerDocument;
import ch.resrc.tichu.use_cases.ports.documents.TeamDocument;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;

public class PlayerAddedToTeamDocument {

  private PlayerDocument player;
  private TeamDocument team;

  ///// Characteristics /////

  public PlayerDocument player() {
    return player;
  }

  public TeamDocument team() {
    return team;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("player", player)
      .append("team", team)
      .toString();
  }

  ///// Constraints /////

  private static Validation<Seq<ValidationError>, PlayerAddedToTeamDocument> validation() {
    Supplier<ValidationError> error = () -> ValidationError.of("", "");
    return allOf(
      attribute(x -> x.player, notNull(error)),
      attribute(x -> x.team, notNull(error))
    );
  }

  ///// Construction /////

  private PlayerAddedToTeamDocument() {
  }

  public PlayerAddedToTeamDocument(PlayerAddedToTeamDocument other) {
    this.player = other.player;
    this.team = other.team;
  }

  private PlayerAddedToTeamDocument copied(Consumer<PlayerAddedToTeamDocument> modification) {
    final var theCopy = new PlayerAddedToTeamDocument(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static Builder aPlayerAddedToTeamDocument() {
    return new Builder();
  }

  public Builder but() {
    return new Builder(this.copied(__ -> {
    }));
  }

  public static class Builder {

    private final PlayerAddedToTeamDocument workpiece;

    private Builder() {
      this.workpiece = new PlayerAddedToTeamDocument();
    }

    private Builder(PlayerAddedToTeamDocument workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withPlayer(PlayerDocument player) {
      return new Builder(workpiece.copied(but -> but.player = player));
    }

    public Builder withTeam(TeamDocument team) {
      return new Builder(workpiece.copied(but -> but.team = team));
    }

    public Either<Seq<ValidationError>, PlayerAddedToTeamDocument> buildResult() {
      return validation().applyTo(workpiece);
    }

    public PlayerAddedToTeamDocument build() {
      return buildResult().getOrElseThrow(InvalidInputDetected::of);
    }
  }
}

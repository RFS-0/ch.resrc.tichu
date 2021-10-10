package ch.resrc.old.use_cases.update_card_points_of_round.ports.input;

import ch.resrc.old.capabilities.validations.old.Validation;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.value_objects.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.util.function.*;

import static ch.resrc.old.capabilities.validations.old.Validations.*;
import static ch.resrc.old.domain.validation.DomainValidationErrors.*;

public class IntendedCardPointsUpdate {

  private Id gameId;
  private Id teamId;
  private RoundNumber roundNumber;
  private int cardPoints;

  public IntendedCardPointsUpdate() {
  }

  public IntendedCardPointsUpdate(IntendedCardPointsUpdate other) {
    this.gameId = other.gameId;
    this.teamId = other.teamId;
    this.roundNumber = other.roundNumber;
    this.cardPoints = other.cardPoints;
  }

  private IntendedCardPointsUpdate copied(Consumer<IntendedCardPointsUpdate> modification) {
    var theCopy = new IntendedCardPointsUpdate(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static Builder anIntendedCardPointsUpdate() {
    return new Builder();
  }

  private static Validation<Seq<ValidationError>, IntendedCardPointsUpdate> validation() {
    return allOf(
      attribute(x -> x.gameId, notNull(mustNotBeNull())),
      attribute(x -> x.teamId, notNull(mustNotBeNull())),
      attribute(x -> x.roundNumber, notNull(mustNotBeNull())),
      attribute(x -> x.cardPoints, notNull(mustNotBeNull()))
    );
  }

  public Id gameId() {
    return gameId;
  }

  public Id teamId() {
    return teamId;
  }

  public RoundNumber roundNumber() {
    return roundNumber;
  }

  public int cardPoints() {
    return cardPoints;
  }

  public static class Builder {

    IntendedCardPointsUpdate workpiece;

    public Builder() {
      this.workpiece = new IntendedCardPointsUpdate();
    }

    public Builder(IntendedCardPointsUpdate workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withGameId(Id gameId) {
      return new Builder(workpiece.copied(but -> but.gameId = gameId));
    }

    public Builder withTeamId(Id teamId) {
      return new Builder(workpiece.copied(but -> but.teamId = teamId));
    }

    public Builder withRoundNumber(RoundNumber roundNumber) {
      return new Builder(workpiece.copied(but -> but.roundNumber = roundNumber));
    }

    public Builder withCardPoints(int cardPoints) {
      return new Builder(workpiece.copied(but -> but.cardPoints = cardPoints));
    }

    public Either<Seq<ValidationError>, IntendedCardPointsUpdate> buildResult() {
      return validation().applyTo(workpiece);
    }
  }
}

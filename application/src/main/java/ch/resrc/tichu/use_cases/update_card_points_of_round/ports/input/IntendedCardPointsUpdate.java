package ch.resrc.tichu.use_cases.update_card_points_of_round.ports.input;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.CardPoints;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.RoundNumber;
import io.vavr.collection.Seq;

import java.util.function.Consumer;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;
import static ch.resrc.tichu.domain.validation.DomainValidationErrors.mustNotBeNull;

public class IntendedCardPointsUpdate {

  private Id gameId;
  private Id teamId;
  private RoundNumber roundNumber;
  private CardPoints cardPoints;

  public IntendedCardPointsUpdate(IntendedCardPointsUpdate other) {
    this.gameId = other.gameId;
    this.teamId = other.teamId;
    this.roundNumber = other.roundNumber;
    this.cardPoints = other.cardPoints;
  }

  private static Validation<Seq<ValidationError>, IntendedCardPointsUpdate> validation() {
    return allOf(
      attribute(x -> x.gameId, notNull(mustNotBeNull())),
      attribute(x -> x.teamId, notNull(mustNotBeNull())),
      attribute(x -> x.roundNumber, notNull(mustNotBeNull())),
      attribute(x -> x.cardPoints, notNull(mustNotBeNull()))
    );
  }

  private IntendedCardPointsUpdate copied(Consumer<IntendedCardPointsUpdate> modification) {
    var theCopy = new IntendedCardPointsUpdate(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static class Builder {

  }
}

package ch.resrc.tichu.adapters.endpoints_websocket.finish_round.dto;

import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.RoundNumber;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.function.Consumer;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;
import static ch.resrc.tichu.domain.validation.DomainValidationErrors.mustNotBeNull;

public class IntendedRoundFinishDto {

  private Id gameId;
  private RoundNumber roundNumber;

  public IntendedRoundFinishDto() {
  }

  public IntendedRoundFinishDto(IntendedRoundFinishDto other) {
    this.gameId = other.gameId;
    this.roundNumber = other.roundNumber;
  }

  private IntendedRoundFinishDto copied(Consumer<IntendedRoundFinishDto> modification) {
    var theCopy = new IntendedRoundFinishDto(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static Builder anIntendedCardPointsUpdate() {
    return new Builder();
  }

  private static Validation<Seq<ValidationError>, IntendedRoundFinishDto> validation() {
    return allOf(
      attribute(x -> x.gameId, notNull(mustNotBeNull())),
      attribute(x -> x.roundNumber, notNull(mustNotBeNull()))
    );
  }

  public Id gameId() {
    return gameId;
  }

  public RoundNumber roundNumber() {
    return roundNumber;
  }

  public static class Builder {

    IntendedRoundFinishDto workpiece;

    public Builder() {
      this.workpiece = new IntendedRoundFinishDto();
    }

    public Builder(IntendedRoundFinishDto workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withGameId(Id gameId) {
      return new Builder(workpiece.copied(but -> but.gameId = gameId));
    }

    public Builder withRoundNumber(RoundNumber roundNumber) {
      return new Builder(workpiece.copied(but -> but.roundNumber = roundNumber));
    }

    public Either<Seq<ValidationError>, IntendedRoundFinishDto> buildResult() {
      return validation().applyTo(workpiece);
    }
  }
}

package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import static ch.resrc.tichu.capabilities.validation.ValidationError.Claim.*;
import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.*;
import static ch.resrc.tichu.capabilities.validation.Validations.chained;
import static ch.resrc.tichu.capabilities.validation.Validations.*;
import static ch.resrc.tichu.domain.validation.DomainValidations.*;

public class CardPoints {

    private final Map<Id, Integer> teamIdsToPoints;

    private CardPoints(Map<Id, Integer> values) {
        this.teamIdsToPoints = values;
    }

    private static Validation<Map<Id, Integer>, ValidationError> validation() {
        return modified(
                chained(notNull(cannotBeUndefinedMsg()),
                        allOf(attribute(Map::size, equalTo(2)),
                                attribute(Map::values, allDivisibleBy(5)),
                                attribute(x -> x.values().toList().sorted(), allMin(-25)),
                                attribute(x -> x.values().toList().sorted(), allMax(125)),
                                attribute(x -> x.values().sum(), equalTo(100)))),
                context(CardPoints.class),
                claimed(SAFE_VALUE)
        );
    }

    public static CardPoints of(Map<Id, Integer> teamIdToPoints) {
        return resultOf(teamIdToPoints).getOrThrow(invariantViolated());
    }

    Option<Integer> ofTeam(Id teamId) {
        return teamIdsToPoints.get(teamId);
    }

    public static Result<CardPoints, ValidationError> resultOf(Map<Id, Integer> teamIdsToPoints) {
        return validation().applyTo(teamIdsToPoints).map(CardPoints::new);
    }

    public Map<Id, Integer> teamIdsToPoints() {
        return teamIdsToPoints;
    }
}

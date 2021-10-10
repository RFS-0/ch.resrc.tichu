package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import java.util.function.*;

import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.*;
import static ch.resrc.tichu.capabilities.validation.Validations.chained;
import static ch.resrc.tichu.capabilities.validation.Validations.*;

public class Ranks extends DomainPrimitive<Ranks, Map<Id, Rank>> {

    private final Map<Id, Rank> playerIdToRank;

    private Ranks(Map<Id, Integer> playerIdToValue) {
        this.playerIdToRank = playerIdToValue.bimap(Function.identity(), Rank::of);
    }

    private static Validation<Map<Id, Integer>, ValidationError> validation() {
        return modified(
                chained(
                        notNull(),
                        attribute(Map::keySet, noneNull()),
                        attribute(Map::values, noneNull()),
                        allOf(
                                attribute(x -> x.keySet().length(), equalTo(4, msg("exactly four players must have a rank"))),
                                attribute(x -> x.values().toList().sorted(), allMin(0, msg("a rank can not be smaller than zero"))),
                                attribute(x -> x.values().toList().sorted(), allMax(4, msg("a rank can not be higher than four")))
                        )),
                context(Ranks.class)
        );
    }

    public static Result<Ranks, ValidationError> resultOf(Map<Id, Integer> values) {
        return validation().applyTo(values).map(Ranks::new);
    }

    public Set<Id> playerIds() {
        return playerIdToRank.keySet();
    }

    public Seq<Rank> ranks() {
        return playerIdToRank.values();
    }

    public Rank rankOfPlayer(Id playerId) {
        return playerIdToRank.get(playerId).getOrElse(Rank.NONE);
    }

    public Option<Id> findPlayerWithRank(Rank rank) {
        return playerIdToRank
                .filterValues(rankOfPlayer -> rankOfPlayer == rank)
                .keySet()
                .headOption();
    }

    public Result<Ranks, ValidationError> nextRank(Id playerId) {
        int nextRankValue = playerIdToRank.values().map(Rank::value).max().get() + 1;

        return resultOf(
                playerIdToRank.bimap(Function.identity(), Rank::value)
                        .put(playerId, nextRankValue)
        );
    }

    public Result<Ranks, ValidationError> resetRank(Id playerId) {
        Rank rankOfPlayer = playerIdToRank.get(playerId).getOrElse(Rank.NONE);

        var updatedRanks = playerIdToRank
                .mapValues(
                        rank -> {
                            if (rank != Rank.NONE && rank.value() > rankOfPlayer.value()) {
                                return Rank.of(rank.value() - 1);
                            } else {
                                return rank;
                            }
                        }
                )
                .put(playerId, Rank.NONE)
                .bimap(Function.identity(), Rank::value);

        return resultOf(updatedRanks);
    }

    @Override
    protected Map<Id, Rank> getPrimitiveValue() {
        return playerIdToRank;
    }
}

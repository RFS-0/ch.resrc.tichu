package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.*;
import ch.resrc.tichu.domain.entities.*;
import io.vavr.collection.*;
import io.vavr.control.*;

import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.*;
import static ch.resrc.tichu.capabilities.validation.Validations.chained;
import static ch.resrc.tichu.capabilities.validation.Validations.*;
import static ch.resrc.tichu.domain.validation.DomainValidations.*;

public class Ranks {

    private final Map<PlayerId, Rank> playerIdToRank;

    private Ranks(Map<PlayerId, Rank> playerIdToRank) {
        this.playerIdToRank = playerIdToRank;
    }

    private static Validation<Map<PlayerId, Rank>, ValidationError> validation() {
        return modified(
                chained(
                        notNull(),
                        attribute(Map::keySet, noneNull()),
                        attribute(Map::values, noneNull()),
                        attribute(x -> x.keySet().length(), equalTo(4, msg("exactly four players must have a rank")))
                ),
                context(Ranks.class)
        );
    }

    public static Result<Ranks, ValidationError> resultOf(Map<PlayerId, Rank> values) {
        return validation().applyTo(values).map(Ranks::new);
    }

    public static Ranks of(Map<PlayerId, Rank> values) {
        return resultOf(values).getOrThrow(invariantViolated());
    }

    public Set<PlayerId> playerIds() {
        return playerIdToRank.keySet();
    }

    public Seq<Rank> ranks() {
        return playerIdToRank.values();
    }

    public Rank rankOfPlayer(PlayerId playerId) {
        return playerIdToRank.get(playerId).getOrElse(Rank.NONE);
    }

    public Option<PlayerId> findPlayerWithRank(Rank rank) {
        return playerIdToRank
                .filterValues(rankOfPlayer -> rankOfPlayer == rank)
                .keySet()
                .headOption();
    }

    public Result<Ranks, ValidationError> nextRank(PlayerId playerId) {
        int nextRankValue = playerIdToRank.values().map(Rank::value).max().get() + 1;

        return resultOf(playerIdToRank.put(playerId, Rank.of(nextRankValue)));
    }

    public Result<Ranks, ValidationError> resetRank(PlayerId playerId) {
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
                .put(playerId, Rank.NONE);

        return resultOf(updatedRanks);
    }

    public Map<PlayerId, Rank> playerIdToRank() {
        return playerIdToRank;
    }
}

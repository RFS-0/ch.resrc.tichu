package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.*;
import ch.resrc.tichu.test.capabilities.habits.assertions.*;
import io.vavr.collection.*;
import org.junit.jupiter.api.*;

import static ch.resrc.tichu.test.capabilities.habits.assertions.IsValidationError.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.hamcrest.Matchers.*;

class RanksTest {

    @Test
    void validValues_resultOf_success() {
        // given:
        var firstPlayer = Id.next();
        var secondPlayer = Id.next();
        var thirdPlayer = Id.next();
        var fourthPlayer = Id.next();
        var validValues = HashMap.of(
                firstPlayer, Rank.FIRST,
                secondPlayer, Rank.SECOND,
                thirdPlayer, Rank.THIRD,
                fourthPlayer, Rank.FOURTH
        );

        // when:
        var result = Ranks.resultOf(validValues);

        // then:
        result.failureEffect(System.out::println);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.value().rankOfPlayer(firstPlayer)).isEqualTo(Rank.FIRST);
        assertThat(result.value().rankOfPlayer(secondPlayer)).isEqualTo(Rank.SECOND);
        assertThat(result.value().rankOfPlayer(thirdPlayer)).isEqualTo(Rank.THIRD);
        assertThat(result.value().rankOfPlayer(fourthPlayer)).isEqualTo(Rank.FOURTH);
    }

    @Test
    void null_resultOf_failure() {
        // given:
        Map<Id, Rank> input = null;

        // when:
        var result = Ranks.resultOf(input);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(whereErrorMessage(containsString("must not be null")))
        );
    }

    @Test
    void nullKey_resultOf_failure() {
        // given:
        Id nullKey = null;
        var secondPlayer = Id.next();
        var thirdPlayer = Id.next();
        var fourthPlayer = Id.next();
        var valuesWithNullKey = HashMap.of(
                nullKey, Rank.FIRST,
                secondPlayer, Rank.SECOND,
                thirdPlayer, Rank.THIRD,
                fourthPlayer, Rank.FOURTH
        );

        // when:
        var result = Ranks.resultOf(valuesWithNullKey);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(whereErrorMessage(containsString("must not contain null")))
        );
    }

    @Test
    void nullValue_resultOf_failure() {
        // given:
        var firstPlayer = Id.next();
        var secondPlayer = Id.next();
        var thirdPlayer = Id.next();
        var fourthPlayer = Id.next();
        var valuesWithNullValue = HashMap.of(
                firstPlayer, null,
                secondPlayer, Rank.SECOND,
                thirdPlayer, Rank.THIRD,
                fourthPlayer, Rank.FOURTH
        );

        // when:
        var result = Ranks.resultOf(valuesWithNullValue);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                contains(whereErrorMessage(containsString("must not contain null")))
        );
    }

    @Test()
    void tooManyPlayers_resultOf_failure() {
        // given:
        var firstPlayer = Id.next();
        var secondPlayer = Id.next();
        var thirdPlayer = Id.next();
        var fourthPlayer = Id.next();
        var fifthPlayer = Id.next();
        var tooManyPlayers = HashMap.of(
                firstPlayer, Rank.FIRST,
                secondPlayer, Rank.SECOND,
                thirdPlayer, Rank.THIRD,
                fourthPlayer, Rank.FOURTH,
                fifthPlayer, Rank.FIRST
        );

        // when:
        var result = Ranks.resultOf(tooManyPlayers);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                hasItem(whereErrorMessage(containsString("exactly four players must have a rank")))
        );
    }

    @Test
    void notEnoughPlayers_resultOf_failure() {
        // given:
        var firstPlayer = Id.next();
        var secondPlayer = Id.next();
        var thirdPlayer = Id.next();
        var notEnoughPlayers = HashMap.of(
                firstPlayer, Rank.FIRST,
                secondPlayer, Rank.SECOND,
                thirdPlayer, Rank.THIRD
        );

        // when:
        var result = Ranks.resultOf(notEnoughPlayers);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                hasItem(whereErrorMessage(containsString("exactly four players must have a rank")))
        );
    }

    @Test
    void noPlayerFinished_nextRank_firstRank() {
        // given:
        var noPlayerFinished = givenNoPlayerFinished();
        var somePlayer = noPlayerFinished.playerIds().get();

        // when:
        var result = noPlayerFinished.nextRank(somePlayer);

        // then:
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.value().rankOfPlayer(somePlayer)).isEqualTo(Rank.FIRST);
    }

    @Test
    void allPlayersFinished_resetRankOfSecondPlayer_rankOfSecondPlayerIsNoneAndRankOfThirdAndFourthPlayerDecreasedByOne() {
        // given:
        var allPlayersFinished = givenAllPlayersFinished();
        var secondPlayer = allPlayersFinished.findPlayerWithRank(Rank.SECOND).get();
        var thirdPlayer = allPlayersFinished.findPlayerWithRank(Rank.THIRD).get();
        var fourthPlayer = allPlayersFinished.findPlayerWithRank(Rank.FOURTH).get();

        // when:
        var result = allPlayersFinished.resetRank(secondPlayer);

        // then:
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.value().rankOfPlayer(secondPlayer)).isEqualTo(Rank.NONE);
        assertThat(result.value().rankOfPlayer(thirdPlayer)).isEqualTo(Rank.SECOND);
        assertThat(result.value().rankOfPlayer(fourthPlayer)).isEqualTo(Rank.THIRD);
    }

    private Ranks givenNoPlayerFinished() {
        return givenPlayersWithRanks(Rank.NONE, Rank.NONE, Rank.NONE, Rank.NONE);
    }

    private Ranks givenAllPlayersFinished() {
        return givenPlayersWithRanks(Rank.FIRST, Rank.SECOND, Rank.THIRD, Rank.FOURTH);
    }

    private Ranks givenPlayersWithRanks(Rank rank1, Rank rank2, Rank rank3, Rank rank4) {
        var firstPlayer = Id.next();
        var secondPlayer = Id.next();
        var thirdPlayer = Id.next();
        var fourthPlayer = Id.next();
        var playerWithTooHighRank = HashMap.of(
                firstPlayer, rank1,
                secondPlayer, rank2,
                thirdPlayer, rank3,
                fourthPlayer, rank4
        );

        // when:
        return Ranks.resultOf(playerWithTooHighRank).getOrThrow((InvalidInputDetected::of));
    }
}

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
                firstPlayer, 1,
                secondPlayer, 2,
                thirdPlayer, 3,
                fourthPlayer, 4
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
        Map<Id, Integer> input = null;

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
                nullKey, 1,
                secondPlayer, 2,
                thirdPlayer, 3,
                fourthPlayer, 4
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
                secondPlayer, 2,
                thirdPlayer, 3,
                fourthPlayer, 4
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
                firstPlayer, 1,
                secondPlayer, 2,
                thirdPlayer, 3,
                fourthPlayer, 4,
                fifthPlayer, 5
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
                firstPlayer, 1,
                secondPlayer, 2,
                thirdPlayer, 3
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
    void tooSmallRank_resultOf_failure() {
        // given:
        var firstPlayer = Id.next();
        var secondPlayer = Id.next();
        var thirdPlayer = Id.next();
        var fourthPlayer = Id.next();
        var tooSmallRank = -1;
        var playerWithTooSmallRank = HashMap.of(
                firstPlayer, 1,
                secondPlayer, 2,
                thirdPlayer, 3,
                fourthPlayer, tooSmallRank
        );

        // when:
        var result = Ranks.resultOf(playerWithTooSmallRank);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                hasItem(whereErrorMessage(containsString("a rank can not be smaller than zero")))
        );
    }

    @Test
    void tooHighRank_resultOf_failure() {
        // given:
        var firstPlayer = Id.next();
        var secondPlayer = Id.next();
        var thirdPlayer = Id.next();
        var fourthPlayer = Id.next();
        var tooHighRank = 5;
        var playerWithTooHighRank = HashMap.of(
                firstPlayer, 1,
                secondPlayer, 2,
                thirdPlayer, 3,
                fourthPlayer, tooHighRank
        );

        // when:
        var result = Ranks.resultOf(playerWithTooHighRank);

        // then:
        assertThat(result.isFailure()).isTrue();
        AssertionHabits.assertThat(
                result.errors(),
                hasItem(whereErrorMessage(containsString("a rank can not be higher than four")))
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
        return givenPlayersWithRanks(0, 0, 0, 0);
    }

    private Ranks givenAllPlayersFinished() {
        return givenPlayersWithRanks(1, 2, 3, 4);
    }

    private Ranks givenPlayersWithRanks(int rank1, int rank2, int rank3, int rank4) {
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

package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.*;
import ch.resrc.tichu.domain.entities.*;
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
        var firstPlayer = PlayerId.of("ca9368a0-fa9c-4cce-82ec-bd699cf92a33");
        var secondPlayer = PlayerId.of("f07ccbab-54e0-44c4-a9de-0783a0cbf452");
        var thirdPlayer = PlayerId.of("40bb6a00-d05d-4a1c-82ca-870cb6099594");
        var fourthPlayer = PlayerId.of("678372f5-7cb5-4e29-b759-0739d697f385");
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
        Map<PlayerId, Rank> input = null;

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
        PlayerId nullKey = null;
        var valuesWithNullKey = HashMap.of(
                nullKey, Rank.FIRST,
                PlayerId.of("041a6e0e-d5cf-4047-8901-0f3e3ab63a51"), Rank.SECOND,
                PlayerId.of("2ed2d2e0-9261-49a8-a5f7-20c36e0ac1e7"), Rank.THIRD,
                PlayerId.of("95c6b823-b46b-4b6c-a167-dd1e13776d96"), Rank.FOURTH
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
        var valuesWithNullValue = HashMap.of(
                PlayerId.of("aad5c57e-9d6b-4aee-ad38-67fe3ed5b2af"), null,
                PlayerId.of("cf2b6096-85aa-4f2b-9184-804ec011d3ad"), Rank.SECOND,
                PlayerId.of("aa1d05b5-a538-4559-8c95-3cf680c55c30"), Rank.THIRD,
                PlayerId.of("a654b05d-3832-4b04-9488-f0fd76d63d24"), Rank.FOURTH
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
        var tooManyPlayers = HashMap.of(
                PlayerId.of("e2a60e69-dde2-41a3-9f29-c47a66494b47"), Rank.FIRST,
                PlayerId.of("496943cc-a92c-4c31-bcb7-2bc04cbdd5d4"), Rank.SECOND,
                PlayerId.of("faf0d74d-f3a4-45c1-8a93-9b5f1c191a71"), Rank.THIRD,
                PlayerId.of("ec567644-6672-47cc-8ded-3f53ea1eb593"), Rank.FOURTH,
                PlayerId.of("a288124f-df41-41da-88af-e388c29b1e9b"), Rank.FIRST
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
        var notEnoughPlayers = HashMap.of(
                PlayerId.of("dadc9250-8234-441f-8ec3-3cdddca8ad08"), Rank.FIRST,
                PlayerId.of("874f45ea-c7c0-49c0-b54e-6ae85d245696"), Rank.SECOND,
                PlayerId.of("f56de489-9a2b-40b9-a0a0-3f8463d55a95"), Rank.THIRD
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
        var playerWithTooHighRank = HashMap.of(
                PlayerId.of("bd1ee20d-ca53-44e3-a401-fe6b629afc0c"), rank1,
                PlayerId.of("bea5dd43-dcfc-4808-949e-99a011ccb1a4"), rank2,
                PlayerId.of("2fde330f-41bf-4b16-971f-4cc47dbbd43f"), rank3,
                PlayerId.of("07c9ad49-af04-43de-96df-45c282d71eec"), rank4
        );

        // when:
        return Ranks.resultOf(playerWithTooHighRank).getOrThrow((InvalidInputDetected::of));
    }
}

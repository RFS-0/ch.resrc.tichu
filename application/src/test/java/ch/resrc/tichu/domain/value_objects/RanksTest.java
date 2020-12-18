package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class RanksTest {

  @Test
  @DisplayName("The [resultOf] four players and four legal values [1, 2, 3, 4] are valid ranks")
  void legalValues_resultOf_validRanks() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    HashMap<Id, Integer> validValues = HashMap.of(
      player1, 1,
      player2, 2,
      player3, 3,
      player4, 4
    );

    // when:
    var errorOrRanks = Ranks.resultOf(validValues);

    // then:
    errorOrRanks.peekLeft(System.out::println);
    assertThatNoException().isThrownBy(errorOrRanks::get);
    Ranks ranks = errorOrRanks.get();
    assertThat(ranks.values()).isEqualTo(validValues);
  }

  @ParameterizedTest(name = "The [resultOf] illegal input [{arguments}] is the validation error [MUST_NOT_BE_NULL]")
  @NullSource
  void null_resultOf_expectedError(Map<Id, Integer> input) {
    // given:
    ValidationError mustNotBeNullError = RanksValidationErrors.MUST_NOT_BE_NULL.get();

    // when:
    var errorOrRanks = Ranks.resultOf(input);

    // then:
    assertThatThrownBy(errorOrRanks::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRanks.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  @DisplayName("The [resultOf] illegal key [null] is the validation error [MUST_NOT_BE_NULL]")
  void nullKey_resultOf_expectedError() {
    // given:
    Id nullKey = null;
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    HashMap<Id, Integer> valuesWithNullKey = HashMap.of(
      nullKey, 1,
      player2, 2,
      player3, 3,
      player4, 4
    );
    ValidationError mustNotBeNullError = RanksValidationErrors.MUST_NOT_BE_NULL.get();

    // when:
    var errorOrRanks = Ranks.resultOf(valuesWithNullKey);

    // then:
    assertThatThrownBy(errorOrRanks::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRanks.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  @DisplayName("The [resultOf] illegal value [null] is the validation error [MUST_NOT_BE_NULL]")
  void nullValue_resultOf_expectedError() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    Integer nullValue = null;
    HashMap<Id, Integer> valuesWithNullValue = HashMap.of(
      player1, nullValue,
      player2, 2,
      player3, 3,
      player4, 4
    );
    ValidationError mustNotBeNullError = RanksValidationErrors.MUST_NOT_BE_NULL.get();

    // when:
    var errorOrRanks = Ranks.resultOf(valuesWithNullValue);

    // then:
    assertThatThrownBy(errorOrRanks::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRanks.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }

  @Test()
  @DisplayName("The [resultOf] values with too many players is the validation error [MUST_BE_DEFINED_FOR_FOUR_PLAYERS]")
  void notEnoughPlayers_resultOf_expectedError() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    Id player5 = Id.next();
    HashMap<Id, Integer> tooManyPlayers = HashMap.of(
      player1, 1,
      player2, 2,
      player3, 3,
      player4, 4,
      player5, 1
    );
    ValidationError mustBeDefinedForAllPlayersError = RanksValidationErrors.MUST_BE_DEFINED_FOR_FOUR_PLAYERS.get();

    // when:
    var errorOrRanks = Ranks.resultOf(tooManyPlayers);

    // then:
    assertThatThrownBy(errorOrRanks::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRanks.getLeft();
    assertThat(errors).hasSize(2);
    assertThat(errors).contains(mustBeDefinedForAllPlayersError);
  }

  @Test
  @DisplayName("The [resultOf] values with not enough players is the validation error [MUST_BE_DEFINED_FOR_FOUR_PLAYERS]")
  void tooFewPlayers_resultOf_expectedError() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    HashMap<Id, Integer> tooFewPlayers = HashMap.of(
      player1, 1,
      player2, 2,
      player3, 3
    );
    ValidationError mustBeDefinedForAllPlayersError = RanksValidationErrors.MUST_BE_DEFINED_FOR_FOUR_PLAYERS.get();

    // when:
    var errorOrRanks = Ranks.resultOf(tooFewPlayers);

    // then:
    assertThatThrownBy(errorOrRanks::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRanks.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustBeDefinedForAllPlayersError);
  }

  @Test
  @DisplayName("The [resultOf] duplicate rank values is the validation error [MUST_BE_DISTINCT_FOR_ALL_PLAYERS]")
  void duplicateRanks_resultOf_expectedError() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    int duplicateRank = 3;
    HashMap<Id, Integer> tooFewPlayers = HashMap.of(
      player1, 1,
      player2, 2,
      player3, duplicateRank,
      player4, duplicateRank
    );
    ValidationError mustBeDistinctForAllPlayersError = RanksValidationErrors.MUST_BE_DISTINCT_FOR_ALL_PLAYERS.get();

    // when:
    var errorOrRanks = Ranks.resultOf(tooFewPlayers);

    // then:
    assertThatThrownBy(errorOrRanks::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRanks.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustBeDistinctForAllPlayersError);
  }

  @Test
  @DisplayName("The [resultOf] a too small rank value is the validation error [MUST_NOT_BE_SMALLER_THAN_ONE]")
  void tooSmallRank_resultOf_expectedError() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    int tooSmallRank = 0;
    HashMap<Id, Integer> tooFewPlayers = HashMap.of(
      player1, 1,
      player2, 2,
      player3, 3,
      player4, tooSmallRank
    );
    ValidationError mustNotBeSmallerThanOneError = RanksValidationErrors.MUST_NOT_BE_SMALLER_THAN_ONE.get();

    // when:
    var errorOrRanks = Ranks.resultOf(tooFewPlayers);

    // then:
    assertThatThrownBy(errorOrRanks::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRanks.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeSmallerThanOneError);
  }

  @Test
  @DisplayName("The [resultOf] a too high rank value is the validation error [MUST_NOT_BE_HIGHER_THAN_FOUR]")
  void tooHighRank_resultOf_expectedError() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    int tooHighRank = 5;
    HashMap<Id, Integer> tooFewPlayers = HashMap.of(
      player1, 1,
      player2, 2,
      player3, 3,
      player4, tooHighRank
    );
    ValidationError mustNotBeHigherThanFourError = RanksValidationErrors.MUST_NOT_BE_HIGHER_THAN_FOUR.get();

    // when:
    var errorOrRanks = Ranks.resultOf(tooFewPlayers);

    // then:
    assertThatThrownBy(errorOrRanks::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRanks.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeHigherThanFourError);
  }
}

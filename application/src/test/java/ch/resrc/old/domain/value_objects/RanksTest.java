package ch.resrc.old.domain.value_objects;

import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.validation.*;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class RanksTest {

  @Test
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
    Ranks ranks = errorOrRanks.get();
    assertThat(ranks.values()).isEqualTo(validValues);
  }

  @ParameterizedTest
  @NullSource
  void null_resultOf_expectedError(Map<Id, Integer> input) {
    // given:
    ValidationError mustNotBeNullError = DomainValidationErrors.mustNotBeNull().apply(input);

    // when:
    var errorOrRanks = Ranks.resultOf(input);

    // then:
    assertThatThrownBy(errorOrRanks::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRanks.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
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
    ValidationError mustNotBeNullError = DomainValidationErrors.mustNotBeNull().apply(valuesWithNullKey);

    // when:
    var errorOrRanks = Ranks.resultOf(valuesWithNullKey);

    // then:
    assertThatThrownBy(errorOrRanks::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRanks.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
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
    ValidationError mustNotBeNullError = DomainValidationErrors.mustNotBeNull().apply(valuesWithNullValue);

    // when:
    var errorOrRanks = Ranks.resultOf(valuesWithNullValue);

    // then:
    assertThatThrownBy(errorOrRanks::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRanks.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }

  @Test()
  void tooManyPlayers_resultOf_expectedError() {
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
    ValidationError mustBeDefinedForAllPlayersError = DomainValidationErrors.errorDetails("four players must have a rank")
      .apply(tooManyPlayers.length());

    // when:
    var errorOrRanks = Ranks.resultOf(tooManyPlayers);

    // then:
    assertThatThrownBy(errorOrRanks::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRanks.getLeft();
    assertThat(errors).hasSize(2);
    assertThat(errors).contains(mustBeDefinedForAllPlayersError);
  }

  @Test
  void notEnoughtPlayers_resultOf_expectedError() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    HashMap<Id, Integer> notEnoughtPlayers = HashMap.of(
      player1, 1,
      player2, 2,
      player3, 3
    );
    ValidationError mustBeDefinedForAllPlayersError = DomainValidationErrors.errorDetails("four players must have a rank")
      .apply(notEnoughtPlayers.length());

    // when:
    var errorOrRanks = Ranks.resultOf(notEnoughtPlayers);

    // then:
    assertThatThrownBy(errorOrRanks::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRanks.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustBeDefinedForAllPlayersError);
  }

  @Test
  void duplicateRanks_resultOf_expectedError() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    int duplicateRank = 3;
    HashMap<Id, Integer> duplicatedRanks = HashMap.of(
      player1, 1,
      player2, 2,
      player3, duplicateRank,
      player4, duplicateRank
    );
    ValidationError mustBeDistinctForAllPlayersError = DomainValidationErrors.errorDetails("each player must have a distinct rank")
      .apply(duplicatedRanks.values().toList().sorted());

    // when:
    var errorOrRanks = Ranks.resultOf(duplicatedRanks);

    // then:
    assertThatThrownBy(errorOrRanks::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRanks.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustBeDistinctForAllPlayersError);
  }

  @Test
  void tooSmallRank_resultOf_expectedError() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    int tooSmallRank = 0;
    HashMap<Id, Integer> playerWithTooSmallRank = HashMap.of(
      player1, 1,
      player2, 2,
      player3, 3,
      player4, tooSmallRank
    );
    ValidationError mustNotBeSmallerThanOneError = DomainValidationErrors.errorDetails("a rank can not be smaller than one")
      .apply(playerWithTooSmallRank.values().toList().sorted());

    // when:
    var errorOrRanks = Ranks.resultOf(playerWithTooSmallRank);

    // then:
    assertThatThrownBy(errorOrRanks::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRanks.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeSmallerThanOneError);
  }

  @Test
  void tooHighRank_resultOf_expectedError() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    int tooHighRank = 5;
    HashMap<Id, Integer> playerWithTooHighRank = HashMap.of(
      player1, 1,
      player2, 2,
      player3, 3,
      player4, tooHighRank
    );
    ValidationError mustNotBeHigherThanFourError = DomainValidationErrors.errorDetails("a rank can not be higher than four")
      .apply(playerWithTooHighRank.values().toList().sorted());

    // when:
    var errorOrRanks = Ranks.resultOf(playerWithTooHighRank);

    // then:
    assertThatThrownBy(errorOrRanks::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRanks.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeHigherThanFourError);
  }
}

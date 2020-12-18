package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.validation.ValidationError;
import io.vavr.collection.HashMap;
import io.vavr.collection.Seq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class RoundTest {

  @Test
  @DisplayName("The [resultOf] legal values [RoundNumber, CardPoints, Ranks, Tichus] is a valid round")
  void legalValues_resultOf_validRound() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    final var leftTeamId = Id.next().value().toString();
    final var rightTeamId = Id.next().value().toString();
    RoundNumber roundNumber = RoundNumber.resultOf(1).get();
    CardPoints cardPoints = CardPoints.resultOf(HashMap.of(leftTeamId, "-25", rightTeamId, "125")).get();
    Ranks ranks = Ranks.resultOf(HashMap.of(player1, 1, player2, 2, player3, 3, player4, 4)).get();
    Tichus tichus = Tichus.resultOf(HashMap.of(player1, Tichu.NONE, player2, Tichu.NONE, player3, Tichu.NONE, player4, Tichu.NONE)).get();

    // when:
    var errorOrRound = Round.resultOf(roundNumber, cardPoints, ranks, tichus);

    // then:
    errorOrRound.peekLeft(System.out::println);
    assertThatNoException().isThrownBy(errorOrRound::get);
    Round round = errorOrRound.get();
    assertThat(round.roundNumber()).isEqualTo(roundNumber);
    assertThat(round.cardPoints()).isEqualTo(cardPoints);
    assertThat(round.ranks()).isEqualTo(ranks);
    assertThat(round.tichus()).isEqualTo(tichus);
  }

  @Test
  @DisplayName("The [resultOf] illegal round number and legal values [CardPoints, Ranks, Tichus] is the validation error [MUST_NOT_BE_NULL]")
  void nullRoundNumber_resultOf_expectedError() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    final var leftTeamId = Id.next().value().toString();
    final var rightTeamId = Id.next().value().toString();
    CardPoints cardPoints = CardPoints.resultOf(HashMap.of(leftTeamId.toString(), "-25", rightTeamId.toString(), "125")).get();
    Ranks ranks = Ranks.resultOf(HashMap.of(player1, 1, player2, 2, player3, 3, player4, 4)).get();
    Tichus tichus = Tichus.resultOf(HashMap.of(player1, Tichu.NONE, player2, Tichu.NONE, player3, Tichu.NONE, player4, Tichu.NONE)).get();
    RoundNumber illegalRoundNumber = null;

    ValidationError mustNotBeNullError = RoundValidationErrors.MUST_NOT_BE_NULL.get();

    // when:
    var errorOrRound = Round.resultOf(illegalRoundNumber, cardPoints, ranks, tichus);

    // then:
    assertThatThrownBy(errorOrRound::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRound.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  @DisplayName("The [resultOf] illegal card points and legal values [RoundNumber, Ranks, Tichus] is the validation error [MUST_NOT_BE_NULL]")
  void nullCardPoints_resultOf_expectedError() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    RoundNumber nullRoundNumber = RoundNumber.resultOf(1).get();
    Ranks ranks = Ranks.resultOf(HashMap.of(player1, 1, player2, 2, player3, 3, player4, 4)).get();
    Tichus tichus = Tichus.resultOf(HashMap.of(player1, Tichu.NONE, player2, Tichu.NONE, player3, Tichu.NONE, player4, Tichu.NONE)).get();
    CardPoints illegalCardPoints = null;

    ValidationError mustNotBeNullError = RoundValidationErrors.MUST_NOT_BE_NULL.get();

    // when:
    var errorOrRound = Round.resultOf(nullRoundNumber, illegalCardPoints, ranks, tichus);

    // then:
    assertThatThrownBy(errorOrRound::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRound.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  @DisplayName("The [resultOf] illegal ranks and legal values [CardPoints, Ranks, Tichus] is the validation error [MUST_NOT_BE_NULL]")
  void nullRanks_resultOf_expectedError() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    final var leftTeamId = Id.next().value().toString();
    final var rightTeamId = Id.next().value().toString();
    RoundNumber roundNumber = RoundNumber.resultOf(1).get();
    CardPoints cardPoints = CardPoints.resultOf(HashMap.of(leftTeamId, "-25", rightTeamId, "125")).get();
    Tichus tichus = Tichus.resultOf(HashMap.of(player1, Tichu.NONE, player2, Tichu.NONE, player3, Tichu.NONE, player4, Tichu.NONE)).get();
    Ranks nullRanks = null;

    ValidationError mustNotBeNullError = RoundValidationErrors.MUST_NOT_BE_NULL.get();

    // when:
    var errorOrRound = Round.resultOf(roundNumber, cardPoints, nullRanks, tichus);

    // then:
    assertThatThrownBy(errorOrRound::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRound.getLeft();
    assertThat(errors).hasSize(1);
    assertThat(errors).contains(mustNotBeNullError);
  }

  @Test
  @DisplayName("The [resultOf] illegal tichus and legal values [CardPoints, Ranks, Tichus] is the validation error [MUST_NOT_BE_NULL]")
  void nullTichus_resultOf_expectedError() {
    // given:
    Id player1 = Id.next();
    Id player2 = Id.next();
    Id player3 = Id.next();
    Id player4 = Id.next();
    final var leftTeamId = Id.next().value().toString();
    final var rightTeamId = Id.next().value().toString();
    RoundNumber roundNumber = RoundNumber.resultOf(1).get();
    CardPoints cardPoints = CardPoints.resultOf(HashMap.of(leftTeamId, "-25", rightTeamId, "125")).get();
    Ranks ranks = Ranks.resultOf(HashMap.of(player1, 1, player2, 2, player3, 3, player4, 4)).get();
    Tichus nullTichus = null;

    ValidationError mustNotBeNullError = RoundValidationErrors.MUST_NOT_BE_NULL.get();

    // when:
    var errorOrRound = Round.resultOf(roundNumber, cardPoints, ranks, nullTichus);

    // then:
    assertThatThrownBy(errorOrRound::get).isInstanceOf(NoSuchElementException.class);
    Seq<ValidationError> errors = errorOrRound.getLeft();
    assertThat(errors).contains(mustNotBeNullError);
  }
}

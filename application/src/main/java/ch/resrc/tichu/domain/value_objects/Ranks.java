package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.domain.DomainPrimitive;
import java.util.List;

public class Ranks extends DomainPrimitive<Ranks, List<Id>> implements StringValueObject {

  private final Id firstPlayer;
  private final Id secondPlayer;
  private final Id thirdPlayer;
  private final Id fourthPlayer;

  private Ranks(Id firstPlayer, Id secondPlayer, Id thirdPlayer, Id fourthPlayer) {
    this.firstPlayer = firstPlayer;
    this.secondPlayer = secondPlayer;
    this.thirdPlayer = thirdPlayer;
    this.fourthPlayer = fourthPlayer;
  }

  private Ranks(Ranks other) {
    firstPlayer = other.firstPlayer;
    secondPlayer = other.secondPlayer;
    thirdPlayer = other.thirdPlayer;
    fourthPlayer = other.fourthPlayer;
  }

  public static Ranks of(Id firstPlayer, Id secondPlayer, Id thirdPlayer, Id fourthPlayer) {
    return new Ranks(firstPlayer, secondPlayer, thirdPlayer, fourthPlayer);
  }

  public static Ranks first(Id firstPlayer) {
    return new Ranks(firstPlayer, null, null, null);
  }

  public static Ranks second(Ranks existing, Id secondPlayer) {
    return new Ranks(existing.firstPlayer, secondPlayer, null, null);
  }

  public static Ranks third(Ranks existing, Id thirdPlayer) {
    return new Ranks(existing.firstPlayer, existing.secondPlayer, thirdPlayer, null);
  }

  public static Ranks fourth(Ranks existing, Id fourthPlayer) {
    return new Ranks(existing.firstPlayer, existing.secondPlayer, existing.thirdPlayer, fourthPlayer);
  }

  @Override
  protected List<Id> getPrimitiveValue() {
    if (firstPlayer == null) {
      return List.of();
    } else if (secondPlayer == null) {
      return List.of(firstPlayer);
    } else if (thirdPlayer == null) {
      return List.of(firstPlayer, secondPlayer);
    } else if (fourthPlayer == null) {
      return List.of(firstPlayer, secondPlayer, thirdPlayer);
    } else {
      return List.of(firstPlayer, secondPlayer, thirdPlayer, fourthPlayer);
    }
  }

  public Id firstPlayer() {
    return firstPlayer;
  }

  public Id secondPlayer() {
    return secondPlayer;
  }

  public Id thirdPlayer() {
    return thirdPlayer;
  }

  public Id fourthPlayer() {
    return fourthPlayer;
  }
}

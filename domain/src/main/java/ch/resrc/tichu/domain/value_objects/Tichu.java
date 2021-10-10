package ch.resrc.tichu.domain.value_objects;

public enum Tichu {
  NONE(-1),
  TICHU_CALLED(0),
  TICHU_SUCCEEDED(100),
  TICHU_FAILED(-100),
  GRAND_TICHU_CALLED(1),
  GRAND_TICHU_SUCCEEDED(200),
  GRAND_TICHU_FAILED(-200),
  UNKNOWN(Integer.MAX_VALUE);

  private final int value;

  Tichu(int value) {
    this.value = value;
  }

  public static Tichu of(int value) {
    return switch (value) {
      case -1 -> NONE;
      case 0 -> TICHU_CALLED;
      case 100 -> TICHU_SUCCEEDED;
      case -100 -> TICHU_FAILED;
      case 1 -> GRAND_TICHU_CALLED;
      case 200 -> GRAND_TICHU_SUCCEEDED;
      case -200 -> GRAND_TICHU_FAILED;
      default -> UNKNOWN;
    };
  }

  public int value() {
    return value;
  }
}

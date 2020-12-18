export enum Tichu {
  NONE = -1,
  TICHU_CALLED = 0,
  TICHU_SUCCEEDED = 100,
  TICHU_FAILED = -100,
  GRAND_TICHU_CALLED = 1,
  GRAND_TICHU_SUCCEEDED = 200,
  GRAND_TICHU_FAILED = -200,
}

export function tichuOf(value: number): Tichu {
  if (value === 0) {
    return Tichu.TICHU_CALLED;
  } else if (value === 1) {
    return Tichu.GRAND_TICHU_CALLED;
  } else if (value === 100) {
    return Tichu.TICHU_SUCCEEDED;
  } else if (value === -100) {
    return Tichu.TICHU_FAILED;
  } else if (value === 200) {
    return Tichu.GRAND_TICHU_SUCCEEDED;
  } else if (value === -200) {
    return Tichu.GRAND_TICHU_FAILED;
  } else {
    return Tichu.NONE;
  }
}

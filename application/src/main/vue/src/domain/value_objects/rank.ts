export enum Rank {
  NONE = 0,
  FIRST = 1,
  SECOND = 2,
  THIRD = 3,
  FOURTH = 4,
}

export function rankOf(ordinal: number): Rank {
  if (ordinal === 1) {
    return Rank.FIRST;
  } else if (ordinal === 2) {
    return Rank.SECOND;
  } else if (ordinal === 3) {
    return Rank.THIRD;
  } else if (ordinal === 4) {
    return Rank.FOURTH;
  } else {
    return Rank.NONE;
  }
}

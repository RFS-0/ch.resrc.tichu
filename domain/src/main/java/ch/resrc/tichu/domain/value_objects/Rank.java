package ch.resrc.tichu.domain.value_objects;

import java.util.*;

public enum Rank {
    NONE(0),
    FIRST(1),
    SECOND(2),
    THIRD(3),
    FOURTH(4);

    private final int value;

    Rank(int value) {
        this.value = value;
    }

    public static Rank of(int value) {
        return Arrays.stream(Rank.values())
                .filter(rank -> rank.value == value)
                .findFirst()
                .orElse(Rank.NONE);
    }

    public int value() {
        return value;
    }
}

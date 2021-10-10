package ch.resrc.tichu.capabilities.functional;

import java.util.*;
import java.util.function.*;

public class Maybe {

    public static <T, U> Function<T, U> surely(Function<T, Optional<U>> f) {

        return f.andThen(Optional::orElseThrow);
    }

    public static <T, U, V> BiFunction<T, U, V> surely(BiFunction<T, U, Optional<V>> f) {

        return f.andThen(Optional::orElseThrow);
    }

    public static <T, U> Function<Optional<T>, Optional<U>> opt(Function<T, U> f) {

        return (Optional<T> ot) -> ot.map(f);
    }
}

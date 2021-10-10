package ch.resrc.tichu.capabilities.functional;

import io.vavr.*;

import java.util.function.*;

public class Memoized {

    public static <T, U> Function<T, U> memoized(Function<T, U> f) {

        return Function1.of(f::apply).memoized();
    }
}

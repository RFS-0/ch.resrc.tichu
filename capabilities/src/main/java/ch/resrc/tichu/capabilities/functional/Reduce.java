package ch.resrc.tichu.capabilities.functional;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Reduce {

    public static <T, X> X reduce(Collection<T> elements, X identity, BiFunction<X, T, X> accumulator) {

        return reduce(elements.stream(), identity, accumulator);
    }

    public static <T, X> X reduce(Stream<T> elements, X x, BiFunction<X, T, X> accumulator) {

        return elements.reduce(x, accumulator, (x1, x2) -> x2);
    }

    public static <T, X> BiFunction<X, T, X> methodAccumulator(BiFunction<T, X, X> f) {

        return (X x, T t) -> f.apply(t, x);
    }
}

package ch.resrc.tichu.capabilities.functional;

import java.util.function.*;

public class NullSafe {

    public static <T, U> Function<T, U> nullSafe(Function<T, U> f) {

        return (T x) -> x != null ? f.apply(x) : null;
    }

    public static <T, U> U nullSafe(T value, Function<T, U> f) {

        return nullSafe(f).apply(value);
    }

    public static <T> String nullSafeToString(T obj) {

        return nullSafe(obj, Object::toString);
    }
}

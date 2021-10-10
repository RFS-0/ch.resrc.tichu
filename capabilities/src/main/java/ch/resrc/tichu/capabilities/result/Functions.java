package ch.resrc.tichu.capabilities.result;

import java.util.*;
import java.util.function.*;

public class Functions {

    public static <T, E> Consumer<Result<T, E>> ifSuccess(Consumer<T> effect) {

        return (Result<T, E> result) -> result.effect(effect);
    }

    public static <T, E> Consumer<Result<T, E>> ifSuccess(Runnable effect) {

        return (Result<T, E> result) -> result.effect(effect);
    }

    public static <T, E> Consumer<Result<T, E>> ifFailure(Consumer<List<E>> effect) {

        return (Result<T, E> result) -> result.failureEffect(effect);
    }
}

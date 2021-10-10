package ch.resrc.tichu.capabilities.doto;

import ch.resrc.tichu.capabilities.result.*;

import java.util.*;
import java.util.function.*;

import static ch.resrc.tichu.capabilities.functional.Reduce.*;

public class DoTo<T, E> {

    private Result<T, E> result;
    private final Class<E> errorType;

    protected DoTo(Result<T, ? extends E> result, Class<E> errorType) {

        this.result = cast(result);
        this.errorType = errorType;
    }

    protected DoTo(DoTo<T, E> other) {

        this.result = other.result;
        this.errorType = other.errorType;
    }

    protected DoTo<T, E> copy() {

        return new DoTo<>(this);
    }

    public static <T> DoTo<T, Object> of(T value) {

        return new DoTo<>(Result.success(value), Object.class);
    }

    public static <T, E> DoTo<T, E> of(T value, Class<E> errorType) {

        return new DoTo<>(Result.success(value), errorType);
    }

    public static <T, E> DoTo<T, E> of(Result<T, ? extends E> result, Class<E> errorType) {

        return new DoTo<>(result, errorType);
    }

    public <EE> DoTo<T, E> apply(Function<T, Result<T, EE>> f) {

        var theCopy = copy();
        theCopy.result = this.result.flatMap(f, errorType);
        return theCopy;
    }

    public <U, EE extends E> DoTo<T, E> applyAll(Collection<U> elements, BiFunction<T, U, Result<T, EE>> f) {

        return reduce(elements, copy(), (d, u) -> d.apply(t -> f.apply(t, u)));
    }

    private static <T, E> Result<T, E> cast(Result<T, ? extends E> result) {

        if (result.isSuccess()) {
            return Result.success(result.value());
        } else {
            return Result.failure(result.errors());
        }
    }

    public Result<T, E> result() {

        return this.result;
    }

    public T value() throws NoSuchElementException {

        return this.result().value();
    }
}

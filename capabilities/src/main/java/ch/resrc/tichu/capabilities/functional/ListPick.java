package ch.resrc.tichu.capabilities.functional;

import java.util.*;
import java.util.function.*;

public class ListPick {

    public static <T> Function<List<T>, T> first() {

        return ListPick.<T>firstOrNull()
                .andThen(Optional::ofNullable)
                .andThen(x -> x.orElseThrow(
                        () -> new NoSuchElementException("The list has no first element because it's empty.")
                ));
    }

    public static <T> Function<List<T>, T> firstOrNull() {

        return (List<T> list) -> {
            if (list.isEmpty()) {
                return null;
            }

            return list.get(0);
        };
    }
}

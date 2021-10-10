package ch.resrc.tichu.capabilities.functional;

import java.util.*;
import java.util.function.*;

public class ForEach {

    public static <T, U> Consumer<List<T>> forEach(Consumer<T> effect) {

        return (List<T> list) -> list.forEach(effect);
    }
}

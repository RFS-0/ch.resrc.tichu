package ch.resrc.tichu.capabilities.functional;

import java.util.*;
import java.util.function.*;

import static java.util.stream.Collectors.*;

public class Filter {

    public static <T> List<T> filter(Collection<T> elements, Predicate<? super T> q) {

        return elements.stream().filter(q).collect(toList());
    }
}

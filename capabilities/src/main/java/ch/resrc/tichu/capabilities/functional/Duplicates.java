package ch.resrc.tichu.capabilities.functional;

import java.util.*;
import java.util.function.*;

import static java.util.stream.Collectors.*;

public class Duplicates {

    public static <T> List<T> duplicatesIn(Collection<T> elements) {

        return elements.stream()
                       .collect(groupingBy(Function.identity()))
                       .entrySet().stream()
                       .filter(it -> it.getValue().size() > 1)
                       .map(Map.Entry::getKey)
                       .collect(toList());
    }
}

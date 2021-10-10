package ch.resrc.tichu.capabilities.functional;

import java.util.*;

import static java.util.stream.Collectors.*;

public class Sorted {

    public static <T> List<T> sorted(Collection<T> elements, Comparator<T> sorting) {

        return elements.stream().sorted(sorting).collect(toList());
    }
}

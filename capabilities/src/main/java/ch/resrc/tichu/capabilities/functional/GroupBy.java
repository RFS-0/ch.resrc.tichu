package ch.resrc.tichu.capabilities.functional;

import java.util.*;
import java.util.function.*;

import static java.util.stream.Collectors.*;

public class GroupBy {

    public static <U, K, V> Map<K, List<V>> groupedBy(Function<U, K> fk, Function<U, V> fv, List<U> items) {

        return items.stream().collect(groupingBy(fk, mapping(fv, toList())));

    }
}

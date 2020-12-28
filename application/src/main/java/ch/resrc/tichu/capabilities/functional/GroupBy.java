package ch.resrc.tichu.capabilities.functional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GroupBy {

  public static <U, K, V> Map<K, List<V>> groupedBy(Function<U, K> fk, Function<U, V> fv, List<U> items) {
    return items.stream().collect(groupingBy(fk, mapping(fv, toList())));
  }
}

package ch.resrc.tichu.capabilities.functional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

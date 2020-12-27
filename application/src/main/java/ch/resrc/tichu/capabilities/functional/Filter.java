package ch.resrc.tichu.capabilities.functional;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class Filter {

  public static <T> List<T> filter(Collection<T> elements, Predicate<? super T> q) {
    return elements.stream().filter(q).collect(toList());
  }
}

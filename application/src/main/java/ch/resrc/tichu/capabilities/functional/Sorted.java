package ch.resrc.tichu.capabilities.functional;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class Sorted {

  public static <T> List<T> sorted(Collection<T> elements, Comparator<T> sorting) {
    return elements.stream().sorted(sorting).collect(toList());
  }
}

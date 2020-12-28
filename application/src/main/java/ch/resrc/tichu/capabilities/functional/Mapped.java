package ch.resrc.tichu.capabilities.functional;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Mapped {

  public static <C extends Collection<T>, T, U> Function<C, List<U>> map(Function<? super T, U> f) {
    return (C elements) -> elements.stream().map(f).collect(toList());
  }

  public static <T, U> Function<List<T>, List<U>> flatMap(Function<T, List<U>> f) {
    return (List<T> list) -> list.stream().flatMap((T t) -> f.apply(t).stream()).collect(toList());
  }

  public static <C extends Collection<T>, T, U> List<U> map(Function<T, U> f, C elements) {
    return map(f).apply(elements);
  }

  public static <T, U> List<U> mapIndexed(Collection<T> elements, BiFunction<? super T, Integer, U> mapping) {
    return io.vavr.collection.Stream.ofAll(elements)
      .zipWithIndex(mapping)
      .collect(toList());
  }

  public static <K, V, U> Function<Map.Entry<K, V>, U> entryMapped(BiFunction<K, V, U> f) {
    return (Map.Entry<K, V> entry) -> f.apply(entry.getKey(), entry.getValue());
  }

  public static <K, V, U> List<U> map(BiFunction<K, V, U> f, Map<K, V> m) {
    return map(entryMapped(f), m.entrySet());
  }
}

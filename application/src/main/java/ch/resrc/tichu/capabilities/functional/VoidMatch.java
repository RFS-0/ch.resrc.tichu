package ch.resrc.tichu.capabilities.functional;

import static io.vavr.API.$;

import io.vavr.API;
import java.util.function.Consumer;

public class VoidMatch {

  public static <T> API.Match.Case<T, Void> Case(API.Match.Pattern0<T> pattern, Consumer<T> f) {
    return API.Case(pattern, (x) -> {
      f.accept(x);
      return null;
    });
  }

  public static <T> API.Match.Case<T, Void> DefaultIgnore() {
    return API.Case($(), (x) -> null);
  }

}

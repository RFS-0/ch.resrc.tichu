package ch.resrc.tichu.capabilities.functional;

import static io.vavr.API.$;

import io.vavr.API;
import java.util.function.Supplier;

public class MatchDefaults {

  public static <T, R> API.Match.Case<T, R> DefaultThrow(Supplier<RuntimeException> thrown) {
    return API.Case($(), (x) -> {
      throw thrown.get();
    });
  }
}

package ch.resrc.tichu.capabilities.functional;

import io.vavr.*;

import java.util.function.*;

import static io.vavr.API.*;

public class VoidMatch {

    public static <T> API.Match.Case<T, Void> Case(API.Match.Pattern0<T> pattern, Consumer<T> f) {
        return API.Case(pattern, (x) -> {f.accept(x); return null;});
    }

    public static <T> API.Match.Case<T, Void> DefaultIgnore() {
        return API.Case($(), (x) -> null);
    }

}

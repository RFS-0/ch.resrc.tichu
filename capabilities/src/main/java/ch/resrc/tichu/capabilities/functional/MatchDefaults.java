package ch.resrc.tichu.capabilities.functional;

import io.vavr.*;

import java.util.function.*;

import static io.vavr.API.*;

public class MatchDefaults {

    public static <T, R> API.Match.Case<T, R> DefaultThrow(Supplier<RuntimeException> thrown) {
        return API.Case($(), (x) -> {throw thrown.get();});
    }
}

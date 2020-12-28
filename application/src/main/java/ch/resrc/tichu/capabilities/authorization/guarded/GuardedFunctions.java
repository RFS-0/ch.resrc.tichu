package ch.resrc.tichu.capabilities.authorization.guarded;

import ch.resrc.tichu.capabilities.changelog.ChangeLogging;
import ch.resrc.tichu.capabilities.result.Result;
import java.util.function.Function;

/**
 * Convenience functions that help to have less verbose calling signatures in certain contexts.
 */
public class GuardedFunctions {

  public static <T extends ChangeLogging, E> GuardedMutation<T, E>
  guarded(Function<T, Result<T, E>> f) {
    return (Guarded<T> g) -> g.mutated(f);
  }
}

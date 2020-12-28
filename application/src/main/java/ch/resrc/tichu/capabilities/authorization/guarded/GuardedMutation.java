package ch.resrc.tichu.capabilities.authorization.guarded;

import ch.resrc.tichu.capabilities.changelog.ChangeLogging;
import ch.resrc.tichu.capabilities.result.Result;
import java.util.function.Function;

public interface GuardedMutation<T extends ChangeLogging, E> extends Function<Guarded<T>, Result<Guarded<T>, E>> {

}

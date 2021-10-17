package ch.resrc.tichu.use_cases.support.outbound_ports.authorization;

import ch.resrc.tichu.capabilities.events.*;

/**
 * Signals that some use case was invoked. Does not mean that the use case has completed.
 *
 * @param <UC> the type of the invoked use case.
 */
public class UseCaseInvoked<UC> extends Event {

    private final Class<UC> invokedUseCase;

    public UseCaseInvoked(Class<UC> invokedUseCase) {

        this.invokedUseCase = invokedUseCase;
    }

    public static <T> UseCaseInvoked<T> of(Class<T> invokedUseCase) {
        return new UseCaseInvoked<>(invokedUseCase);
    }

    public Class<UC> invokedUseCase() { return invokedUseCase; }

}

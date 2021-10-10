package ch.resrc.tichu.capabilities.changelog;

/**
 * Signals that the {@link ChangeLog} that throws this exception is not the continuation
 * of another {@code ChangeLog} but is required to be a continuation in the context of the
 * throwing operation.
 *
 * <p>A {@code ChangeLog} is the continuation of another {@code ChangeLog}, if it contains a version
 * with the same ID as the last version (head version) of the other change log.</p>
 */
public class NotAContinuation extends IllegalStateException {

    public NotAContinuation(String message) {
        this(message, null);
    }

    public NotAContinuation(String message, Throwable cause) {
        super(message, cause);
    }
}

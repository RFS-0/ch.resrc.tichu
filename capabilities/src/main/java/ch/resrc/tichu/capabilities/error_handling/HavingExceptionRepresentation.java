package ch.resrc.tichu.capabilities.error_handling;

/**
 * Implementations have a representation as an exception.
 */
public interface HavingExceptionRepresentation {

    RuntimeException asException();
}

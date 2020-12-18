package ch.resrc.tichu.capabilities.errorhandling;

/**
 * Implementations have a representation as an exception.
 */
public interface HavingExceptionRepresentation {

  RuntimeException asException();
}

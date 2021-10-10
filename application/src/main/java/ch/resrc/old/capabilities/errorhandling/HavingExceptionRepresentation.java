package ch.resrc.old.capabilities.errorhandling;

/**
 * Implementations have a representation as an exception.
 */
public interface HavingExceptionRepresentation {

  RuntimeException asException();
}

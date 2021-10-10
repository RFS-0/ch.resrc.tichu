package ch.resrc.tichu.capabilities.error_handling;

/**
 * Signals a business error.
 * A business error can be the result of invalid client input, unacceptable client intent
 * or some anticipated condition in the business domain that needs the client's attention.
 */
public interface BusinessError {

    ProblemDetected asException();
}

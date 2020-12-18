package ch.resrc.tichu.capabilities.errorhandling;

import ch.resrc.tichu.capabilities.aspects.Aspect;
import ch.resrc.tichu.capabilities.errorhandling.faults.ClientFault;
import ch.resrc.tichu.capabilities.errorhandling.faults.CommunicationFault;
import ch.resrc.tichu.capabilities.presentation.ErrorPresenter;
import org.slf4j.Logger;

import java.util.function.Consumer;

public class ErrorAudit {

  public static Aspect reportProblemsTo(Logger logger) {
    return (Runnable operation) -> Try.ofVoid(operation)
      .onFailure(reportProblemTo(logger))
      .onFailureThrow();
  }

  public static Consumer<RuntimeException> reportProblemTo(Logger logger) {
    return (RuntimeException bad) -> logException(bad, logger);
  }

  public static Aspect swallowExceptionsAndPresentWith(ErrorPresenter presenter) {
    return (Runnable operation) -> Try.ofVoid(operation)
      .onFailure(presenter::presentSystemFailure);
  }

  private static void logException(Throwable bad, Logger log) {
    String typeOfBad = bad.getClass().getSimpleName();

    if (bad instanceof ClientFault) {
      log.info("{} - {}", typeOfBad, bad.getMessage());
    } else if (bad instanceof CommunicationFault) {
      log.warn("{}", typeOfBad, bad);
    } else {
      log.error("{}", typeOfBad, bad);
    }
  }
}

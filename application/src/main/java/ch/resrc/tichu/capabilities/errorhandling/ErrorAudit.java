package ch.resrc.tichu.capabilities.errorhandling;

import ch.resrc.tichu.capabilities.aspects.Aspect;
import ch.resrc.tichu.capabilities.errorhandling.faults.ClientFault;
import ch.resrc.tichu.capabilities.errorhandling.faults.CommunicationFault;
import ch.resrc.tichu.capabilities.presentation.ErrorPresenter;
import ch.resrc.tichu.eventbus.EventBus;
import ch.resrc.tichu.eventbus.EventBusAddresses;
import ch.resrc.tichu.usecases.events.UseCaseProblemDetected;
import java.util.function.Consumer;
import org.slf4j.Logger;

public class ErrorAudit {

  public static Aspect reportProblemsTo(EventBus eventBus) {
    return (Runnable operation) -> Try.ofVoid(operation)
      .onFailure(bad -> eventBus.publish(EventBusAddresses.ERRORS.value(), UseCaseProblemDetected.of(bad)))
      .onFailureThrow();
  }

  public static Aspect reportProblemsTo(Logger logger, EventBus eventBus) {
    return (Runnable operation) -> Try.ofVoid(operation)
      .onFailure(reportProblemTo(logger, eventBus))
      .onFailureThrow();
  }

  public static Consumer<RuntimeException> reportProblemTo(Logger logger, EventBus eventBus) {
    return (RuntimeException bad) -> {
      logException(bad, logger);
      eventBus.publish(EventBusAddresses.ERRORS.value(), UseCaseProblemDetected.of(bad));
    };
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

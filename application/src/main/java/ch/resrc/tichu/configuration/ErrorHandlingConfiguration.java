package ch.resrc.tichu.configuration;

import ch.resrc.tichu.capabilities.errorhandling.ProblemCatalogue;
import ch.resrc.tichu.capabilities.errorhandling.TichuProblemCatalog;

import javax.enterprise.context.ApplicationScoped;

public class ErrorHandlingConfiguration {

  @ApplicationScoped
  public ProblemCatalogue problemCatalogue() {
    return new TichuProblemCatalog();
  }
}

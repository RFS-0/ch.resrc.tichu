package ch.resrc.old.configuration;

import ch.resrc.old.capabilities.errorhandling.*;

import javax.enterprise.context.*;

public class ErrorHandlingConfiguration {

  @ApplicationScoped
  public ProblemCatalogue problemCatalogue() {
    return new TichuProblemCatalog();
  }
}

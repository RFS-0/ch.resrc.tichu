package ch.resrc.old.configuration;

import ch.resrc.old.capabilities.errorhandling.*;
import ch.resrc.old.capabilities.json.*;

import javax.enterprise.context.*;

public final class RestConfiguration {

  private final ProblemCatalogue problemCatalogue;

  public RestConfiguration(ProblemCatalogue problemCatalogue) {
    this.problemCatalogue = problemCatalogue;
  }

  @ApplicationScoped
  Json json() {
    return JsonCapability.json();
  }
}

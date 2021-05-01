package ch.resrc.tichu.configuration;

import ch.resrc.tichu.capabilities.errorhandling.ProblemCatalogue;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.json.JsonCapability;

import javax.enterprise.context.ApplicationScoped;

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

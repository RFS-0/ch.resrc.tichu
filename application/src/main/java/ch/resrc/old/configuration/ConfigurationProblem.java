package ch.resrc.old.configuration;

import ch.resrc.old.capabilities.errorhandling.*;

public enum ConfigurationProblem implements Problem {

  INVALID_ENVIRONMENT("Configuration failed", "Configuration failed since an invalid environment was specified."),
  INVALID_REPOSITORY("Configuration failed", "Configuration failed since an invalid repository was specified."),
  INVALID_PROFILE("Configuration failed", "Configuration failed since an invalid profile was specified.");

  private final String title;
  private final String details;

  ConfigurationProblem(String title, String details) {
    this.title = title;
    this.details = details;
  }

  @Override
  public String detailsTemplate() {
    return details;
  }

  @Override
  public String title() {
    return title;
  }
}

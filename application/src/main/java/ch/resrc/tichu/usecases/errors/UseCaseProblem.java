package ch.resrc.tichu.usecases.errors;

import ch.resrc.tichu.capabilities.errorhandling.Problem;

public enum UseCaseProblem implements Problem {

  INVALID_INPUT_DETECTED("Invalid input", "${validationMessage}"),

  EXCEPTION_LEAKED("Use case leaked exception", "Use cases must swallow all their exceptions and invoke the appropriate presenters."),

  RESOURCE_NOT_FOUND("Resource not found", "<${method} ${path}> is not supported."),

  METHOD_NOT_ALLOWED("Method not allowed", "<${method}> is not a valid method for the url. Try ${supportedMethods}."),

  UNAUTHENTICATED("Authentication failed", "You could not be authenticated.");

  private final String title;
  private final String detailsTemplate;


  UseCaseProblem(String title, String detailsTemplate) {
    this.title = title;
    this.detailsTemplate = detailsTemplate;
  }

  @Override
  public String title() {
    return title;
  }

  @Override
  public String detailsTemplate() {
    return detailsTemplate;
  }
}

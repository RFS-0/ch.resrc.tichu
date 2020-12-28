package ch.resrc.tichu.capabilities.errorhandling;

public enum DomainProblem implements Problem {

  INVALID_PROPERTY_MUTATION("${validationErrors}"),

  MANDATORY_VALUE_MISSING("${message}"),

  INVARIANT_VIOLATED("${message}");

  public static DomainProblem[] all() {
    return DomainProblem.values();
  }

  private final String details;

  DomainProblem(String details) {
    this.details = details;
  }

  @Override
  public String title() {
    return "Domain rule violated";
  }

  @Override
  public String detailsTemplate() {
    return details;
  }
}

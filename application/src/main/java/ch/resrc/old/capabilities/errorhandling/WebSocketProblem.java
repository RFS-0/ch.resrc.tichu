package ch.resrc.old.capabilities.errorhandling;

public enum WebSocketProblem implements Problem {

  USE_CASE_RESULT_MISSING("Use case result missing.", "Use case failed to present any result.");

  private final String title;
  private final String detailsTemplate;


  WebSocketProblem(String title, String detailsTemplate) {
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

package ch.resrc.tichu.capabilities.errorhandling;

public enum PersistenceProblem implements Problem {

  INSERT_FAILED("Insert failed", "Insertion failed due to some unknown error"),
  READ_FAILED("Read failed", "Read failed due to some unknown problem"),
  UPDATE_FAILED("Update failed", "Update failed due to some unknown problem"),
  DELETE_FAILED("Delete failed", "Delete failed due to some unknown problem"),
  SHUTDOWN_FAILED("Shutdown failed", "Shutdown failed due to some unknown problem");

  private final String title;
  private final String details;

  PersistenceProblem(String title, String details) {
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

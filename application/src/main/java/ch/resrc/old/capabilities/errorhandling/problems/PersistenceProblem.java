package ch.resrc.old.capabilities.errorhandling.problems;

public class PersistenceProblem extends Problem {

  public static PersistenceProblem persistFailed() {
    return (PersistenceProblem) aProblem()
      .withTitle("Persist failed")
      .withDetails("Persist failed due to some unknown error")
      .withCausedBy(null)
      .build();
  }

  public static PersistenceProblem readFailed() {
    return (PersistenceProblem) aProblem()
      .withTitle("Read failed")
      .withDetails("Read failed due to some unknown error")
      .withCausedBy(null)
      .build();
  }

  public static PersistenceProblem updateFailed() {
    return (PersistenceProblem) aProblem()
      .withTitle("Update failed")
      .withDetails("Update failed due to some unknown problem")
      .withCausedBy(null)
      .build();
  }

  public static PersistenceProblem deleteFailed() {
    return (PersistenceProblem) aProblem()
      .withTitle("Delete failed")
      .withDetails("Delete failed due to some unknown problem")
      .withCausedBy(null)
      .build();
  }

  public static PersistenceProblem shutdownFailed() {
    return (PersistenceProblem) aProblem()
      .withTitle("Shutdown failed")
      .withDetails("Shutdown failed due to some unknown problem")
      .withCausedBy(null)
      .build();
  }
}

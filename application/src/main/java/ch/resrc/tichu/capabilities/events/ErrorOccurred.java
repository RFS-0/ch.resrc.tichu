package ch.resrc.tichu.capabilities.events;

public class ErrorOccurred extends Event {

  private final RuntimeException error;

  private ErrorOccurred(RuntimeException error) {
    this.error = error;
  }

  public static ErrorOccurred of(RuntimeException error) {
    return new ErrorOccurred(error);
  }

  public RuntimeException asException() {
    return error;
  }
}

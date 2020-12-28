package ch.resrc.tichu.capabilities.aspects;

public class CommonAspects {

  public static Aspect finallyOnExit(Runnable exitAction) {
    return (op) -> {
      try {
        op.run();
      } finally {
        exitAction.run();
      }
    };
  }

  public static Aspect onSuccessfulExit(Runnable exitAction) {
    return (op) -> {
      op.run();
      exitAction.run();
    };
  }
}

package ch.resrc.tichu.ports.outbound.authorization;


import ch.resrc.tichu.capabilities.errorhandling.HavingDiagnosis;
import ch.resrc.tichu.capabilities.events.Event;

public class MutationFailed extends Event {

  private final HavingDiagnosis error;

  private MutationFailed(HavingDiagnosis error) {
    this.error = error;
  }

  public static MutationFailed of(HavingDiagnosis error) {
    return new MutationFailed(error);
  }

  public HavingDiagnosis error() {
    return error;
  }
}

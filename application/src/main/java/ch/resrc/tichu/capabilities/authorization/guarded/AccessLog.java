package ch.resrc.tichu.capabilities.authorization.guarded;

import ch.resrc.tichu.capabilities.changelog.ChangeLog;
import ch.resrc.tichu.capabilities.events.Events;

class AccessLog {

  private final ChangeLog baseline;
  private final ChangeLog progress;

  public Events accessEvents() {
    ChangeLog changes = progress.continuationOf(baseline);
    return Events.of(changes.allEvents());
  }

  private AccessLog(ChangeLog baseline, ChangeLog progress) {
    this.baseline = baseline;
    this.progress = progress;
  }

  static AccessLog ofNew(ChangeLog initialChanges) {
    return new AccessLog(ChangeLog.empty(), initialChanges);
  }

  static AccessLog ofContinued(ChangeLog baseline) {
    return new AccessLog(baseline, baseline);
  }

  AccessLog continued(ChangeLog continuation) {
    return new AccessLog(this.baseline, continuation);
  }

  AccessLog baselined() {
    return AccessLog.ofContinued(this.progress);
  }
}

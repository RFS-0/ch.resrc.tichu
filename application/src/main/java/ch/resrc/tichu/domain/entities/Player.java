package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.capabilities.changelog.ChangeLog;
import ch.resrc.tichu.capabilities.changelog.ChangeLogging;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import ch.resrc.tichu.domain.value_objects.Picture;
import java.time.Instant;

public class Player implements ChangeLogging {

  private Id id;
  private Name name;
  private Picture picture;
  private Instant createdAt;

  private ChangeLog changeLog = ChangeLog.empty();

  ///// Characteristics /////

  @Override
  public ChangeLog changeLog() {
    return changeLog;
  }

}

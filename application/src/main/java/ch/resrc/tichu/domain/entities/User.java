package ch.resrc.tichu.domain.entities;

import ch.resrc.tichu.capabilities.changelog.ChangeLog;
import ch.resrc.tichu.capabilities.changelog.ChangeLogging;
import ch.resrc.tichu.domain.value_objects.Email;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import ch.resrc.tichu.domain.value_objects.Picture;
import java.time.Instant;

public class User implements ChangeLogging {

  private Id id;
  private Email email;
  private Name name;
  private Name givenName;
  private Name familyName;
  private Picture picture;
  private Instant createdAt;

  private ChangeLog changeLog = ChangeLog.empty();

  @Override
  public ChangeLog changeLog() {
    return changeLog;
  }
}

package ch.resrc.tichu.capabilities.changelog;

import ch.resrc.tichu.capabilities.events.Event;
import java.util.Objects;
import java.util.UUID;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Identifies and characterizes a particular change to an entity whose change history is represented by a  {@link ChangeLog}.
 */
public class Version {

  private final VersionId id;
  private final Event event;

  Version(VersionId id, Event event) {
    this.id = Objects.requireNonNull(id);
    this.event = Objects.requireNonNull(event);
  }

  public static VersionId nextId() {
    return VersionId.fromLiteral(UUID.randomUUID().toString());
  }

  public static Version of(VersionId id, Event changeEvent) {
    return new Version(id, changeEvent);
  }

  public VersionId id() {
    return id;
  }

  Event event() {
    return event;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("id", id)
      .append("event", event.getClass().getSimpleName())
      .toString();
  }
}

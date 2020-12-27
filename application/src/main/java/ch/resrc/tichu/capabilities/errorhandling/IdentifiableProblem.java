package ch.resrc.tichu.capabilities.errorhandling;

import java.time.Instant;
import java.util.UUID;

/**
 * Provides information that helps humans to identify and communicate about a particular problem instance.
 */
public interface IdentifiableProblem {

  UUID id();

  Instant occurredOn();
}

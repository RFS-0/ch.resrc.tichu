package ch.resrc.tichu.capabilities.error_handling;

import java.time.*;
import java.util.*;

/**
 * Provides information that helps humans to identify and communicate about a particular problem instance.
 */
public interface IdentifiableProblem {

    UUID id();

    Instant occurredOn();
}

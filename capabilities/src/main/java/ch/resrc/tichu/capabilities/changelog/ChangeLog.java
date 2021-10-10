package ch.resrc.tichu.capabilities.changelog;

import ch.resrc.tichu.capabilities.error_handling.*;
import ch.resrc.tichu.capabilities.events.*;
import ch.resrc.tichu.capabilities.functional.*;
import org.apache.commons.lang3.builder.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static java.util.stream.Collectors.*;

/**
 * Describes a series of changes that were made to an event oriented entity or aggregate.
 * Each change is described by an {@link Event}. Each change gives rise to a {@link Version} that is
 * recorded in this object. A {@code Version} identifies a change with a {@link VersionId}.
 *
 * <p>Is able to determine what changes occurred after some base version. This is useful for replaying changes
 * on objects that need to be synchronized with the aggregate. It is also useful for auditing changes</p>
 */
public class ChangeLog {

    private List<Version> versions;

    private ChangeLog() { versions = List.of(); }

    private ChangeLog(ChangeLog other) { this.versions = other.versions; }

    private ChangeLog copied(Consumer<ChangeLog> modification) {

        ChangeLog theCopy = new ChangeLog(this);
        modification.accept(theCopy);
        return theCopy;
    }

    private ChangeLog invariantsChecked() {

        requireUniqueVersionIds(this.versions);

        return this;
    }

    private static void requireUniqueVersionIds(Collection<Version> versions) {

        List<VersionId> duplicateIds = versions.stream()
                                            .collect(groupingBy(Version::id))
                                            .entrySet().stream()
                                            .filter(x -> x.getValue().size() > 1)
                                            .map(Map.Entry::getKey)
                                            .collect(toList());

        if (!duplicateIds.isEmpty()) {
            throw new IllegalStateException(String.format("Duplicate version-IDs: <%s>", duplicateIds));
        }
    }

    /**
     * Creates a new, change log.
     *
     * @return a new, empty change log
     */
    public static ChangeLog empty() { return new ChangeLog(); }

    /**
     * Creates a change log with the specified version as the base version.
     *
     * @param baseVersion the first item in the change log.
     * @return the new change log
     */
    public static ChangeLog createWithBase(Version baseVersion) {
        return ChangeLog.empty().versionAppended(baseVersion);
    }

    /**
     * Creates a change log with the specified change as the first change in the change log.
     * @param firstChange the first change in the change log
     * @return the new change log
     */
    public static ChangeLog createWithBase(Event firstChange) {
        return ChangeLog.empty().changeAppended(firstChange);
    }

    /**
     * Returns all change events in this change log
     * @return all events in this change log
     */
    public List<Event> allEvents() {

        return versions.stream().map(Version::event).collect(toList());
    }

    /**
     * Appends a change event to the change log
     *
     * @param change an event that describes a change
     * @return a new change log with the change event appended
     */
    public ChangeLog changeAppended(Event change) {

        var version = Version.of(Version.nextId(), change);
        return this.versionAppended(version);
    }

    /**
     * Appends an {@link Version} to the change log. Intended only for persistence adapters that need to restore
     * the change log from some persistent representation.
     *
     * @param version the version to append
     * @return a new change log with the version appended.
     * @throws IllegalStateException if the ID of the appended version collides with the ID of a version that is
     *                               already present in this change log.
     */
    public ChangeLog versionAppended(Version version) throws IllegalStateException {

        return this.copied(theCopy -> theCopy.versions = PersistentCollections.addedTo(theCopy.versions, version))
                   .invariantsChecked();
    }

    /**
     * Merges this stream into the given base stream. Merging means that the items of this stream that follow the head
     * item of the base stream are appended to the base stream. The head item of the base stream is called the continuation
     * point. The continuation point must be present in this stream or otherwise the merge fails, because the continuation
     * point determines, what items get appended to the base stream.
     *
     * @param baseStream the stream to merge into. This stream must have an item with the same ID as the head item of the
     *                   base stream.
     * @return a new stream that represents the merges stream
     * @throws NotAContinuation if this stream does not contain the continuation point of the base stream
     */
    public ChangeLog mergedInto(ChangeLog baseStream) throws NotAContinuation {

        return this.continuationVersionsFor(baseStream)
                   .mapFailure(NotAContinuation.class, x -> new NotAContinuation("Cannot merge change logs. " + x.getMessage(), x))
                   .map(s -> s.reduce(baseStream, ChangeLog::versionAppended, (s1, s2) -> s2))
                   .getOrThrow();
    }

    /**
     * Returns a change log that contains all events of this change log that are the continuation of the given
     * base change log. The continuation events are all the events that follow the continuation point of this change log
     * relative to the base change log. The continuation includes any snapshot that might follow the continuation point.
     *
     * @param base the change log whose continuation should be obtained from this stream.
     * @return a new change log as explained
     * @throws NotAContinuation if this change log is not a continuation of the other change log
     */
    public ChangeLog continuationOf(ChangeLog base) throws NotAContinuation {

        return this.continuationVersionsFor(base)
                   .map(s -> s.reduce(ChangeLog.empty(), ChangeLog::versionAppended, (s1, s2) -> s2))
                   .getOrThrow();
    }

    public ChangeLog changesAfter(VersionId baseVersionId) {

        Version baseVersion = this.findVersionWithId(baseVersionId);

        if (baseVersion == null) {
            throw new NotAContinuation(
                    String.format("This stream does not have a base version with id=<%s>, this=<%s>",
                                  baseVersionId, this));
        }

        return versionsFollowingContinuationPoint(baseVersion)
                .reduce(ChangeLog.empty(), ChangeLog::versionAppended, (s1, s2) -> s2);
    }

    private Try<Stream<Version>> continuationVersionsFor(ChangeLog other) {

        if (other.isEmpty()) {
            return Try.of(this.versions.stream());
        }

        return this.continuationPointOf(other)
                   .map(this::versionsFollowingContinuationPoint);
    }

    private Stream<Version> versionsFollowingContinuationPoint(Version continuationPoint) {

        Predicate<Version> itemIsNotTheContinuationPoint = x -> !x.equals(continuationPoint);

        return versions.stream()
                       .dropWhile(itemIsNotTheContinuationPoint)
                       .filter(itemIsNotTheContinuationPoint);
    }

    private Try<Version> continuationPointOf(ChangeLog other) {

        Supplier<RuntimeException> notAContinuation = () -> new NotAContinuation(
                String.format("This change log is not a continuation of the other change log. base=<%s>, this=<%s>",
                              other, this));

        if (this.isEmpty()) {
            return Try.failure(notAContinuation.get());
        }

        if (other.isEmpty()) {
            return Try.empty();
        }

        Version othersHead = other.head().orElseThrow();

        Version continuationPoint = this.findVersionWithId(othersHead.id());

        if (continuationPoint == null) {
            return Try.failure(notAContinuation.get());
        }

        return Try.of(continuationPoint);
    }


    /**
     * Tells whether this change log contains any events.
     *
     * @return true, if this change log contains events. False otherwise.
     */
    public boolean isEmpty() { return versions.isEmpty(); }

    private Version findVersionWithId(VersionId id) {

        return this.versions.stream()
                            .filter((Version x) -> id.equals(x.id()))
                            .findFirst()
                            .orElse(null);
    }

    /**
     * The version of this change log. The version of the change log is the version of its head version.
     *
     * @return the version of this change log, of null if the change log is empty.
     */
    public VersionId version() {

        return this.head()
                   .map(Version::id)
                   .orElse(null);
    }

    private Optional<Version> head() {

        if (versions.isEmpty()) return Optional.empty();

        return Optional.of(versions.get(versions.size() - 1));
    }

    public String asEventClassNames() {

        return this.allEvents().stream().map(it -> it.getClass().getSimpleName()).collect(toList()).toString();
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("versions", versions)
                .toString();
    }
}

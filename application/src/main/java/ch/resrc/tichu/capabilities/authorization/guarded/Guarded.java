package ch.resrc.tichu.capabilities.authorization.guarded;

import ch.resrc.tichu.capabilities.changelog.ChangeLog;
import ch.resrc.tichu.capabilities.changelog.ChangeLogging;
import ch.resrc.tichu.capabilities.events.Event;
import ch.resrc.tichu.capabilities.events.Events;
import ch.resrc.tichu.capabilities.result.Result;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Restricts changes to a data item so that side effects originating from the changes are only possible after all changes have been
 * authorized with an authorization function.
 * <p>
 * This object enables an authorization system where data mutations are authorized based on what actually happened to the data. Data
 * that is guarded by this object must be able to provide a {@link ChangeLog} that describes all mutations that happened to the data.
 * <p>
 * The guarded data item can be mutated without side effects using this object and a mutation function for the data item. Then, before
 * side effects such as storing are applied, this object forces you to authorize all change events that resulted from all mutations.
 * Because, in order to apply side effects, you have to retrieve the value from this object. The retrieval function refuses to yield
 * the guarded data unless you have properly authorized all data access that has happened so far.
 * <p>
 * After the change events have been authorized, they don't get authorized again.
 * </p>
 *
 * @param <T> the type of the guarded data item
 */
public class Guarded<T extends ChangeLogging> {

  T dataItem;
  AccessLog accessLog;

  private Guarded(T dataItem, AccessLog accessLog) {
    this.dataItem = dataItem;
    this.accessLog = accessLog;
  }

  private Guarded(Guarded<T> other) {
    this.dataItem = other.dataItem;
    this.accessLog = other.accessLog;
  }

  /**
   * Guards a data item whose entire current change log needs to be authorized. This is typically the case for objects that have been
   * newly created by a use case and which has not produced any side effects yet. In particular it has not been stored yet.
   *
   * @param dataItem the value to be guarded
   * @param <T>      the type of the guarded value
   * @return a new instance as explained
   */
  public static <T extends ChangeLogging> Guarded<T> ofNew(T dataItem) {
    return new Guarded<>(dataItem, AccessLog.ofNew(dataItem.changeLog()));
  }

  public static <T extends ChangeLogging> Guarded<T> ofContinued(T dataItem) {
    return new Guarded<>(dataItem, AccessLog.ofContinued(dataItem.changeLog()));
  }

  public <E> Result<Guarded<T>, E> authorized(Function<Events, Result<Void, E>> authorization) {
    return authorization.apply(accessLog.accessEvents())
      .yield(() -> copied(but -> but.accessLog = accessLog.baselined()));
  }

  /**
   * Applies the given function to the guarded data item. Returns the guarded result value of the function. The function must be a pure
   * function without side effects.
   * <p>
   * Of course, we cannot enforce that the function is pure. If you don't provide a pure function, you might compromise the security
   * properties that this object provides. Namely that side effects of the guarded data item cannot happen unless all data access has
   * been authorized first.
   * </p>
   * <p>
   * You may want to use the {@link GuardedFunctions#guarded(Function)} (Mutation)} in order to apply the function in the context of a
   * {@code DoTo}-flow.
   * </p>
   *
   * @param mutation the function to be applied
   * @param <E>      the type of the errors that may be produced by the function
   * @return a Result with a the guarded result value of the function. If the function fails with errors, the returned Result contains
   * those errors.
   */
  public <E> Result<Guarded<T>, E> mutated(Function<T, Result<T, E>> mutation) {
    return mutation.apply(dataItem)
      .map(mutated -> copied(but -> {
        but.dataItem = mutated;
        but.accessLog = accessLog.continued(mutated.changeLog());
      }));
  }

  public <U, E> Result<U, E> map(Function<T, Result<U, E>> f) {
    return f.apply(dataItem);
  }

  private Guarded<T> copied(Consumer<Guarded<T>> modification) {
    var theCopy = new Guarded<>(this);
    modification.accept(theCopy);
    return theCopy;
  }

  /**
   * Yields the guarded data item provided that all data access has been authorized earlier. Throws an exception if you failed to
   * authorize the data access.
   *
   * @return the guarded data item
   * @throws IllegalStateException if there are data access events that have not been authorized yet
   */
  public T tryYield() {
    return tryYield(Function.identity());
  }

  public <U> U tryYield(Function<T, U> lense) {
    return secureYield(lense)
      .ifFailureThrow(this::unauthorizedAccessEvents)
      .value();
  }

  public <U> Result<U, Event> secureYield(Function<T, U> lense) {
    return this.expectNoUnauthorizedAccessEvents()
      .yield(() -> dataItem)
      .map(lense);
  }

  private RuntimeException unauthorizedAccessEvents(List<Event> unauthorizedAccessEvents) {
    return new IllegalStateException("There are unauthorized data access events: " + unauthorizedAccessEvents);
  }

  private Result<Void, Event> expectNoUnauthorizedAccessEvents() {
    Events unauthorizedAccess = accessLog.accessEvents();

    if (unauthorizedAccess.isEmpty()) {
      return Result.voidSuccess();
    } else {
      return Result.failure(unauthorizedAccess.events());
    }
  }
}

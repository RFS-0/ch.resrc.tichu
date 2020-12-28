package ch.resrc.tichu.ports.outbound.authorization;

import ch.resrc.tichu.capabilities.changelog.ChangeLog;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis;
import ch.resrc.tichu.capabilities.events.Events;
import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.domain.value_objects.Id;

/**
 * Authorizes or denies access to data items based on the implementation's access control policies.
 * <p>
 * An adapter needs to implement this interface to supply the concrete access control strategy for the application. An implementation
 * may call a remote authorization system to do the authorization.
 * </p>
 * Access control is based on data access events. A data access event states how data has been accessed in the course of a use case. A
 * data access event signals either a read or a mutation. The use case collects all data access events and passes them to this object
 * for authorization. This object decides whether all data access was authorized. If any data access is denied, the use case prevents
 * all side effects of the denied data access. The fact that the data has already been accessed by the use case is of no consequence,
 * because it does not have any visible side effects.
 * <p>
 * Our access control strategy requires that data access is separated from externally visible side effects and persistence. Since the
 * use case ensures that data that was illegally accessed has no side effects, we can safely authorize after the data processing has
 * taken place. This has the great advantage that we always authorize the access that has
 * <em>actually</em> happened.
 * <p>
 * Contrast this with an approach that authorizes access as a precondition to the actual processing. The precondition must reflect what
 * access the programmer assumes is going to happen in the use case. This is error prone. It is easy to overlook some access mode. The
 * precondition can easily become wrong if the use case flow changes. If we authorize after the fact, we can be sure that we always
 * check all access that has actually happened, no matter how the use case flow changes.
 * </p>
 * <p>
 * Our approach is feasible because our domain entities are immutable objects that record all mutations in a {@link ChangeLog}, anyway.
 * The access authorization is primarily based on these mutation events, plus the {@link DataAccessed} event. The entities can be
 * mutated without externally visible side effects. By design, any side effects, including persistence, must be explicitly invoked by
 * the use case workflows and they happen after access authorization.
 * </p>
 */
public interface AccessControl {


  /**
   * Authorizes the access that has happened without side effects to some data item for the specified client.
   *
   * @param accessEvents the events that state how the data in question was accessed.
   * @param who          identifies the client who wants to access the data item
   * @return the result of the authorization. A success, if the client was authorized to access the data in the ways that the access
   * events describe. A failure with errors describing the lack of privileges, if the client was not authorized for the stated access.
   */
  Result<Void, AuthorizationError> authorize(Events accessEvents, Id who);

  /**
   * Returns an {@link AccessControl} that authorizes any access for anyone.
   *
   * @return as explained
   */
  static AccessControl grantAll() {
    return (accessEvents, who) -> Result.voidSuccess();
  }

  /**
   * Returns an {@link AccessControl} that denies any access to anyone.
   *
   * @return as explained
   */
  static AccessControl denyAll() {
    return (accessEvents, who) -> Result.<Problem>voidFailure(AuthorizationProblem.ACCESS_DENIED)
      .mapErrors(ProblemDiagnosis::of)
      .mapErrors(AuthorizationError::new);
  }
}

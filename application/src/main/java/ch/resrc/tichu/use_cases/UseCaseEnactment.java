package ch.resrc.tichu.use_cases;

/**
 * Defines the object collaboration of roles that embodies the use case behaviour. Defines the entry point of the use case logic in the
 * implementation of the {@link #apply()} operation. When {@link #apply()} gets called, the use case gets enacted.
 *
 * <p>Intended to be instantiated by the use case invocation method as an anonymous inner class.
 * The anonymous inner class contains an initializer block that sets up the objects and roles for the use case execution. The {@link
 * #apply()} method normally sets of the use case execution by just calling one of the role objects.
 * </p>
 *
 * <p>In terms of DCI, an implementation can be thought of a DCI context that assigns the roles of
 * the context and defines the use case entry point.</p>
 */
public interface UseCaseEnactment {

  /**
   * Executes the use case logic.
   */
  UseCaseOutput apply();

}

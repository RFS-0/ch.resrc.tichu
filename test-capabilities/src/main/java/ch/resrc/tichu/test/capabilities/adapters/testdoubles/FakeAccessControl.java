package ch.resrc.tichu.test.capabilities.adapters.testdoubles;

import ch.resrc.tichu.capabilities.error_handling.*;
import ch.resrc.tichu.capabilities.events.*;
import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.domain.value_objects.*;
import ch.resrc.tichu.use_cases.support.outbound_ports.authorization.*;
import org.hamcrest.*;

import java.util.*;
import java.util.stream.*;

import static ch.resrc.tichu.test.capabilities.habits.assertions.CustomMatchers.*;
import static ch.resrc.tichu.use_cases.support.outbound_ports.authorization.AuthorizationProblem.*;
import static java.util.stream.Collectors.*;
import static org.hamcrest.core.Is.*;

/**
 * A fake implementation of the {@link AccessControl} port.
 * <p>
 * Define rules using Hamcrest matchers for the access attempt. The rule is applied if the Hamcrest matcher matches
 * the access attempt.
 */
public class FakeAccessControl implements AccessControl {

    private final List<AccessRule> accessRules = new ArrayList<>();
    private Events authorizedEvents = Events.none();

    public FakeAccessControl() {
    }

    @Override
    public Result<Void, AuthorizationError> authorize(Events accessEvents, ClientId who) {

        this.authorizedEvents = this.authorizedEvents.added(accessEvents);

        List<Result<Void, AuthorizationError>> accessDenied =
                accessEvents.stream()
                        .map((Event x) -> new AccessAttempt(x, who))
                        .flatMap(this::deniedAccess)
                        .map(Result::voidFailure)
                        .collect(toList());

        return Precondition.of(accessDenied)
                .thenVoidSuccess();
    }

    private Stream<AuthorizationError> deniedAccess(AccessAttempt accessAttempt) {

        return accessRules.stream()
                .filter((AccessRule rule) -> rule.isDenied(accessAttempt))
                .map(__ -> new AuthorizationError(ProblemDiagnosis.of(ACCESS_DENIED)));
    }

    public FakeAccessControl withRule(AccessRule rule) {

        this.accessRules.add(rule);
        return this;
    }

    public static class AccessAttempt {

        final Object accessEvent;
        final ClientId who;

        public AccessAttempt(Object accessEvent, ClientId who) {

            this.accessEvent = accessEvent;
            this.who = who;
        }

        public Object accessEvent() {
            return accessEvent;
        }

        public ClientId who() {
            return who;
        }
    }

    interface AccessRule {

        boolean isDenied(AccessAttempt accessAttempt);
    }

    /**
     * Denies access if the access attempt matches the specified criteria.
     * The criteria are expressed as Hamcrest matchers.
     */
    public static class Deny implements AccessRule {

        private final Matcher<?> ruleMatcher;

        private Deny(Matcher<AccessAttempt> ruleMatcher) {
            this.ruleMatcher = ruleMatcher;
        }

        /**
         * Denies access if the accessed data is of the given data type.
         *
         * @param dataType the data type that this rule matches
         *
         * @return a rule that denies access if the two criterion matches
         */
        public static <T> Deny of(Class<T> dataType) {

            return Deny.of(dataType, Matchers.any(dataType));
        }

        public static <T> Deny of(Class<T> dataType, Matcher<? super T> dataCriterion) {

            Matcher<AccessAttempt> accessCriterion =
                    accessEventIs(DataAccessed.class,
                            whereAttribute(DataAccessed::accessedData,
                                    "accessed item",
                                    instanceOf(dataType, dataCriterion)));

            return Deny.of(accessCriterion);
        }

        /**
         * Denies access if the access event matches the given criterion.
         *
         * @param ruleMatchCriterion the rule is only applied to access events that matche this criterion
         *
         * @return a rule that denies access if the criterion matches
         */
        public static Deny of(Matcher<AccessAttempt> ruleMatchCriterion) {

            return new Deny(ruleMatchCriterion);
        }

        @Override
        public boolean isDenied(AccessAttempt accessAttempt) {
            return ruleMatcher.matches(accessAttempt);
        }
    }

    public static <T> Matcher<AccessAttempt> accessEventIs(Class<T> eventType, Matcher<? super T> payloadMatcher) {

        return whereAttribute(AccessAttempt::accessEvent,
                "access event",
                instanceOf(eventType, payloadMatcher));
    }

    public static <E> Matcher<AccessAttempt> mutationError(Class<E> errorType) {

        return accessEventIs(MutationFailed.class, whereAttribute(MutationFailed::error, Matchers.instanceOf(errorType)));
    }

    public static Matcher<AccessAttempt> invokedUseCaseIs(Class<?> useCase) {

        return accessEventIs(UseCaseInvoked.class,
                whereAttribute(UseCaseInvoked::invokedUseCase,
                        is(useCase)));
    }
}

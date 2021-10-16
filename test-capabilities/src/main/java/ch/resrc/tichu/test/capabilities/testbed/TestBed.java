package ch.resrc.tichu.test.capabilities.testbed;

import ch.resrc.tichu.test.capabilities.adapters.testdoubles.*;
import ch.resrc.tichu.use_cases.outbound_ports.authorization.*;
import ch.resrc.tichu.use_cases.outbound_ports.eventbus.*;
import ch.resrc.tichu.use_cases.outbound_ports.transaction.*;

import java.util.stream.*;

public final class TestBed {

    private final Adapters adapters = new Adapters();

    private final TestBedFixtureSupport fixtureSupport = new TestBedFixtureSupport(adapters);

    public Ports ports() {
        return adapters;
    }

    public Adapters adapters() {
        return adapters;
    }

    public FixtureSupport fixtureSupport() {
        return fixtureSupport;
    }

    /**
     * The testing API. A facade to the test habits. Provides tests with access to the functionality offered by all
     * test habits. Lets you drive use cases, query output ports, setup fixtures, do domain assertions
     * through a single facade.
     */
    public static class Context implements TestBedContextHabits {

        private final TestBed testBed;

        public Context(TestBed testBed, TestBedModifier... testBedModifications) {

            this.testBed = testBed;
            Stream.of(testBedModifications).forEach(x -> x.accept(this.testBed));
        }

        public Context(TestBedModifier... adjustments) {

            this(new TestBed(), adjustments);
        }

        public TestBed testBed() {
            return testBed;
        }

        public TestBed testBed(TestBedModifier modifier) {
            modifier.accept(testBed);
            return testBed;
        }

        public FixtureSupport fixtureSupport() {
            return testBed.fixtureSupport();
        }

        public Ports ports() {
            return testBed.ports();
        }

        public Adapters adapters() {
            return testBed.adapters();
        }
    }

    /**
     * Maintains the port adapters for the test environment. Allows you to change the port adapters.
     */
    @SuppressWarnings("UnusedReturnValue")
    public static class Adapters implements Ports {

        private EventBus eventBus = new FakeEventBus();

        private TransactionAdapters transactionAdapters = TransactionAdapters.NO_OPS;

        private AccessControl accessControl = AccessControl.grantAll();

        public Adapters useAccessControl(AccessControl accessControl) {

            this.accessControl = accessControl;
            return this;
        }

        public Adapters useEventBus(EventBus eventBus) {

            this.eventBus = eventBus;
            return this;
        }

        public Adapters useTransactionAdapters(TransactionAdapters transactionAspects) {

            this.transactionAdapters = transactionAspects;
            return this;
        }

        @Override
        public NewTransaction newTransaction() {
            return transactionAdapters.newTransaction();
        }

        @Override
        public EnsuredTransaction ensuredTransaction() {
            return transactionAdapters.ensuredTransaction();
        }

        @Override
        public EventBus eventBus() {
            return eventBus;
        }

        @Override
        public AccessControl accessControl() {
            return accessControl;
        }
    }

    /**
     * Provides use case drivers to fixture habits that want to create the fixture by
     * running use cases. Fixtures should always use this object to drive use cases.
     * This allows us to revise use cases while using the old use case for all fixtures.
     * Avoids unnecessary test failures of unrelated tests if the new use case has bugs
     * during development.
     */
    public static class TestBedFixtureSupport implements FixtureSupport {

        private final FixtureUseCases fixtureUseCases;
        private final Adapters adapters;

        TestBedFixtureSupport(Adapters adapters) {

            this.fixtureUseCases = new FixtureUseCases(adapters, this);
            this.adapters = adapters;
        }
    }

    /**
     * Provides the use case functionality that the test bed fixture support needs to setup fixtures.
     */
    private static class FixtureUseCases {

        private final Ports ports;
        private final FixtureSupport fixtureSupport;

        FixtureUseCases(Ports ports, FixtureSupport fixtureSupport) {

            this.ports = ports;
            this.fixtureSupport = fixtureSupport;
        }

        public Ports ports() {

            return ports;
        }

        public FixtureSupport fixtureSupport() {
            return fixtureSupport;
        }
    }
}

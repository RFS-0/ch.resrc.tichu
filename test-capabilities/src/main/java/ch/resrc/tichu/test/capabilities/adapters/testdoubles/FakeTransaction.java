package ch.resrc.tichu.test.capabilities.adapters.testdoubles;

import ch.resrc.tichu.capabilities.error_handling.*;
import ch.resrc.tichu.capabilities.events.*;
import ch.resrc.tichu.use_cases.outbound_ports.transaction.*;

import java.util.*;

import static java.util.stream.Collectors.*;

public class FakeTransaction implements TransactionAdapters {

    private Events events = Events.none();

    @Override
    public NewTransaction newTransaction() {

        return (Runnable action) -> {
            events = events.added(new NewTransactionRequired());
            runAndCommitOrRollback(action);
        };
    }

    @Override
    public EnsuredTransaction ensuredTransaction() {

        return (Runnable action) -> {
            events = events.added(new TransactionRequired());
            runAndCommitOrRollback(action);
        };
    }

    private void runAndCommitOrRollback(Runnable action) {

        Try.ofVoid(action)
           .onSuccess(() -> events = events.added(new TransactionCommitted()))
           .onFailure(thrown -> events = events.added(new TransactionRolledBack()))
           .onFailureThrow();
    }

    public List<Class<? extends Event>> events() {
        return events.events().stream().map(Event::getClass).collect(toList());
    }

    public static class NewTransactionRequired extends Event {}

    public static class TransactionRequired extends Event {}

    public static class TransactionRolledBack extends Event {}

    public static class TransactionCommitted extends Event {}
}

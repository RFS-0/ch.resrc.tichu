package ch.resrc.tichu.use_cases.outbound_ports.transaction;

/**
 * Makes sure that a given action is run within a transaction.
 */
public interface EnsuredTransaction extends TransactionalExecution {

    /**
     * Executes an action within a transaction. If a transaction is already active, the action runs within that transaction.
     * If there is no active transaction, a new transaction is created for the duration of the action.
     *
     * @param action the action to be executed
     */
    @Override
    void execute(Runnable action);

    EnsuredTransaction NO_OP = Runnable::run;
}

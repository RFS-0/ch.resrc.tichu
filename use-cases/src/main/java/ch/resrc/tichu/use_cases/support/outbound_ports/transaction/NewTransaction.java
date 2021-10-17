package ch.resrc.tichu.use_cases.support.outbound_ports.transaction;

/**
 * Executes an action within a new transaction.
 */
public interface NewTransaction extends TransactionalExecution {

    /**
     * Executes an action within a new transaction.
     *
     * @param action the action to be executed
     */
    @Override
    void execute(Runnable action);

}

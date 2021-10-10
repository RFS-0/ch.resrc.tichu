package ch.resrc.tichu.use_cases.outbound_ports.transaction;

/**
 * Implementations execute an action in an implementation specific transaction context.
 */
public interface TransactionalExecution {

    /**
     * Executes the given action in the implementation specific transaction context.
     *
     * @param action the action to be executed
     */
    void execute(Runnable action);

    TransactionalExecution NONE = Runnable::run;
}

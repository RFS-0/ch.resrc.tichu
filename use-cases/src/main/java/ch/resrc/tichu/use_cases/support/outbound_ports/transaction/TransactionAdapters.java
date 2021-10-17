package ch.resrc.tichu.use_cases.support.outbound_ports.transaction;

/**
 * Provides related transaction adapters for a given application context.
 */
public interface TransactionAdapters {

    TransactionAdapters NO_OPS = new TransactionAdapters() {};

    default EnsuredTransaction ensuredTransaction() {return Runnable::run;}

    default NewTransaction newTransaction() {return Runnable::run;}

}

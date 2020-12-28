package ch.resrc.tichu.security.events;

import ch.resrc.tichu.capabilities.errorhandling.faults.Fault;

public class SecurityProblemDetected extends SecurityEvent {

  private Fault securityProblem;

  public SecurityProblemDetected(Fault securityProblem) {
    super();
    this.securityProblem = securityProblem;
  }

}

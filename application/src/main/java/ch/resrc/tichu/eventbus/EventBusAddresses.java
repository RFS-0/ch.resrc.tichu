package ch.resrc.tichu.eventbus;

public enum EventBusAddresses {

  USERS("users"),
  GAMES("games"),
  TEAMS("teams"),
  ERRORS("errors");

  private final String address;

  EventBusAddresses(String address) {
    this.address = address;
  }

  public String value() {
    return address;
  }
}

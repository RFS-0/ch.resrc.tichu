package ch.resrc.tichu.capabilities.errorhandling;

public enum DomainProblem implements Problem {

  INVALID_PROPERTY_MUTATION("${validationErrors}"),

  MANDATORY_VALUE_MISSING("${message}"),

  USER_NOT_FOUND("User with id=<${id}> does not exist."),

  USERS_NOT_FOUND("Could not load existing users."),

  USERS_NOT_SAVED("User with id=<${id}> could not be saved."),

  TEAM_NOT_FOUND("Team with id=<${id}> does not exist."),

  TEAM_NOT_SAVED("Team with id=<${id}> could not be saved."),

  TEAM_NOT_UPDATED("Team with id=<${id}> could not be updated."),

  TEAMS_NOT_FOUND("Could not load existing teams."),

  PLAYER_NOT_FOUND("Player with id=<${id}> does not exist."),

  PLAYER_NOT_SAVED("Player with id=<${id}> could not be saved."),

  PLAYER_NOT_UPDATED("Player with id=<${id}> could not be updated."),

  PLAYERS_NOT_FOUND("Could not load existing players."),

  GAME_NOT_FOUND("Game with id=<${id}> does not exist."),

  GAME_NOT_SAVED("Game with id=<${id}> could not be saved."),

  GAME_NOT_UPDATED("Game with id=<${id}> could not be updated."),

  GAMES_NOT_FOUND("Could not load existing games."),

  ROUND_NOT_FOUND("Round with roundNumber=<${roundNumber}> does not exist."),

  INVARIANT_VIOLATED("${message}");

  public static DomainProblem[] all() {
    return DomainProblem.values();
  }

  private final String details;

  DomainProblem(String details) {
    this.details = details;
  }

  @Override
  public String title() {
    return "Domain rule violated";
  }

  @Override
  public String detailsTemplate() {
    return details;
  }
}

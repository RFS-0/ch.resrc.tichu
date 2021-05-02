package ch.resrc.tichu.adapters.endpoints_websocket;

public final class WebSocketAddresses {

  public static class Games {

    public static final String CREATE = "/events/games/create";
    public static final String CREATED = "/events/games/created/{receiverId}";
    public static final String UPDATE_TEAM_NAME = "/events/games/update-team-name/{receiverId}";
    public static final String UPDATED_TEAM_NAME = "/events/games/updated-team-name/{receiverId}";
  }

  public static class Teams {

    public static final String FIND_BY_ID = "/events/teams/find-by-id";
    public static final String FOUND_BY_ID = "/events/teams/found/{receiverId}";
    public static final String ADD_FIRST_PLAYER_TO_TEAM = "/events/teams/add-first-player-to-team";
    public static final String ADDED_FIRST_PLAYER_TO_TEAM = "/events/teams/added-first-player-to-team/{receiverId}";
    public static final String ADD_SECOND_PLAYER_TO_TEAM = "/events/teams/add-second-player-to-team";
    public static final String ADDED_SECOND_PLAYER_TO_TEAM = "/events/teams/added-second-player-to-team/{receiverId}";
    public static final String REMOVE_FIRST_PLAYER_FROM_TEAM = "/events/teams/remove-first-player-from-team";
    public static final String REMOVED_FIRST_PLAYER_FROM_TEAM = "/events/teams/removed-first-player-from-team/{receiverId}";
    public static final String REMOVE_SECOND_PLAYER_FROM_TEAM = "/events/teams/remove-second-player-from-team";
    public static final String REMOVED_SECOND_PLAYER_FROM_TEAM = "/events/teams/removed-second-player-from-team/{receiverId}";

  }

}

package ch.resrc.tichu.adapters.endpoints_websocket;

public final class WebSocketAddresses {

  public static class UseCases {

    public static class Input {
      public static final String CREATE_A_GAME = "/events/games/create";
      public static final String UPDATE_TEAM_NAME = "/events/games/update-team-name/{receiverId}";
      public static final String ADD_FIRST_PLAYER_TO_TEAM = "/events/teams/add-first-player-to-team";
      public static final String FIND_BY_ID = "/events/teams/find-by-id";
      public static final String ADD_SECOND_PLAYER_TO_TEAM = "/events/teams/add-second-player-to-team";
      public static final String REMOVE_FIRST_PLAYER_FROM_TEAM = "/events/teams/remove-first-player-from-team";
      public static final String REMOVE_SECOND_PLAYER_FROM_TEAM = "/events/teams/remove-second-player-from-team";
      public static final String UPDATE_RANK_OF_PLAYER = "/events/games/update-rank-of-player/{receiverId}";
    }

    public static class Output {
      public static final String CREATE_A_GAME = "/events/games/created/{receiverId}";
      public static final String UPDATE_TEAM_NAME = "/events/games/updated-team-name/{receiverId}";
      public static final String ADD_FIRST_PLAYER_TO_TEAM = "/events/teams/added-first-player-to-team/{receiverId}";
      public static final String FIND_BY_ID = "/events/teams/found/{receiverId}";
      public static final String ADD_SECOND_PLAYER_TO_TEAM = "/events/teams/added-second-player-to-team/{receiverId}";
      public static final String REMOVE_FIRST_PLAYER_FROM_TEAM = "/events/teams/removed-first-player-from-team/{receiverId}";
      public static final String REMOVE_SECOND_PLAYER_FROM_TEAM = "/events/teams/removed-second-player-from-team/{receiverId}";
      public static final String UPDATE_RANK_OF_PLAYER = "/events/games/updated-rank-of-player/{receiverId}";
    }
  }
}

package ch.resrc.old.adapters.endpoints_websocket;

public final class WebSocketAddresses {

  public static class Input {
    public static final String USE_CASE_INPUT = "/events/use-cases/input/{useCase}";
  }

  public static class Output {
    public static final String USE_CASE_OUTPUT = "/events/use-cases/output/{useCase}/{receiverId}";
  }

  public static class UseCases {

    public static final String UPDATE_FIRST_PLAYER_OF_TEAM = "update-first-player-of-team";
    public static final String UPDATE_SECOND_PLAYER_OF_TEAM = "update-second-player-of-team";
    public static final String CREATE_GAME = "create-game";
    public static final String FINISH_GAME = "finish-game";
    public static final String FINISH_ROUND = "finish-round";
    public static final String RESET_RANK_OF_PLAYER = "reset-rank-of-player";
    public static final String UPDATE_CARD_POINTS_OF_ROUND = "update-card-points-of-round";
    public static final String UPDATE_RANK_OF_PLAYER = "update-rank-of-player";
    public static final String UPDATE_ROUND = "update-round";
    public static final String UPDATE_TEAM_NAME = "update-team-name";
  }
}

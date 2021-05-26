package ch.resrc.tichu.adapters.endpoints_websocket.add_first_player_to_team;

import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketAddresses;
import ch.resrc.tichu.adapters.endpoints_websocket.WebSocketClient;
import ch.resrc.tichu.adapters.endpoints_websocket.add_first_player_to_team.dto.IntendedFirstPlayerAdditionDto;
import ch.resrc.tichu.adapters.endpoints_websocket.common.dto.GameDto;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.domain.entities.Game;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.domain.entities.Team;
import ch.resrc.tichu.domain.entities.User;
import ch.resrc.tichu.domain.operations.AddGame;
import ch.resrc.tichu.domain.operations.AddPlayer;
import ch.resrc.tichu.domain.operations.AddTeam;
import ch.resrc.tichu.domain.operations.GetAllGames;
import ch.resrc.tichu.domain.operations.GetAllPlayers;
import ch.resrc.tichu.domain.operations.GetAllTeams;
import ch.resrc.tichu.domain.value_objects.Email;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.JoinCode;
import ch.resrc.tichu.domain.value_objects.Name;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.vavr.collection.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.websocket.ContainerProvider;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.await;

@QuarkusTest
class AddFirstPlayerToTeamWSTest {

  private static final String GAME_ID = "33eced03-f78a-4447-9364-bd938d0e3690";
  private static final String USER_ID = "6a14a44d-eb98-497f-96ac-2e5dc516d40b";
  private static final String LEFT_TEAM_ID = "eff532e9-177a-45bb-9f22-5522ee16e2ce";
  private static final String RIGHT_TEAM_ID = "7b6edbdd-386e-40f7-8dd4-ee2586d2483d";
  private static final JoinCode JOIN_CODE = JoinCode.next();
  private static final String GERARD_BLACKWELL = "Gerard Blackwell";

  private final Json json;
  private final GetAllPlayers getAllPlayers;
  private final AddPlayer addPlayer;
  private final GetAllGames getAllGames;
  private final AddGame addGame;
  private final GetAllTeams getAllTeams;
  private final AddTeam addTeam;

  @TestHTTPResource(WebSocketAddresses.UseCases.Input.ADD_FIRST_PLAYER_TO_TEAM)
  URI add;
  @TestHTTPResource("/events/teams/added-first-player-to-team/" + GAME_ID)
  URI added;

  public AddFirstPlayerToTeamWSTest(Json json,
                                    GetAllPlayers getAllPlayers,
                                    AddPlayer addPlayer,
                                    GetAllGames getAllGames,
                                    AddGame addGame,
                                    GetAllTeams getAllTeams,
                                    AddTeam addTeam) {
    this.json = json;
    this.getAllPlayers = getAllPlayers;
    this.addPlayer = addPlayer;
    this.getAllGames = getAllGames;
    this.addGame = addGame;
    this.getAllTeams = getAllTeams;
    this.addTeam = addTeam;
  }

  @BeforeEach
  public void setup() {
    User existingUser = User.create(
      Id.resultOf(USER_ID).get(),
      Name.resultOf(GERARD_BLACKWELL).get(),
      Email.resultOf("gerard@blackwell.com").get(),
      Instant.now()
    ).get();

    Player existingPlayer = Player.create(existingUser.id(), existingUser.name(), existingUser.createdAt()).get();

    Team anExistingTeam = Team.create(Id.resultOf(LEFT_TEAM_ID).get()).get();
    Team anotherExistingTeam = Team.create(Id.resultOf(RIGHT_TEAM_ID).get()).get();

    Game existingGame = Game.create(
      Id.resultOf(GAME_ID).get(),
      existingUser,
      JOIN_CODE,
      HashSet.of(anExistingTeam, anotherExistingTeam),
      HashSet.empty(),
      Instant.now()
    ).get();

    addPlayer.add(HashSet.empty(), existingPlayer);
    addTeam.add(HashSet.empty(), anExistingTeam);
    addTeam.add(getAllTeams.getAll().get(), anotherExistingTeam);
    addGame.add(HashSet.empty(), existingGame);
  }

  @Test
  void onMessage_validIntendedFirstUserAddition_usecaseSuccessfullyExecutedAndResultReceivedByCreatedClient() throws Exception {
    // given
    WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
    WebSocketClient addedClient = new WebSocketClient();
    Id gameToAddPlayerTo = Id.resultOf(GAME_ID).get();
    Id teamToAddPlayerTo = Id.resultOf(LEFT_TEAM_ID).get();
    Id userToAdd = Id.resultOf(USER_ID).get();

    Player firstPlayerOfTeam = findFirstPlayerOfTeam(gameToAddPlayerTo, teamToAddPlayerTo);
    assertThat(firstPlayerOfTeam).isNull();

    // when
    try (Session add = webSocketContainer.connectToServer(WebSocketClient.class, this.add);
         Session added = webSocketContainer.connectToServer(addedClient, this.added)) {
      IntendedFirstPlayerAdditionDto dto = new IntendedFirstPlayerAdditionDto(
        gameToAddPlayerTo.value().toString(),
        teamToAddPlayerTo.value().toString(),
        userToAdd.value().toString(),
        null
      );
      RemoteEndpoint.Basic basicRemote = add.getBasicRemote();
      basicRemote.sendText(json.toJsonString(dto));

      // then
      await().atMost(3, TimeUnit.SECONDS).until(useCaseOutputReceived(addedClient));
      String sentMessage = addedClient.getMessages().get(0);
      GameDto updatedGame = json.parse(sentMessage, GameDto.class);
      Set<String> firstPlayerIds = updatedGame.teams
        .map(team -> team.firstPlayer)
        .filter(Objects::nonNull)
        .map(playerDto -> playerDto.id)
        .collect(Collectors.toSet());
      assertThat(firstPlayerIds).contains(userToAdd.value().toString());
    }
  }

  @Test
  void onMessage_validIntendedFirstPlayerAddition_usecaseSuccessfullyExecutedAndResultReceivedByCreatedClient() throws Exception {
    // given
    WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
    WebSocketClient addedClient = new WebSocketClient();
    Id gameToAddPlayerTo = Id.resultOf(GAME_ID).get();
    Id teamToAddPlayerTo = Id.resultOf(LEFT_TEAM_ID).get();
    String playerName = "Player to be added as first user";

    Player firstPlayerOfTeam = findFirstPlayerOfTeam(gameToAddPlayerTo, teamToAddPlayerTo);
    assertThat(firstPlayerOfTeam).isNull();

    // when
    try (Session add = webSocketContainer.connectToServer(WebSocketClient.class, this.add);
         Session added = webSocketContainer.connectToServer(addedClient, this.added)) {
      IntendedFirstPlayerAdditionDto dto = new IntendedFirstPlayerAdditionDto(
        gameToAddPlayerTo.value().toString(),
        teamToAddPlayerTo.value().toString(),
        null,
        playerName
      );
      RemoteEndpoint.Basic basicRemote = add.getBasicRemote();
      basicRemote.sendText(json.toJsonString(dto));

      // then
      await().atMost(3, TimeUnit.SECONDS).until(useCaseOutputReceived(addedClient));
      String sentMessage = addedClient.getMessages().get(0);
      GameDto updatedGame = json.parse(sentMessage, GameDto.class);
      Set<String> firstPlayerIds = updatedGame.teams
        .map(team -> team.firstPlayer)
        .filter(Objects::nonNull)
        .map(playerDto -> playerDto.name)
        .collect(Collectors.toSet());
      assertThat(firstPlayerIds).contains(playerName);
    }
  }

  private Player findFirstPlayerOfTeam(Id gameToAddPlayerTo, Id teamToAddPlayerTo) {
    return getAllGames.getAll().get()
      .find(game -> game.id().equals(gameToAddPlayerTo))
      .map(Game::teams).get()
      .find(team -> team.id().equals(teamToAddPlayerTo))
      .map(Team::firstPlayer).get();
  }

  private Callable<Boolean> useCaseOutputReceived(WebSocketClient createdClient) {
    return () -> createdClient.getMessages().size() > 0;
  }
}

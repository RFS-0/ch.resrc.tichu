package ch.resrc.tichu.adapters.endpoints_websocket;

import ch.resrc.tichu.adapters.endpoints_websocket.input.GameDto;
import ch.resrc.tichu.adapters.endpoints_websocket.input.IntendedCardPointsUpdateDto;
import ch.resrc.tichu.adapters.endpoints_websocket.input.IntendedFirstPlayerRemovalDto;
import ch.resrc.tichu.adapters.endpoints_websocket.input.IntendedFirstPlayerUpdateDto;
import ch.resrc.tichu.adapters.endpoints_websocket.input.IntendedGameDto;
import ch.resrc.tichu.adapters.endpoints_websocket.input.IntendedGameFinishDto;
import ch.resrc.tichu.adapters.endpoints_websocket.input.IntendedPlayerRankResetDto;
import ch.resrc.tichu.adapters.endpoints_websocket.input.IntendedPlayerRankUpdateDto;
import ch.resrc.tichu.adapters.endpoints_websocket.input.IntendedRoundFinishDto;
import ch.resrc.tichu.adapters.endpoints_websocket.input.IntendedSecondPlayerAdditionDto;
import ch.resrc.tichu.adapters.endpoints_websocket.input.IntendedSecondPlayerRemovalDto;
import ch.resrc.tichu.adapters.endpoints_websocket.input.IntendedTeamNameDto;
import ch.resrc.tichu.capabilities.json.Json;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.operations.CreateGame;
import ch.resrc.tichu.domain.operations.FinishGame;
import ch.resrc.tichu.domain.operations.UpdateFirstPlayerOfTeam;
import ch.resrc.tichu.domain.operations.UpdateSecondPlayerOfTeam;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import ch.resrc.tichu.domain.value_objects.RoundNumber;
import ch.resrc.tichu.use_cases.add_first_player_to_team.UpdateFirstPlayerOfTeamInput;
import ch.resrc.tichu.use_cases.add_second_player_to_team.UpdateSecondPlayerOfTeamInput;
import ch.resrc.tichu.use_cases.common.input.IntendedPlayerRemoval;
import ch.resrc.tichu.use_cases.create_game.CreateGameInput;
import ch.resrc.tichu.use_cases.finish_game.FinishGameInput;
import ch.resrc.tichu.use_cases.finish_round.ports.input.IntendedRoundFinish;
import ch.resrc.tichu.use_cases.reset_rank_of_player.ports.input.IntendedPlayerRankReset;
import ch.resrc.tichu.use_cases.update_a_team_name.ports.input.IntendedTeamName;
import ch.resrc.tichu.use_cases.update_card_points_of_round.ports.input.IntendedCardPointsUpdate;
import ch.resrc.tichu.use_cases.update_rank_of_player.ports.input.IntendedPlayerRankUpdate;
import ch.resrc.tichu.use_cases.update_round.ports.input.IntendedRoundUpdate;
import io.vavr.collection.HashSet;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import io.vavr.control.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicReference;

import static ch.resrc.tichu.domain.validation.DomainObjectInput.parse;
import static ch.resrc.tichu.use_cases.add_first_player_to_team.UpdateFirstPlayerOfTeamInput.anUpdateFirstPlayerOfTeamInput;
import static ch.resrc.tichu.use_cases.add_second_player_to_team.UpdateSecondPlayerOfTeamInput.anUpdateSecondPlayerOfTeamInput;
import static ch.resrc.tichu.use_cases.common.input.IntendedPlayerRemoval.anIntendedPlayerRemoval;
import static ch.resrc.tichu.use_cases.create_game.CreateGameInput.aCreateGameInput;
import static ch.resrc.tichu.use_cases.finish_game.FinishGameInput.aFinishGameInput;
import static ch.resrc.tichu.use_cases.finish_round.ports.input.IntendedRoundFinish.anIntendedRoundFinish;
import static ch.resrc.tichu.use_cases.reset_rank_of_player.ports.input.IntendedPlayerRankReset.anIntendedPlayerRankReset;
import static ch.resrc.tichu.use_cases.update_a_team_name.ports.input.IntendedTeamName.anIntendedTeamName;
import static ch.resrc.tichu.use_cases.update_card_points_of_round.ports.input.IntendedCardPointsUpdate.anIntendedCardPointsUpdate;
import static ch.resrc.tichu.use_cases.update_rank_of_player.ports.input.IntendedPlayerRankUpdate.anIntendedPlayerRankUpdate;
import static ch.resrc.tichu.use_cases.update_round.ports.input.IntendedRoundUpdate.anIntendedRoundUpdate;

@ServerEndpoint(WebSocketAddresses.Input.USE_CASE_INPUT)
public class UseCaseInputSocket {

  private static final Logger LOG = LoggerFactory.getLogger(UseCaseInputSocket.class);

  private static final AtomicReference<Set<Session>> SESSIONS_REF = new AtomicReference<>(HashSet.empty());

  private final Json json;
  private final UseCaseOutputSocket outputSocket;
  private final UpdateFirstPlayerOfTeam updateFirstPlayerOfTeam;
  private final UpdateSecondPlayerOfTeam updateSecondPlayerOfTeam;
  private final CreateGame createGame;
  private final FinishGame finishGame;

  public UseCaseInputSocket(Json json,
                            UseCaseOutputSocket outputSocket,
                            UpdateFirstPlayerOfTeam updateFirstPlayerOfTeam,
                            UpdateSecondPlayerOfTeam updateSecondPlayerOfTeam,
                            CreateGame createGame,
                            FinishGame finishGame) {
    this.json = json;
    this.outputSocket = outputSocket;
    this.updateFirstPlayerOfTeam = updateFirstPlayerOfTeam;
    this.updateSecondPlayerOfTeam = updateSecondPlayerOfTeam;
    this.createGame = createGame;
    this.finishGame = finishGame;
  }

  @OnOpen
  public void onOpen(Session session) {
    SESSIONS_REF.updateAndGet(sessions -> sessions.add(session));
  }

  @OnClose
  public void onClose(Session session) {
    SESSIONS_REF.updateAndGet(sessions -> sessions.remove(session));
  }

  @OnMessage
  public void onMessage(@PathParam("useCase") String useCase, String message) {
    switch (useCase) {
      case WebSocketAddresses.UseCases.UPDATE_FIRST_PLAYER_OF_TEAM -> executeUpdateFirstPlayerOfTeamUseCase(message);
      case WebSocketAddresses.UseCases.UPDATE_SECOND_PLAYER_OF_TEAM -> executeUpdateSecondPlayerOfTeamUseCase(message);
      case WebSocketAddresses.UseCases.CREATE_GAME -> executeCreateGameUseCase(message);
      case WebSocketAddresses.UseCases.FINISH_GAME -> executeFinishGameUseCase(message);
      default -> LOG.warn("Can not execute unknown use case '{}'", useCase);
    }
  }

  private Either<Seq<ValidationError>, UpdateFirstPlayerOfTeamInput> validateUseCaseInput(IntendedFirstPlayerUpdateDto dto) {
    return anUpdateFirstPlayerOfTeamInput()
      .withGameId(parse(Id.class, dto.gameId))
      .withTeamId(parse(Id.class, dto.teamId))
      .withUserId(parse(Id.class, dto.userId))
      .withPlayerName(parse(Name.class, dto.playerName))
      .buildResult();
  }

  private Either<Seq<ValidationError>, UpdateSecondPlayerOfTeamInput> validateUseCaseInput(IntendedSecondPlayerAdditionDto dto) {
    return anUpdateSecondPlayerOfTeamInput()
      .withGameId(parse(Id.class, dto.gameId))
      .withTeamId(parse(Id.class, dto.teamId))
      .withUserId(parse(Id.class, dto.userId))
      .withPlayerName(parse(Name.class, dto.playerName))
      .buildResult();
  }

  private Either<Seq<ValidationError>, FinishGameInput> validateUseCaseInput(IntendedGameFinishDto dto) {
    return aFinishGameInput()
      .withGameId(parse(Id.class, dto.gameId()))
      .buildResult();
  }

  private Either<Seq<ValidationError>, CreateGameInput> validateUseCaseInput(IntendedGameDto dto) {
    return aCreateGameInput()
      .withCreatedBy(parse(Id.class, dto.createdBy))
      .buildResult();
  }

  private Either<Seq<ValidationError>, IntendedRoundFinish> validatedIntendedRoundFinish(IntendedRoundFinishDto dto) {
    return anIntendedRoundFinish()
      .withGameId(parse(Id.class, dto.gameId))
      .withRoundNumber(parse(RoundNumber.class, dto.roundNumber))
      .buildResult();
  }

  private Either<Seq<ValidationError>, IntendedPlayerRemoval> validateIntendedPlayerRemoval(IntendedFirstPlayerRemovalDto dto) {
    return anIntendedPlayerRemoval()
      .withGameId(parse(Id.class, dto.gameId))
      .withTeamId(parse(Id.class, dto.teamId))
      .withUserId(parse(Id.class, dto.userId))
      .withPlayerName(parse(Name.class, dto.playerName))
      .buildResult();
  }

  private Either<Seq<ValidationError>, IntendedPlayerRemoval> validateIntendedPlayerRemoval(IntendedSecondPlayerRemovalDto dto) {
    return anIntendedPlayerRemoval()
      .withGameId(parse(Id.class, dto.gameId))
      .withTeamId(parse(Id.class, dto.teamId))
      .withUserId(parse(Id.class, dto.userId))
      .withPlayerName(parse(Name.class, dto.playerName))
      .buildResult();
  }

  private Either<Seq<ValidationError>, IntendedPlayerRankReset> validatedIntendedPlayerRankReset(IntendedPlayerRankResetDto dto) {
    return anIntendedPlayerRankReset()
      .withGameId(parse(Id.class, dto.gameId))
      .withPlayerId(parse(Id.class, dto.playerId))
      .withRoundNumber(parse(RoundNumber.class, dto.roundNumber))
      .buildResult();
  }

  private Either<Seq<ValidationError>, IntendedTeamName> validatedIntendedTeamName(IntendedTeamNameDto dto) {
    return anIntendedTeamName()
      .withGameId(parse(Id.class, dto.gameId))
      .withTeamId(parse(Id.class, dto.teamId))
      .withTeamName(parse(Name.class, dto.teamName))
      .buildResult();
  }

  private Either<Seq<ValidationError>, IntendedCardPointsUpdate> validatedIntendedTeamName(IntendedCardPointsUpdateDto dto) {
    return anIntendedCardPointsUpdate()
      .withGameId(parse(Id.class, dto.gameId))
      .withTeamId(parse(Id.class, dto.teamId))
      .withRoundNumber(parse(RoundNumber.class, dto.roundNumber))
      .withCardPoints(parse(Integer.class, dto.cardPoints))
      .buildResult();
  }

  private Either<Seq<ValidationError>, IntendedPlayerRankUpdate> validatedIntendedPlayerRankUpdate(IntendedPlayerRankUpdateDto dto) {
    return anIntendedPlayerRankUpdate()
      .withGameId(parse(Id.class, dto.gameId))
      .withPlayerId(parse(Id.class, dto.playerId))
      .withRoundNumber(parse(RoundNumber.class, dto.roundNumber))
      .buildResult();
  }

  private Either<Seq<ValidationError>, IntendedRoundUpdate> validatedIntendedRoundUpdate(IntendedRoundFinishDto dto) {
    return anIntendedRoundUpdate()
      .withGameId(parse(Id.class, dto.gameId))
      .withRoundNumber(parse(RoundNumber.class, dto.roundNumber))
      .buildResult();
  }

  private void executeUpdateFirstPlayerOfTeamUseCase(String message) {
    IntendedFirstPlayerUpdateDto dto = json.parse(message, IntendedFirstPlayerUpdateDto.class);
    final var validatedInput = validateUseCaseInput(dto);
    if (validatedInput.isLeft()) {
      LOG.warn(validatedInput.getLeft().mkString());
      System.out.println(validatedInput.getLeft());
    } else {
      final var useCaseOutput = updateFirstPlayerOfTeam.invoke(validatedInput.get());
      if (useCaseOutput.isLeft()) {
        LOG.warn(useCaseOutput.getLeft().toString());
      } else {
        final var gameDocument = useCaseOutput.get().toBePresented();
        outputSocket.send(
          gameDocument.id(),
          json.toJsonString(GameDto.fromDocument(gameDocument))
        );
      }
    }
  }

  private void executeUpdateSecondPlayerOfTeamUseCase(String message) {
    IntendedSecondPlayerAdditionDto dto = json.parse(message, IntendedSecondPlayerAdditionDto.class);
    final var validatedInput = validateUseCaseInput(dto);
    if (validatedInput.isLeft()) {
      LOG.warn(validatedInput.getLeft().mkString());
      System.out.println(validatedInput.getLeft());
    } else {
      final var useCaseOutput = updateSecondPlayerOfTeam.invoke(validatedInput.get());
      if (useCaseOutput.isLeft()) {
        LOG.warn(useCaseOutput.getLeft().toString());
      } else {
        final var gameDocument = useCaseOutput.get().toBePresented();
        outputSocket.send(
          gameDocument.id(),
          json.toJsonString(GameDto.fromDocument(gameDocument))
        );
      }
    }
  }

  private void executeCreateGameUseCase(String message) {
    final var dto = json.parse(message, IntendedGameDto.class);
    final var validatedInput = validateUseCaseInput(dto);
    if (validatedInput.isLeft()) {
      LOG.warn(validatedInput.getLeft().mkString());
      System.out.println(validatedInput.getLeft());
    } else {
      final var useCaseOutput = createGame.invoke(validatedInput.get());
      if (useCaseOutput.isLeft()) {
        LOG.warn(useCaseOutput.getLeft().toString());
      } else {
        final var gameDocument = useCaseOutput.get().toBePresented();
        outputSocket.send(
          gameDocument.id(),
          json.toJsonString(GameDto.fromDocument(gameDocument))
        );
      }
    }
  }

  private void executeFinishGameUseCase(String message) {
    final var dto = json.parse(message, IntendedGameFinishDto.class);
    final var validatedInput = validateUseCaseInput(dto);
    if (validatedInput.isLeft()) {
      LOG.warn(validatedInput.getLeft().mkString());
      System.out.println(validatedInput.getLeft());
    } else {
      final var useCaseOutput = finishGame.invoke(validatedInput.get());
      if (useCaseOutput.isLeft()) {
        LOG.warn(useCaseOutput.getLeft().toString());
      } else {
        final var gameDocument = useCaseOutput.get().toBePresented();
        outputSocket.send(
          gameDocument.id(),
          json.toJsonString(GameDto.fromDocument(gameDocument))
        );
      }
    }
  }
}

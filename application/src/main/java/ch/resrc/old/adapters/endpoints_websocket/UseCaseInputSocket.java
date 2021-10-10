package ch.resrc.old.adapters.endpoints_websocket;

import ch.resrc.old.adapters.endpoints_websocket.input.*;
import ch.resrc.old.capabilities.json.*;
import ch.resrc.old.capabilities.validations.old.*;
import ch.resrc.old.domain.operations.*;
import ch.resrc.old.domain.value_objects.*;
import ch.resrc.old.use_cases.add_first_player_to_team.*;
import ch.resrc.old.use_cases.add_second_player_to_team.*;
import ch.resrc.old.use_cases.common.input.*;
import ch.resrc.old.use_cases.create_game.*;
import ch.resrc.old.use_cases.finish_game.*;
import ch.resrc.old.use_cases.finish_round.ports.input.*;
import ch.resrc.old.use_cases.reset_rank_of_player.ports.input.*;
import ch.resrc.old.use_cases.update_a_team_name.ports.input.*;
import ch.resrc.old.use_cases.update_card_points_of_round.ports.input.*;
import ch.resrc.old.use_cases.update_rank_of_player.ports.input.*;
import ch.resrc.old.use_cases.update_round.ports.input.*;
import io.vavr.collection.*;
import io.vavr.control.*;
import org.slf4j.*;

import javax.websocket.*;
import javax.websocket.server.*;
import java.util.concurrent.atomic.*;

import static ch.resrc.old.domain.validation.DomainObjectInput.*;

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
    return UpdateFirstPlayerOfTeamInput.anUpdateFirstPlayerOfTeamInput()
      .withGameId(parse(Id.class, dto.gameId))
      .withTeamId(parse(Id.class, dto.teamId))
      .withUserId(parse(Id.class, dto.userId))
      .withPlayerName(parse(Name.class, dto.playerName))
      .buildResult();
  }

  private Either<Seq<ValidationError>, UpdateSecondPlayerOfTeamInput> validateUseCaseInput(IntendedSecondPlayerAdditionDto dto) {
    return UpdateSecondPlayerOfTeamInput.anUpdateSecondPlayerOfTeamInput()
      .withGameId(parse(Id.class, dto.gameId))
      .withTeamId(parse(Id.class, dto.teamId))
      .withUserId(parse(Id.class, dto.userId))
      .withPlayerName(parse(Name.class, dto.playerName))
      .buildResult();
  }

  private Either<Seq<ValidationError>, FinishGameInput> validateUseCaseInput(IntendedGameFinishDto dto) {
    return FinishGameInput.aFinishGameInput()
      .withGameId(parse(Id.class, dto.gameId()))
      .buildResult();
  }

  private Either<Seq<ValidationError>, CreateGameInput> validateUseCaseInput(IntendedGameDto dto) {
    return CreateGameInput.aCreateGameInput()
      .withCreatedBy(parse(Id.class, dto.createdBy))
      .buildResult();
  }

  private Either<Seq<ValidationError>, IntendedRoundFinish> validatedIntendedRoundFinish(IntendedRoundFinishDto dto) {
    return IntendedRoundFinish.anIntendedRoundFinish()
      .withGameId(parse(Id.class, dto.gameId))
      .withRoundNumber(parse(RoundNumber.class, dto.roundNumber))
      .buildResult();
  }

  private Either<Seq<ValidationError>, IntendedPlayerRemoval> validateIntendedPlayerRemoval(IntendedFirstPlayerRemovalDto dto) {
    return IntendedPlayerRemoval.anIntendedPlayerRemoval()
      .withGameId(parse(Id.class, dto.gameId))
      .withTeamId(parse(Id.class, dto.teamId))
      .withUserId(parse(Id.class, dto.userId))
      .withPlayerName(parse(Name.class, dto.playerName))
      .buildResult();
  }

  private Either<Seq<ValidationError>, IntendedPlayerRemoval> validateIntendedPlayerRemoval(IntendedSecondPlayerRemovalDto dto) {
    return IntendedPlayerRemoval.anIntendedPlayerRemoval()
      .withGameId(parse(Id.class, dto.gameId))
      .withTeamId(parse(Id.class, dto.teamId))
      .withUserId(parse(Id.class, dto.userId))
      .withPlayerName(parse(Name.class, dto.playerName))
      .buildResult();
  }

  private Either<Seq<ValidationError>, IntendedPlayerRankReset> validatedIntendedPlayerRankReset(IntendedPlayerRankResetDto dto) {
    return IntendedPlayerRankReset.anIntendedPlayerRankReset()
      .withGameId(parse(Id.class, dto.gameId))
      .withPlayerId(parse(Id.class, dto.playerId))
      .withRoundNumber(parse(RoundNumber.class, dto.roundNumber))
      .buildResult();
  }

  private Either<Seq<ValidationError>, IntendedTeamName> validatedIntendedTeamName(IntendedTeamNameDto dto) {
    return IntendedTeamName.anIntendedTeamName()
      .withGameId(parse(Id.class, dto.gameId))
      .withTeamId(parse(Id.class, dto.teamId))
      .withTeamName(parse(Name.class, dto.teamName))
      .buildResult();
  }

  private Either<Seq<ValidationError>, IntendedCardPointsUpdate> validatedIntendedTeamName(IntendedCardPointsUpdateDto dto) {
    return IntendedCardPointsUpdate.anIntendedCardPointsUpdate()
      .withGameId(parse(Id.class, dto.gameId))
      .withTeamId(parse(Id.class, dto.teamId))
      .withRoundNumber(parse(RoundNumber.class, dto.roundNumber))
      .withCardPoints(parse(Integer.class, dto.cardPoints))
      .buildResult();
  }

  private Either<Seq<ValidationError>, IntendedPlayerRankUpdate> validatedIntendedPlayerRankUpdate(IntendedPlayerRankUpdateDto dto) {
    return IntendedPlayerRankUpdate.anIntendedPlayerRankUpdate()
      .withGameId(parse(Id.class, dto.gameId))
      .withPlayerId(parse(Id.class, dto.playerId))
      .withRoundNumber(parse(RoundNumber.class, dto.roundNumber))
      .buildResult();
  }

  private Either<Seq<ValidationError>, IntendedRoundUpdate> validatedIntendedRoundUpdate(IntendedRoundFinishDto dto) {
    return IntendedRoundUpdate.anIntendedRoundUpdate()
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

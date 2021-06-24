import {
  CreateGameEvent,
  FinishGameEvent,
  FinishRoundEvent,
  Game,
  ResetRankEvent,
  UpdateCardPointsEvent,
  UpdateRankEvent,
  UpdateRoundEvent,
} from '@/domain/entities/game';
import { Player } from '@/domain/entities/player';
import {
  AddPlayerEvent,
  RemoveFirstPlayerEvent,
  RemoveSecondPlayerEvent,
  Team,
  UpdateTeamNameEvent,
} from '@/domain/entities/team';
import { IntendedUserDto, User } from '@/domain/entities/user';
import {
  AddFirstPlayerToTeam,
  AddSecondPlayerToTeam,
  CreateGame,
  FindOrCreateUser,
  FinishGame,
  FinishRound,
  RemoveFirstPlayerFromTeam,
  RemoveSecondPlayerFromTeam,
  ResetRankOfPlayer,
  UpdateCardPointsOfRound,
  UpdateRankOfPlayer,
  UpdateRound,
  UpdateTeamName,
} from '@/endpoints/endpoints';
import { gameState, userState } from '@/store';
import { isNone, isSome } from 'fp-ts/Option';
import { Observable, Subject } from 'rxjs';

const FAKE_USER = User.of(
  'FAKE USER',
  'tichu.master@gmail.com',
  'Master',
  '2020-12-31 06:23:17',
);

const FAKE_GAME = Game.of(
  '3466b1bc-821c-42f6-9f83-1d29cb58699e',
  FAKE_USER,
  '5f52cd8c',
  [
    Team.of(
      'FAKE 1',
      'FAKE 1',
      null,
      null,
    ),
    Team.of(
      'FAKE 2',
      'FAKE 2',
      null,
      null,
    ),
  ],
  [],
  null,
);

export class FindOrCreateUserFake implements FindOrCreateUser {

  send(intendedUser: IntendedUserDto): Promise<User> {
    return Promise.resolve(FAKE_USER);
  }
}

export class CreateGameFake implements CreateGame {
  send(intent: CreateGameEvent): Observable<Game> {
    const fakeEndpoint = new Subject<Game>();
    setTimeout(() => {
        fakeEndpoint.next(FAKE_GAME);
      },
      50,
    );
    return fakeEndpoint.asObservable();
  }
}

export class UpdateTeamNameFake implements UpdateTeamName {
  send(intent: UpdateTeamNameEvent): Observable<Game> {
    if (isNone(gameState.game)) {
      throw Error('Invariant violated: cannot update team name of non existing game');
    }
    const fakeEndpoint = new Subject<Game>();
    const updatedTeam = gameState.game.value.teamById(intent.teamId)?.butName(intent.teamName);
    if (!updatedTeam) {
      throw Error('Invariant violated: Cannot update name of unknown team');
    }
    const updatedGame = Game.copy(gameState.game.value).butTeam(updatedTeam);
    setTimeout(() => fakeEndpoint.next(updatedGame), 50);
    return fakeEndpoint.asObservable();
  }
}

function createFakePlayer(intent: AddPlayerEvent): Player {
  if (intent.userId && isSome(userState.user)) {
    return Player.of(intent.userId, userState.user.value.name);
  } else {
    const randomId = Math.floor(
      Math.random() * Math.floor(10000),
    ).toString();
    return Player.of(
      'FAKE PLAYER ' + randomId,
      intent.playerName,
    );
  }
}

export class AddFirstPlayerToTeamFake implements AddFirstPlayerToTeam {
  send(intent: AddPlayerEvent): Observable<Game> {
    if (isNone(gameState.game)) {
      throw Error('Invariant violated: cannot update team name of non existing game');
    }
    const fakeEndpoint = new Subject<Game>();
    const updatedTeam = gameState.game.value.teamById(intent.teamId)?.butFirstPlayer(createFakePlayer(intent));
    if (!updatedTeam) {
      throw Error('Invariant violated: Cannot add player to unknown team');
    }
    const updatedGame = Game.copy(gameState.game.value).butTeam(updatedTeam);
    setTimeout(() => fakeEndpoint.next(updatedGame), 50);
    return fakeEndpoint.asObservable();
  }
}

export class RemoveFirstPlayerFromTeamFake implements RemoveFirstPlayerFromTeam {
  send(intent: RemoveFirstPlayerEvent): Observable<Game> {
    if (isNone(gameState.game)) {
      throw Error('Invariant violated: cannot update team name of non existing game');
    }
    const fakeEndpoint = new Subject<Game>();
    const updatedTeam = gameState.game.value.teamById(intent.teamId)?.butFirstPlayer(null);
    if (!updatedTeam) {
      throw Error('Invariant violated: Cannot add player to unknown team');
    }
    const updatedGame = Game.copy(gameState.game.value).butTeam(updatedTeam);
    setTimeout(() => fakeEndpoint.next(updatedGame), 50);
    return fakeEndpoint.asObservable();
  }
}

export class AddSecondPlayerToTeamFake implements AddSecondPlayerToTeam {
  send(intent: AddPlayerEvent): Observable<Game> {
    if (isNone(gameState.game)) {
      throw Error('Invariant violated: cannot update team name of non existing game');
    }
    const fakeEndpoint = new Subject<Game>();
    const updatedTeam = gameState.game.value.teamById(intent.teamId)?.butSecondPlayer(createFakePlayer(intent));
    if (!updatedTeam) {
      throw Error('Invariant violated: Cannot add player to unknown team');
    }
    const updatedGame = Game.copy(gameState.game.value).butTeam(updatedTeam);
    setTimeout(() => fakeEndpoint.next(updatedGame), 50);
    return fakeEndpoint.asObservable();
  }
}

export class RemoveSecondPlayerFromTeamFake implements RemoveSecondPlayerFromTeam {
  send(intent: RemoveSecondPlayerEvent): Observable<Game> {
    if (isNone(gameState.game)) {
      throw Error('Invariant violated: cannot update team name of non existing game');
    }
    const fakeEndpoint = new Subject<Game>();
    const updatedTeam = gameState.game.value.teamById(intent.teamId)?.butSecondPlayer(null);
    if (!updatedTeam) {
      throw Error('Invariant violated: Cannot add player to unknown team');
    }
    const updatedGame = Game.copy(gameState.game.value).butTeam(updatedTeam);
    setTimeout(() => fakeEndpoint.next(updatedGame), 50);
    return fakeEndpoint.asObservable();
  }
}

export class FinishRoundFake implements FinishRound {
  send(intent: FinishRoundEvent): Observable<Game> {
    if (isNone(gameState.game)) {
      throw Error('Invariant violated: cannot update team name of non existing game');
    }
    const fakeEndpoint = new Subject<Game>();
    let updatedGame = gameState.game.value.finishRound(intent.roundNumber);
    if (!updatedGame.isComplete()) {
      const nextRoundNumber = intent.roundNumber + 1;
      updatedGame = updatedGame.createRound(nextRoundNumber);
    }
    setTimeout(() => fakeEndpoint.next(updatedGame), 50);
    setTimeout(() => fakeEndpoint.complete(), 50);
    return fakeEndpoint.asObservable();
  }
}

export class UpdateRoundFake implements UpdateRound {
  send(intent: UpdateRoundEvent): Observable<Game> {
    if (isNone(gameState.game)) {
      throw Error('Invariant violated: cannot update team name of non existing game');
    }
    const fakeEndpoint = new Subject<Game>();
    const updatedGame = gameState.game.value.finishRound(intent.roundNumber);
    setTimeout(() => fakeEndpoint.next(updatedGame), 50);
    setTimeout(() => fakeEndpoint.complete(), 50);
    return fakeEndpoint.asObservable();
  }
}

export class UpdateRankOfPlayerFake implements UpdateRankOfPlayer {
  send(intent: UpdateRankEvent): Observable<Game> {
    if (isNone(gameState.game)) {
      throw Error('Invariant violated: cannot update team name of non existing game');
    }
    const fakeEndpoint = new Subject<Game>();
    const round = gameState.game.value.rounds.find(round => round.roundNumber === intent.roundNumber);
    if (!round) {
      throw Error('Invariant violated: Round cannot be undefined');
    }
    const updatedGame = gameState.game.value.butRound(round.butRank(intent.playerId, round.nextRank()));
    setTimeout(() => fakeEndpoint.next(updatedGame), 50);
    return fakeEndpoint.asObservable();
  }
}

export class ResetRankOfPlayerFake implements ResetRankOfPlayer {
  send(intent: ResetRankEvent): Observable<Game> {
    if (isNone(gameState.game)) {
      throw Error('Invariant violated: cannot update team name of non existing game');
    }
    const fakeEndpoint = new Subject<Game>();
    const round = gameState.game.value.rounds.find(round => round.roundNumber === intent.roundNumber);
    if (!round) {
      throw Error('Invariant violated: Round cannot be undefined');
    }
    const updatedGame = gameState.game.value.butRound(round.resetRank(intent.playerId));
    setTimeout(() => fakeEndpoint.next(updatedGame), 50);
    return fakeEndpoint.asObservable();
  }
}

export class UpdateCardPointsOfRoundFake implements UpdateCardPointsOfRound {
  send(intent: UpdateCardPointsEvent): Observable<Game> {
    if (isNone(gameState.game)) {
      throw Error('Invariant violated: cannot update team name of non existing game');
    }
    const fakeEndpoint = new Subject<Game>();
    const leftTeamId = gameState.game.value.leftTeam.id;
    const rightTeamId = gameState.game.value.rightTeam.id;
    let validPointsOfLeftTeam;
    let validPointsOfRightTeam;
    if (gameState.game.value.isLeftTeam(intent.teamId)) {
      validPointsOfLeftTeam = UpdateCardPointsOfRoundFake.mapToValidRange(intent.cardPoints);
      validPointsOfRightTeam = 100 - validPointsOfLeftTeam;
    } else {
      validPointsOfRightTeam = UpdateCardPointsOfRoundFake.mapToValidRange(intent.cardPoints);
      validPointsOfLeftTeam = 100 - validPointsOfRightTeam;
    }
    const updatedGame = gameState.game.value
      .updateCardPointsOfTeam(leftTeamId, intent.roundNumber, validPointsOfLeftTeam)
      .updateCardPointsOfTeam(rightTeamId, intent.roundNumber, validPointsOfRightTeam);
    setTimeout(() => fakeEndpoint.next(updatedGame), 50);
    return fakeEndpoint.asObservable();
  }

  private static mapToValidRange(cardPoints: number): number {
    if (cardPoints > 100) {
      return 100;
    } else if (cardPoints < -25) {
      return -25;
    } else {
      return cardPoints as number;
    }
  }
}

export class FinishGameFake implements FinishGame {
  send(intent: FinishGameEvent): Observable<Game> {
    if (isNone(gameState.game)) {
      throw Error('Invariant violated: cannot update team name of non existing game');
    }
    const fakeEndpoint = new Subject<Game>();
    const updatedGame = gameState.game.value.butFinishedAt(new Date());
    setTimeout(() => fakeEndpoint.next(updatedGame), 50);
    return fakeEndpoint.asObservable();
  }
}

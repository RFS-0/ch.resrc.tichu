import {
  CreateGameEvent,
  FinishGameEvent,
  FinishRoundEvent,
  Game,
  GameDto,
  ResetRankEvent,
  UpdateCardPointsEvent,
  UpdateRankEvent,
  UpdateRoundEvent,
} from '@/domain/entities/game';
import { IntendedUserDto, User } from '@/domain/entities/user';
import { Observable } from 'rxjs';
import {
  AddPlayerEvent,
  RemoveFirstPlayerEvent,
  RemoveSecondPlayerEvent,
  UpdateTeamNameEvent,
} from '@/domain/entities/team';

export interface FindOrCreateUser {
  send: (intent: IntendedUserDto) => Promise<User>;
}

export interface CreateGame {
  send: (intent: CreateGameEvent) => Observable<GameDto>;
}

export interface UpdateTeamName {
  send: (intent: UpdateTeamNameEvent) => Observable<Game>
}

export interface AddFirstPlayerToTeam {
  send: (intent: AddPlayerEvent) => Observable<Game>
}

export interface RemoveFirstPlayerFromTeam {
  send: (intent: RemoveFirstPlayerEvent) => Observable<Game>
}

export interface AddSecondPlayerToTeam {
  send: (intent: AddPlayerEvent) => Observable<Game>
}

export interface RemoveSecondPlayerFromTeam {
  send: (intent: RemoveSecondPlayerEvent) => Observable<Game>
}

export interface UpdateRankOfPlayer {
  send: (intent: UpdateRankEvent) => Observable<Game>
}

export interface ResetRankOfPlayer {
  send: (intent: ResetRankEvent) => Observable<Game>
}

export interface UpdateCardPointsOfRound {
  send: (intent: UpdateCardPointsEvent) => Observable<Game>
}

export interface FinishRound {
  send: (intent: FinishRoundEvent) => Observable<Game>
}

export interface UpdateRound {
  send: (intent: UpdateRoundEvent) => Observable<Game>
}

export interface FinishGame {
  send: (intent: FinishGameEvent) => Observable<Game>
}


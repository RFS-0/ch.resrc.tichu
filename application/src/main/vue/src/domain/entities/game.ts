import { Player } from '@/domain/entities/player';
import { Team, TeamDto } from '@/domain/entities/team';
import { User } from '@/domain/entities/user';
import { Rank } from '@/domain/value_objects/rank';
import { Round, RoundDto } from '@/domain/value_objects/round';

export class Game {

  constructor(
    private readonly _id: string,
    private readonly _createdBy: User,
    private readonly _joinCode: string,
    private _teams: Team[],
    private _rounds: Round[],
    private _finishedAt: Date | null,
  ) {
  }

  static of(id: string, createdBy: User, joinCode: string, teams: Team[], rounds: Round[], finishedAt: Date | null) {
    return new Game(id, createdBy, joinCode, teams, rounds, finishedAt);
  }

  static fromDto(dto: GameDto) {
    return Game.of(dto.id, dto.createdBy, dto.joinCode, Team.fromDtos(dto.teams), Round.fromDtos(dto.rounds), dto.finishedAt);
  }

  static fromJson(json: string): Game {
    const dto = JSON.parse(json) as GameDto;
    return Game.fromDto(dto);
  }

  static copy(other: Game) {
    return new Game(
      other._id,
      other._createdBy,
      other._joinCode,
      other._teams.map(team => Team.copy(team)),
      other._rounds.map(round => Round.copy(round)),
      other._finishedAt ? new Date(other._finishedAt.getDate()) : null,
    );
  }

  butTeam(updated: Team): Game {
    const copy = Game.copy(this);
    const existing = this._teams.find(team => team.id === updated.id);
    if (!existing) {
      throw Error('Invariant violated: Cannot update non existing team');
    }
    const index = this.teams.indexOf(existing);
    copy.teams.splice(index, 1, updated);
    return copy;
  }

  butRounds(rounds: Round[]): Game {
    const copy = Game.copy(this);
    copy._rounds = rounds;
    return copy;
  }

  butRound(updated: Round): Game {
    const copy = Game.copy(this);
    const existing = this.rounds.find(round => round.roundNumber === updated.roundNumber);
    if (!existing) {
      throw Error('Invariant violated: Cannot update non existing round');
    }
    const index = this.rounds.indexOf(existing);
    copy.rounds.splice(index, 1, updated);
    return copy;
  }

  butFinishedAt(finishedAt: Date): Game {
    const copy = Game.copy(this);
    copy._finishedAt = finishedAt;
    return copy;
  }

  updateCardPointsOfTeam(teamId: string, roundNumber: number, cardPoints: number): Game {
    const copy = Game.copy(this);
    const round = copy.rounds.find(
      round => round.roundNumber === roundNumber,
    );
    if (!round) {
      throw new Error('Invariant violated: Round cannot be undefined');
    }
    return copy.butRound(round.butCardPoints(teamId, cardPoints));
  }

  cardPointsOfTeam(roundNumber: number, teamId: string): number {
    const round = this.rounds.find(
      round => round.roundNumber === roundNumber,
    );
    if (!round) {
      throw new Error('Invariant violated: Round cannot be undefined');
    }
    const cardPoints = round.cardPoints.get(teamId);
    if (cardPoints === undefined) {
      throw new Error('Invariant violated: Card points cannot be undefined');
    }
    return cardPoints;
  }

  createRound(roundNumber: number): Game {
    const newRound = Round.create(
      roundNumber,
      this.teams.map(team => team.id),
      this.players.map(player => player.id),
    );
    const updatedRounds = this.rounds.map(round => Round.copy(round));
    updatedRounds.push(newRound);
    return this.butRounds(updatedRounds);
  }

  currentRound(): Round {
    if (this.rounds.length < 1) {
      throw new Error('Invariant violated: at least the first round has to exist');
    }
    return this.rounds.reduce((prev, current) => (prev.roundNumber > current.roundNumber) ? prev : current);
  }

  finishRound(roundNumber: number): Game {
    const round = this.rounds.find(round => round.roundNumber === roundNumber);
    if (!round) {
      throw new Error('Invariant violated: cannot finish non existing round');
    }
    return this.butRound(round.finishRound());
  }

  nextRank(): Rank {
    return this.currentRound().nextRank();
  }

  isLeftTeam(teamId: string) {
    return this.teams[0].id === teamId;
  }

  teamById(teamId: string): Team {
    const team = this.teams.find(team => team.id === teamId);
    if (!team) {
      throw new Error('Invariant violated: Cannot find team');
    }
    return team;
  }

  playerById(playerId: string): Player {
    const player = this.players.find(player => player.id === playerId);
    if (!player) {
      throw new Error('Invariant violated: Cannot find player');
    }
    return player;
  }

  findPlayerById(playerId: string): Player | undefined {
    return this.players.find(player => player.id === playerId);
  }

  totalPointsOfTeam(teamId: string): number {
    const firstPlayerId = this.teamById(teamId).firstPlayer?.id;
    const secondPlayerId = this.teamById(teamId).secondPlayer?.id;
    if (!firstPlayerId || !secondPlayerId) {
      throw new Error('Invariant violated: Player id cannot be undefined');
    }
    return this.rounds
      .map(round => round.totalPoints(teamId, firstPlayerId, secondPlayerId))
      .reduce((prev, current) => prev + current) || 0;
  }

  totalPointsOfRound(roundNumber: number, teamId: string, firstPlayerId: string, secondPlayerId: string): number {
    return this.rounds
      .filter(round => round.roundNumber === roundNumber)
      .map(round => round.totalPoints(teamId, firstPlayerId, secondPlayerId))
      .reduce((prev, current) => prev + current) || 0;
  }

  totalPointsUpToRound(roundNumber: number, teamId: string, firstPlayerId: string, secondPlayerId: string): number {
    return this.rounds
      .filter(round => round.roundNumber <= roundNumber)
      .map(round => round.totalPoints(teamId, firstPlayerId, secondPlayerId))
      .reduce((prev, current) => prev + current) || 0;
  }

  isComplete() {
    const totalPointsLeftTeam = this.totalPointsOfTeam(this.leftTeam.id);
    const totalPointsRightTeam = this.totalPointsOfTeam(this.rightTeam.id);
    return totalPointsLeftTeam >= 1000 || totalPointsRightTeam >= 1000;
  }

  get id(): string {
    return this._id;
  }

  get createdBy(): User {
    return this._createdBy;
  }

  get joinCode(): string {
    return this._joinCode;
  }

  get teams(): Team[] {
    return this._teams;
  }

  get leftTeam(): Team {
    return this._teams[0];
  }

  get rightTeam(): Team {
    return this._teams[1];
  }

  get players(): Player[] {
    return this.teams[0].players().concat(this.teams[1].players());
  }

  get rounds(): Round[] {
    return this._rounds;
  }

  get finishedAt(): Date | null {
    return this._finishedAt;
  }
}

export interface GameDto {
  readonly id: string,
  readonly createdBy: User,
  readonly joinCode: string,
  readonly teams: TeamDto[],
  readonly rounds: RoundDto[],
  readonly finishedAt: Date | null,
}

export class CreateGameEvent {
  private constructor(public createdBy: string) {
  }

  static of(userId: string): CreateGameEvent {
    return new CreateGameEvent(userId);
  }

  toDto(): string {
    return JSON.stringify(this);
  }
}

export class UpdateRankEvent {
  private constructor(
    public gameId: string,
    public playerId: string,
    public roundNumber: number) {
  }

  static of(gameId: string, playerId: string, roundNumber: number): UpdateRankEvent {
    return new UpdateRankEvent(gameId, playerId, roundNumber);
  }
}

export class ResetRankEvent {
  private constructor(
    public gameId: string,
    public playerId: string,
    public roundNumber: number) {
  }

  static of(gameId: string, playerId: string, roundNumber: number): ResetRankEvent {
    return new ResetRankEvent(gameId, playerId, roundNumber);
  }
}

export class UpdateCardPointsEvent {
  private constructor(
    public gameId: string,
    public teamId: string,
    public roundNumber: number,
    public cardPoints: number) {
  }

  static of(gameId: string, teamId: string, roundNumber: number, cardPoints: number): UpdateCardPointsEvent {
    return new UpdateCardPointsEvent(gameId, teamId, roundNumber, cardPoints);
  }
}

export class FinishRoundEvent {
  private constructor(public gameId: string, public roundNumber: number) {
  }

  static of(gameId: string, roundNumber: number): FinishRoundEvent {
    return new FinishRoundEvent(gameId, roundNumber);
  }
}

export class UpdateRoundEvent {
  private constructor(public gameId: string, public roundNumber: number) {
  }

  static of(gameId: string, roundNumber: number): UpdateRoundEvent {
    return new UpdateRoundEvent(gameId, roundNumber);
  }
}

export class FinishGameEvent {
  private constructor(public gameId: string) {
  }

  static of(gameId: string): FinishGameEvent {
    return new FinishGameEvent(gameId);
  }
}

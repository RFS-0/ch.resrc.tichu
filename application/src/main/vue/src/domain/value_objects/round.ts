import { Rank, rankOf } from '@/domain/value_objects/rank';
import { Tichu } from '@/domain/value_objects/tichu';

export class Round {

  private constructor(
    private readonly _roundNumber: number,
    private _cardPoints: Map<string, number>,
    private _ranks: Map<string, Rank>,
    private _tichus: Map<string, Tichu>,
  ) {
  }

  static create(roundNumber: number, teamIds: string[], playerIds: string[]): Round {
    return new Round(
      roundNumber,
      new Map(teamIds.map(id => [id, 0])),
      new Map(playerIds.map(id => [id, Rank.NONE])),
      new Map(playerIds.map(id => [id, Tichu.NONE])),
    );
  }

  static of(roundNumber: number, cardPoints: Map<string, number>, ranks: Map<string, Rank>, tichus: Map<string, Tichu>) {
    return new Round(roundNumber, cardPoints, ranks, tichus);
  }

  static fromDto(dto: RoundDto) {
    return this.of(dto.roundNumber, dto.cardPoints, dto.ranks, dto.tichus);
  }

  static fromDtos(dtos: RoundDto[]) {
    return dtos.map(this.fromDto);
  }

  static copy(other: Round) {
    return new Round(
      other._roundNumber,
      new Map(other._cardPoints),
      new Map(other._ranks),
      new Map(other._tichus),
    );
  }

  butRank(playerId: string, rank: Rank) {
    const copy = Round.copy(this);
    copy._ranks.set(playerId, rank);
    return copy;
  }

  butCardPoints(teamId: string, cardPoints: number) {
    const copy = Round.copy(this);
    copy._cardPoints.set(teamId, cardPoints);
    return copy;
  }

  butRanks(ranks: Map<string, Rank>) {
    const copy = Round.copy(this);
    copy._ranks = ranks;
    return copy;
  }

  butTichus(tichus: Map<string, Tichu>) {
    const copy = Round.copy(this);
    copy._tichus = tichus;
    return copy;
  }

  roundNumberIsEven() {
    return this._roundNumber % 2 === 0;
  }

  nextRank(): Rank {
    const playerRanks = [...this.ranks.values()];
    if (playerRanks.includes(Rank.THIRD)) {
      return Rank.FOURTH;
    } else if (playerRanks.includes(Rank.SECOND)) {
      return Rank.THIRD;
    } else if (playerRanks.includes(Rank.FIRST)) {
      return Rank.SECOND;
    } else {
      return Rank.FIRST;
    }
  }

  resetRank(playerId: string): Round {
    const updatedRanks = new Map(this.ranks);
    const rankOfPlayer = updatedRanks.get(playerId) || Rank.NONE;
    updatedRanks.forEach((rank, key) => {
      if (rank !== Rank.NONE && rank.valueOf() > rankOfPlayer.valueOf()) {
        updatedRanks.set(key, rankOf(rank.valueOf() - 1));
      }
    });
    updatedRanks.set(playerId, Rank.NONE);
    return Round.copy(this).butRanks(updatedRanks);
  }

  toggleTichu(playerId: string): Round {
    const updatedTichus = new Map(this.tichus);
    const tichuOfPlayer = this.tichus.get(playerId);
    if (tichuOfPlayer === undefined) {
      throw new Error('Invariant violated: Tichu cannot be undefined');
    }
    if (tichuOfPlayer.valueOf() === Tichu.NONE.valueOf()) {
      updatedTichus.set(playerId, Tichu.TICHU_CALLED);
    } else if (tichuOfPlayer.valueOf() === Tichu.TICHU_CALLED.valueOf()) {
      updatedTichus.set(playerId, Tichu.GRAND_TICHU_CALLED);
    } else {
      updatedTichus.set(playerId, Tichu.NONE);
    }
    return Round.copy(this).butTichus(updatedTichus);
  }

  isComplete(): boolean {
    const allPlayersHaveARank = ![...this.ranks.values()]
      .map(rank => rank.valueOf())
      .includes(Rank.NONE.valueOf());
    const cardPointsSpecified = [...this.cardPoints.values()]
      .map(card => card.valueOf())
      .reduce((prev, next) => prev + next) === 100;
    return allPlayersHaveARank && cardPointsSpecified;
  }

  finishRound(): Round {
    const updatedTichus = this.evaluateSuccessOfTichus();
    return Round.copy(this).butTichus(updatedTichus);
  }

  private evaluateSuccessOfTichus(): Map<string, Tichu> {
    const updatedTichus = new Map(this.tichus);
    this.tichus.forEach((tichu: Tichu, playerId: string) => {
      if (tichu.valueOf() !== Tichu.NONE.valueOf()) {
        const rank = this.ranks.get(playerId);
        if (!rank) {
          throw new Error('Invariant violated: Rank cannot be undefined');
        }
        const tichuOfPlayer = updatedTichus.get(playerId);
        if (tichuOfPlayer === undefined) {
          throw new Error('Invariant violated: Tichu cannot be undefined');
        }
        if (rank.valueOf() !== Rank.FIRST.valueOf()) {
          if (tichuOfPlayer.valueOf() === Tichu.TICHU_CALLED.valueOf()) {
            updatedTichus.set(playerId, Tichu.TICHU_FAILED);
          } else {
            updatedTichus.set(playerId, Tichu.GRAND_TICHU_FAILED);
          }
        } else {
          if (tichuOfPlayer.valueOf() === Tichu.TICHU_CALLED.valueOf()) {
            updatedTichus.set(playerId, Tichu.TICHU_SUCCEEDED);
          } else {
            updatedTichus.set(playerId, Tichu.GRAND_TICHU_SUCCEEDED);
          }
        }
      }
    });
    return updatedTichus;
  }

  totalPoints(teamId: string, firstPlayerId: string, secondPlayerId: string): number {
    return this.cardPointsOfTeam(teamId)
      + this.matchPoints(firstPlayerId, secondPlayerId)
      + this.tichuPointsOfPlayer(firstPlayerId)
      + this.tichuPointsOfPlayer(secondPlayerId);
  }

  cardPointsOfTeam(teamId: string): number {
    const cardPoints = this.cardPoints.get(teamId);
    if (cardPoints === undefined) {
      throw new Error('Invariant violated: Card points cannot be undefined');
    }
    return cardPoints;
  }

  isMatch(firstPlayerId: string, secondPlayerId: string): boolean {
    const firstPlayerRank = this.ranks.get(firstPlayerId);
    const secondPlayerRank = this.ranks.get(secondPlayerId);
    if (firstPlayerRank === undefined || secondPlayerRank === undefined) {
      throw new Error('Invariant violated: Rank cannot be undefined');
    }
    const ranks = [firstPlayerRank.valueOf(), secondPlayerRank.valueOf()];
    return ranks.includes(Rank.FIRST.valueOf()) && ranks.includes(Rank.SECOND.valueOf());
  }

  private matchPoints(firstPlayerId: string, secondPlayerId: string): number {
    return this.isMatch(firstPlayerId, secondPlayerId) ? 100 : 0;
  }

  private tichuPointsOfPlayer(playerId: string): number {
    const tichu = this.tichus.get(playerId);
    if (tichu === undefined) {
      throw new Error('Invariant violated: Tichu cannot be undefined');
    }
    if (Math.abs(tichu.valueOf()) < 100) {
      return 0;
    } else {
      return tichu.valueOf();
    }
  }

  get roundNumber(): number {
    return this._roundNumber;
  }

  get cardPoints(): Map<string, number> {
    return this._cardPoints;
  }

  get ranks(): Map<string, Rank> {
    return this._ranks;
  }

  get tichus(): Map<string, Tichu> {
    return this._tichus;
  }
}

export interface RoundDto {
  readonly  roundNumber: number,
  readonly cardPoints: Map<string, number>,
  readonly ranks: Map<string, Rank>,
  readonly tichus: Map<string, Tichu>,
}

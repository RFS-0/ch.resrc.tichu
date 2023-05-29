import {Rank, rankOf} from './rank';
import {Tichu} from './tichu';
import {Entity, type RawEntity} from '../entities';
import {implement} from '../validation';
import {z} from 'zod';
import {PlayerId} from './player-id';
import {TeamId} from './team-id';

export interface RawRound extends RawEntity {
    roundNumber: number,
    cardPoints: Map<string, number>,
    ranks: Map<string, Rank>,
    tichus: Map<string, Tichu>,
}

export const RoundSchema = implement<RawRound>().with({
    roundNumber: z.number(),
    cardPoints: z.map(z.string(), z.number()),
    ranks: z.map(z.string(), z.nativeEnum(Rank)),
    tichus: z.map(z.string(), z.nativeEnum(Tichu))
});

export class Round extends Entity {

    private _roundNumber: number;
    private _cardPoints: Map<string, number>;
    private _ranks: Map<string, Rank>;
    private _tichus: Map<string, Tichu>;

    constructor(
        input: RawRound,
    ) {
        super(input);
        const result = RoundSchema.safeParse(input);
        if (!result.success) {
            throw new Error(`Preconditions to create ${Round.name} not met because ${result.error.message}`)
        }
        this._roundNumber = result.data.roundNumber;
        this._cardPoints = result.data.cardPoints;
        this._ranks = result.data.ranks;
        this._tichus = result.data.tichus;
    }

    static create(roundNumber: number, teamIds: string[], playerIds: string[]): Round {
        return new Round({
                roundNumber,
                cardPoints: new Map(teamIds.map(id => [id, 0])),
                ranks: new Map(playerIds.map(id => [id, Rank.NONE])),
                tichus: new Map(playerIds.map(id => [id, Tichu.NONE])),
            }
        );
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
        const rankOfPlayer = this._ranks.get(playerId) || Rank.NONE;
        this._ranks.forEach((rank, key) => {
            if (rank !== Rank.NONE && rank.valueOf() > rankOfPlayer.valueOf()) {
                updatedRanks.set(key, rankOf(rank.valueOf() - 1));
            }
        });
        updatedRanks.set(playerId, Rank.NONE);
        this._ranks = updatedRanks;
        return this;
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
        this._tichus = updatedTichus;
        return this;
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
        this._tichus = this.evaluateSuccessOfTichus();
        return this;
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

    totalPoints(teamId: TeamId, firstPlayerId: PlayerId, secondPlayerId: PlayerId): number {
        return this.cardPointsOfTeam(teamId)
            + this.matchPoints(firstPlayerId, secondPlayerId)
            + this.tichuPointsOfPlayer(firstPlayerId)
            + this.tichuPointsOfPlayer(secondPlayerId);
    }

    cardPointsOfTeam(teamId: TeamId): number {
        const cardPoints = this.cardPoints.get(teamId.value);
        if (cardPoints === undefined) {
            throw new Error('Invariant violated: Card points cannot be undefined');
        }
        return cardPoints;
    }

    isMatch(firstPlayerId: PlayerId, secondPlayerId: PlayerId): boolean {
        const firstPlayerRank = this.ranks.get(firstPlayerId.value);
        const secondPlayerRank = this.ranks.get(secondPlayerId.value);
        if (firstPlayerRank === undefined || secondPlayerRank === undefined) {
            throw new Error('Invariant violated: Rank cannot be undefined');
        }
        const ranks = [firstPlayerRank.valueOf(), secondPlayerRank.valueOf()];
        return ranks.includes(Rank.FIRST.valueOf()) && ranks.includes(Rank.SECOND.valueOf());
    }

    private matchPoints(firstPlayerId: PlayerId, secondPlayerId: PlayerId): number {
        return this.isMatch(firstPlayerId, secondPlayerId) ? 100 : 0;
    }

    private tichuPointsOfPlayer(playerId: PlayerId): number {
        const tichu = this.tichus.get(playerId.value);
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

    toRaw(): RawRound {
        return {
            roundNumber: this.roundNumber,
            cardPoints: this.cardPoints,
            ranks: this.ranks,
            tichus: this.tichus
        };
    }
}

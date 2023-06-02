import {Rank, rankOf} from './rank';
import {Tichu} from './tichu';
import {implement} from '../validation';
import {z} from 'zod';
import {PlayerId} from './player-id';
import {CompositeValueObject, type RawCompositeValueObject} from './value-object';
import {Team} from './team';

export interface RawRound extends RawCompositeValueObject {
    roundNumber: number,
    cardPoints: Map<number, number>,
    ranks: Map<string, Rank>,
    tichus: Map<string, Tichu>,
}

export const RoundSchema = implement<RawRound>().with({
    roundNumber: z.number().min(1).max(100),
    cardPoints: z.map(z.number(), z.number()),
    ranks: z.map(z.string(), z.nativeEnum(Rank)),
    tichus: z.map(z.string(), z.nativeEnum(Tichu))
});

export class Round extends CompositeValueObject {

    private _roundNumber: number;
    private _cardPoints: Map<number, number>;
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

    private mutate(mutate: (round: Round) => void): Round {
        const validate = (round: Round) => {
            const result = RoundSchema.safeParse(round.toRaw());
            if (!result.success) {
                throw new Error(`Preconditions to mutate ${Round.name} not met because ${result.error.message}`)
            }
            return round;
        }
        const copy = new Round(this.toRaw());
        mutate(copy)
        return validate(copy);
    }


    static create(roundNumber: number, teams: Team[]): Round {
        return new Round({
                roundNumber,
                cardPoints: new Map([
                    [0, 0],
                    [1, 0],
                ]),
                ranks: new Map(
                    teams.flatMap(team => Array.from(team.players.values()))
                         .map(playerId => [playerId.value, Rank.NONE])
                ),
                tichus: new Map(
                    teams.flatMap(team => Array.from(team.players.values()))
                         .map(playerId => [playerId.value, Tichu.NONE])
                ),
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
        return this.mutate(round => {
            const updatedRanks = new Map(round.ranks);
            const rankOfPlayer = round._ranks.get(playerId) || Rank.NONE;
            round._ranks.forEach((rank, key) => {
                if (rank !== Rank.NONE && rank.valueOf() > rankOfPlayer.valueOf()) {
                    updatedRanks.set(key, rankOf(rank.valueOf() - 1));
                }
            });
            updatedRanks.set(playerId, Rank.NONE);
            round._ranks = updatedRanks;
        })
    }

    toggleTichu(playerId: string): Round {
        return this.mutate(round => {
            const updatedTichus = new Map(round.tichus);
            const tichuOfPlayer = round.tichus.get(playerId);
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
            round._tichus = updatedTichus;
        });
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
        return this.mutate(round => {
            round._tichus = round.evaluateSuccessOfTichus();
        });
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

    totalPoints(team: Team): number {
        const playersOfTeam = Array.from(team.players.values());
        return this.cardPointsOfTeam(team.index)
            + this.matchPoints(playersOfTeam)
            + this.tichuPointsOfPlayer(playersOfTeam[0])
            + this.tichuPointsOfPlayer(playersOfTeam[1]);
    }

    updateCardPointsOfTeam(teamIndex: number, cardPoints: number): Round {
        return this.mutate(round => {
            const sanitizeCardPointValue = (cardPoints: number): number => {
                if (cardPoints > 100) {
                    return 100;
                } else if (cardPoints < -25) {
                    return -25;
                } else {
                    return cardPoints as number;
                }
            }
            let sanitizedPointsOfLeftTeam;
            let sanitizedPointsOfRightTeam;
            if (teamIndex === 0) {
                sanitizedPointsOfLeftTeam = sanitizeCardPointValue(cardPoints);
                sanitizedPointsOfRightTeam = 100 - sanitizedPointsOfLeftTeam;
            } else {
                sanitizedPointsOfRightTeam = sanitizeCardPointValue(cardPoints);
                sanitizedPointsOfLeftTeam = 100 - sanitizedPointsOfRightTeam;
            }
            round._cardPoints.set(0, sanitizedPointsOfLeftTeam);
            round._cardPoints.set(1, sanitizedPointsOfRightTeam);
        });
    }

    rankPlayer(playerId: string, rank: Rank): Round {
       return this.mutate(round => {
           round._ranks = round._ranks.set(playerId, rank);
       })
    }

    cardPointsOfTeam(teamIndex: number): number {
        const cardPoints = this.cardPoints.get(teamIndex);
        if (cardPoints === undefined) {
            throw new Error('Invariant violated: Card points cannot be undefined');
        }
        return cardPoints;
    }

    isMatch(playerIds: PlayerId[]): boolean {
        const ranks = new Set([
            this.ranks.get(playerIds[0].value),
            this.ranks.get(playerIds[1].value)
        ]);
        return ranks.has(Rank.FIRST) && ranks.has(Rank.SECOND);
    }

    private matchPoints(playerIds: PlayerId[]): number {
        return this.isMatch(playerIds) ? 100 : 0;
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

    get cardPoints(): Map<number, number> {
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

import {
    EntityIdSchema, GameId, JoinCode, JoinCodeSchema, PlayerId, Rank, type RawRound, type RawTeam, Round, RoundSchema,
    Team, TeamSchema
} from '../value_objects';
import {Entity, EntitySchema, type RawEntity} from './entity';
import {implement} from '../validation';
import {z, type ZodTypeDef} from 'zod';

export interface RawGame extends RawEntity {
    id: string;
    createdBy: string | null;
    joinCode: string;
    teams: RawTeam[];
    rounds: RawRound[];
}

export const GameSchema: z.ZodType<RawGame, ZodTypeDef, RawGame> = implement<RawGame>()
    .extend(EntitySchema)
    .with({
        id: EntityIdSchema.shape.value,
        createdBy: EntityIdSchema.shape.value.nullable(),
        joinCode: JoinCodeSchema.shape.value,
        teams: z.array(TeamSchema).max(2),
        rounds: z.array(RoundSchema).max(100)
    });

export class Game extends Entity {

    private _id: GameId;
    private _createdBy: PlayerId | null;
    private _joinCode: JoinCode;
    private _teams: Team[];
    private _rounds: Round[];

    constructor(
        input: RawGame,
    ) {
        super(input);
        const result = GameSchema.safeParse(input);
        if (!result.success) {
            throw new Error(`Preconditions to create ${Game.name} not met because ${result.error.message}`)
        }
        this._id = new GameId({value: result.data.id});
        this._createdBy = result.data.createdBy !== null ? new PlayerId({value: result.data.createdBy}) : null;
        this._joinCode = new JoinCode({value: result.data.joinCode});
        this._teams = result.data.teams.map(team => new Team(team));
        this._rounds = result.data.rounds.map(round => new Round(round));
    }

    cardPointsOfTeam(roundNumber: number, teamIndex: number): number {
        const round = this.rounds.find(
            round => round.roundNumber === roundNumber,
        );
        if (!round) {
            throw new Error('Invariant violated: Round cannot be undefined');
        }
        const cardPoints = round.cardPoints.get(teamIndex);
        if (cardPoints === undefined) {
            throw new Error('Invariant violated: Card points cannot be undefined');
        }
        return cardPoints;
    }

    createRound(roundNumber: number): Game {
        const newRound = Round.create(
            roundNumber,
            this.teams,
        );
        this._rounds.push(newRound);
        return this;
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
        round.finishRound();
        return this;
    }

    nextRank(): Rank {
        return this.currentRound().nextRank();
    }

    totalPointsOfTeam(team: Team): number {
        return this.rounds
                   .map(round => round.totalPoints(team))
                   .reduce((prev, current) => prev + current) || 0;
    }

    totalPointsOfRound(roundNumber: number, team: Team): number {
        return this.rounds
                   .filter(round => round.roundNumber === roundNumber)
                   .map(round => round.totalPoints(team))
                   .reduce((prev, current) => prev + current) || 0;
    }

    totalPointsUpToRound(roundNumber: number, team: Team): number {
        return this.rounds
                   .filter(round => round.roundNumber <= roundNumber)
                   .map(round => round.totalPoints(team))
                   .reduce((prev, current) => prev + current) || 0;
    }

    isComplete() {
        const totalPointsLeftTeam = this.totalPointsOfTeam(this.teams[0]);
        const totalPointsRightTeam = this.totalPointsOfTeam(this.teams[1]);
        return totalPointsLeftTeam >= 1000 || totalPointsRightTeam >= 1000;
    }

    get id(): GameId {
        return this._id;
    }

    get createdBy(): PlayerId | null {
        return this._createdBy;
    }

    get joinCode(): JoinCode {
        return this._joinCode;
    }

    get teams(): Team[] {
        return this._teams;
    }

    get players(): PlayerId[] {
        return this.teams[0].playerIds.concat(this.teams[1].playerIds);
    }

    get leftTeam(): Team {
        return this._teams[0];
    }

    get rightTeam(): Team {
        return this._teams[1];
    }

    get playerIds(): PlayerId[] {
        return this.teams[0].playerIds.concat(this.teams[1].playerIds);
    }

    get rounds(): Round[] {
        return this._rounds;
    }

    toRaw(): RawGame {
        return {
            id: this.id.value,
            createdBy: this.createdBy ? this.createdBy.value : null,
            joinCode: this.joinCode.value,
            teams: this.teams.map(team => team.toRaw()),
            rounds: this.rounds.map(round => round.toRaw()),
        };
    }
}

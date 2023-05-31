import {type RawTeam, Team, TeamSchema} from './team';
import {
    EntityIdSchema, GameId, JoinCode, JoinCodeSchema, PlayerId, Rank, type RawRound, Round, RoundSchema, TeamId
} from '../value_objects';
import {Player} from './player';
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


    updateCardPointsOfTeam(teamId: string, roundNumber: number, cardPoints: number): Game {
        return this;
    }

    cardPointsOfTeam(roundNumber: number, teamId: TeamId): number {
        const round = this.rounds.find(
            round => round.roundNumber === roundNumber,
        );
        if (!round) {
            throw new Error('Invariant violated: Round cannot be undefined');
        }
        const cardPoints = round.cardPoints.get(teamId.value);
        if (cardPoints === undefined) {
            throw new Error('Invariant violated: Card points cannot be undefined');
        }
        return cardPoints;
    }

    createRound(roundNumber: number): Game {
        const newRound = Round.create(
            roundNumber,
            this.teams.map(team => team.id.value),
            this.players.map(player => player.id.value),
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

    isLeftTeam(teamId: TeamId) {
        return this.teams[0].id.value === teamId.value;
    }

    teamById(teamId: TeamId): Team {
        const team = this.teams.find(team => team.id.value === teamId.value);
        if (!team) {
            throw new Error('Invariant violated: Cannot find team');
        }
        return team;
    }

    playerById(playerId: PlayerId): Player {
        const player = this.players.find(player => player.id.value === playerId.value);
        if (!player) {
            throw new Error('Invariant violated: Cannot find player');
        }
        return player;
    }

    findPlayerById(playerId: PlayerId): Player | undefined {
        return this.players.find(player => player.id.value === playerId.value);
    }

    totalPointsOfTeam(teamId: TeamId): number {
        const firstPlayerId = this.teamById(teamId).firstPlayer.id;
        const secondPlayerId = this.teamById(teamId).secondPlayer.id;
        if (!firstPlayerId || !secondPlayerId) {
            throw new Error('Invariant violated: Player id cannot be undefined');
        }
        return this.rounds
                   .map(round => round.totalPoints(teamId, firstPlayerId, secondPlayerId))
                   .reduce((prev, current) => prev + current) || 0;
    }

    totalPointsOfRound(roundNumber: number, teamId: TeamId, firstPlayerId: PlayerId, secondPlayerId: PlayerId): number {
        return this.rounds
                   .filter(round => round.roundNumber === roundNumber)
                   .map(round => round.totalPoints(teamId, firstPlayerId, secondPlayerId))
                   .reduce((prev, current) => prev + current) || 0;
    }

    totalPointsUpToRound(roundNumber: number, teamId: TeamId, firstPlayerId: PlayerId, secondPlayerId: PlayerId): number {
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

    get leftTeam(): Team {
        return this._teams[0];
    }

    get rightTeam(): Team {
        return this._teams[1];
    }

    get players(): Player[] {
        return this.teams[0].players.concat(this.teams[1].players);
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

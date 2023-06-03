import {
    EntityIdSchema, GameId, JoinCode, JoinCodeSchema, PlayerId, Rank, type RawRound, type RawTeam, Round, RoundSchema,
    Team, TeamSchema, Tichu
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

    hasPlayerJoinedGame(playerId: PlayerId): boolean {
        return this.teams.some(team => team.hasPlayer(playerId));
    }

    private mutate(mutate: (game: Game) => void): Game {
        const validate = (game: Game) => {
            const result = GameSchema.safeParse(game.toRaw());
            if (!result.success) {
                throw new Error(`Preconditions to mutate ${Game.name} not met because ${result.error.message}`)
            }
            return game;
        }
        const copy = new Game(this.toRaw());
        mutate(copy)
        return validate(copy);
    }

    addPlayerToTeam(teamIndex: number, playerIndex: number, playerId: PlayerId) {
        return this.mutate(game => {
            const teamToUpdate = game.teams[teamIndex];
            game.teams[teamIndex] = teamToUpdate.addPlayer(playerIndex, playerId);
            game.rounds[0].tichus.set(playerId.value, Tichu.NONE);
            game.rounds[0].ranks.set(playerId.value, Rank.NONE);
        });
    }

    numberOfPlayersInGame(): number {
        return this.teams.reduce((sum, team) => sum + Array.from(team.players.keys()).length, 0);
    }

    idsOfPlayersInGame(): PlayerId[] {
        return this.teams.flatMap(team => Array.from(team.players.values()));
    }

    getPlayerOfTeam(teamIndex: number, playerIndex: number) {
        return this.teams[teamIndex].getPlayer(playerIndex);
    }

    removePlayerFromTeam(teamIndex: number, playerIndex: number) {
        return this.mutate(game => {
            const teamToUpdate = game.teams[teamIndex];
            game.teams[teamIndex] = teamToUpdate.removePlayer(playerIndex);
        });
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

    round(roundNumber: number): Round {
        const round = this.rounds.find(round => round.roundNumber === roundNumber);
        if (!round) {
            throw new Error('Invariant violated: round does not exist');
        }
        return round;
    }

    updateRound(round: Round): Game {
        return this.mutate(game => {
            const roundIndex = game.rounds.findIndex(r => r.roundNumber === round.roundNumber);
            if (roundIndex < 0) {
                throw new Error('Invariant violated: cannot update non existing round');
            }
            game.rounds[roundIndex] = round;
        });
    }

    finishRound(roundNumber: number): Game {
        return this.mutate(game => {
            const roundIndex = game.rounds.findIndex(round => roundNumber === round.roundNumber);
            if (roundIndex < 0) {
                throw new Error('Invariant violated: cannot update non existing round');
            }
            const round = game.rounds[roundIndex];
            if (!round) {
                throw new Error('Invariant violated: cannot finish non existing round');
            }
            game.rounds[roundIndex] = round.finishRound();
        });
    }

    rankPlayer(roundNumber: number, playerId: PlayerId): Game {
        return this.mutate(game => {
            const round = game.rounds.find(round => round.roundNumber === roundNumber);
            if (!round) {
                throw new Error('Invariant violated: cannot finish non existing round');
            }
            game.rounds[round.roundNumber - 1] = round.rankPlayer(playerId.value, round.nextRank());
        });
    }

    resetRank(roundNumber: number, playerId: any) {
        return this.mutate(game => {
            const round = game.rounds.find(round => round.roundNumber === roundNumber);
            if (!round) {
                throw new Error('Invariant violated: cannot finish non existing round');
            }
            game.rounds[round.roundNumber - 1] = round.resetRank(playerId.value);
        });
    }

    nextRank(): Rank {
        return this.currentRound().nextRank();
    }

    totalPointsOfTeam(teamIndex: number): number {
        const team = this.teams[teamIndex];
        return this.rounds
                   .map(round => round.totalPoints(team))
                   .reduce((prev, current) => prev + current) || 0;
    }

    totalPointsOfRound(roundNumber: number, teamIndex: number): number {
        const team = this.teams[teamIndex];
        return this.rounds
                   .filter(round => round.roundNumber === roundNumber)
                   .map(round => round.totalPoints(team))
                   .reduce((prev, current) => prev + current) || 0;
    }

    totalPointsUpToRound(roundNumber: number, teamIndex: number): number {
        const team = this.teams[teamIndex];
        return this.rounds
                   .filter(round => round.roundNumber <= roundNumber)
                   .map(round => round.totalPoints(team))
                   .reduce((prev, current) => prev + current) || 0;
    }

    isComplete() {
        const totalPointsLeftTeam = this.totalPointsOfTeam(0);
        const totalPointsRightTeam = this.totalPointsOfTeam(1);
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

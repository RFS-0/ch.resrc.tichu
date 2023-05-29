import {Player, PlayerSchema, type RawPlayer} from './player';
import {Entity, type RawEntity} from './entity';
import {implement} from '../validation';
import {EntityIdSchema, TeamId, PlayerId} from '../value_objects';
import {z} from 'zod';

export interface RawTeam extends RawEntity {
    id: string
    name: string
    firstPlayer: RawPlayer
    secondPlayer: RawPlayer
}

export const TeamSchema = implement<RawTeam>().with({
    id: EntityIdSchema.shape.value,
    name: z.string(),
    firstPlayer: PlayerSchema,
    secondPlayer: PlayerSchema,
});

export class Team extends Entity {

    private _id: TeamId;
    private _name: string;
    private _firstPlayer: Player;
    private _secondPlayer: Player;

    constructor(
        input: RawTeam,
    ) {
        super(input);
        const result = TeamSchema.safeParse(input);
        if (!result.success) {
            throw new Error(`Preconditions to create ${Team.name} not met because ${result.error.message}`)
        }
        this._id = new TeamId({value: result.data.id});
        this._name = result.data.name;
        this._firstPlayer = new Player(result.data.firstPlayer);
        this._secondPlayer = new Player(result.data.secondPlayer);
    }


    isFirstPlayer(playerId: PlayerId): boolean {
        return this.firstPlayer.id.value === playerId.value;
    }

    isSecondPlayer(playerId: PlayerId): boolean {
        return this.secondPlayer.id.value === playerId.value;
    }

    get id(): TeamId {
        return this._id;
    }

    get name(): string {
        return this._name;
    }

    get firstPlayer(): Player {
        return this._firstPlayer;
    }

    get secondPlayer(): Player {
        return this._secondPlayer;
    }

    get players(): Player[] {
        return [this.firstPlayer, this.secondPlayer];
    }

    toRaw(): RawTeam {
        return {
            id: this.id.value,
            name: this.name,
            firstPlayer: this.firstPlayer.toRaw(),
            secondPlayer: this.secondPlayer.toRaw(),
        }
    }
}
